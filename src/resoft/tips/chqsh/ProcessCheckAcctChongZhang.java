package resoft.tips.chqsh;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;
/**
 * <p>重庆商行与后台冲帐,TIPS日切的时候做一次抹账</p>
 * Author: fanchengwei
 * Date: 2007-11-10
 * Time: 09:36:06
 * 
 * Update by liwei 2008.10.27
 * Description：添加了对于抹账后修改相应状态时候的数据检索条件
 * 
 * 修改了冲账完成后的状态值：
 * adjustAcct表的adjustStatus字段和realtimepayment、batchPackDetail的checkStatus字段的值保持一直
 * 值列表说明：
 * 			0：表示该数据为异常信息，当result为”90000“并且MSG_DATA为”9004“需要做抹帐处理
 * 			1：表示不需要冲账
 * 			2：冲账成功
 * 			3：冲账错误或冲账异常
 * 
 */
public class ProcessCheckAcctChongZhang implements Action{
	
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctChongZhang.class);
	
	public int execute(Message msg) {
    
		SendMsgToBankSystem send = new SendMsgToBankSystem();
		String tipsDate = msg.getString("ChkDate");        		//对账日期
        String chkAcctType=msg.getString("ChkAcctType");		//对账类型
        String IADAC_DAT = "";
        String MSG_DATA = "9004";                               //对账结果
        String JRN_NO = "";
        String VCH_NO = "";
        String TR_CODE = "";
        String RcvMsg = "";
        String error = "";
        String updateSql="";
        
        if(chkAcctType.equals("1")){							//0:日间;1:日切
        	/**
        	 * 进行抹账  	注意要加交易结果为9004这个条件
        	 */
    		String sql = "select * from adjustAcct where adjustStatus='0' and MSG_DATA='" + MSG_DATA + "'and chkDate='"+tipsDate+"' order by traNo";
    		logger.info("抹帐数据检索SQL is:"+sql);
            List adjustLists = null;
            try{
            	adjustLists= QueryUtil.queryRowSet(sql);
            }catch(Exception e){
            	e.printStackTrace();
            	logger.info("抹帐数据查询出错！！");
            }
            synchronized(TuxedoPackager.getTuxedoPackager()){
	            
            	for (int i=0;i<adjustLists.size();i++){
	            
		    		Map row=(Map)adjustLists.get(i);  		    		
		    		String entrusDate=(String)row.get("ENTRUSTDATE");
	    			String taxOrgCode=(String)row.get("TAXORGCODE");
	    			String traNo=(String)row.get("TraNo");
	    			String packNo=(String)row.get("PackNo")==null?"":(String)row.get("PackNo");
	    			
	    			//初始化抹帐信息	    			
		    		JRN_NO = (String)row.get("JRN_NO");
		    		VCH_NO = (String)row.get("VCH_NO");
		    		TR_CODE = (String)row.get("TR_CODE");
		    		RcvMsg = send.sendOutMsg(JRN_NO, VCH_NO, TR_CODE,VCH_NO.substring(0, 4));
		    		logger.info("与核心抹账接到核心应答：\n["+RcvMsg+"]");
		    		if(RcvMsg.length()==85){
		    			//银行端缴款未做		    			 
//		    			id = RcvMsg.substring(37,38);							//后台交易结果
//				    	TR_CODE = "0" + RcvMsg.substring(11, 14);				//交易码
				    	IADAC_DAT = RcvMsg.substring(21, 29);					//会计日
				    	JRN_NO = RcvMsg.substring(54, 61);						//日志号
				    	VCH_NO = RcvMsg.substring(69, 77);						//传票号
//				    	MSG_DATA = RcvMsg.substring(81, 85);					//处理结果描述
		    			error = RcvMsg.substring(81, 85);		    					    			
		    					    			
		    			if(error.equals("9004")){		//抹账成功
		    				DBUtil.executeUpdate("update adjustAcct set adjustStatus='2',reason='已抹帐',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");		    			
				    		if ( !packNo.equals("") ) {
				    			updateSql="update batchPackDetail set checkStatus='2',chkDate='"+tipsDate+"',result='00001',addWord='已抹帐' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";				    			
				    		}else {
				    			updateSql="update realTimePayMent set checkStatus='2',chkDate='"+tipsDate+"',result='00001',addWord='已抹帐' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";				    			
				    		}	
				    		logger.info("抹帐成功!修改状态SQL is:"+updateSql);
				    		DBUtil.executeUpdate(updateSql);
				    		updateSql="";
		    			}else{							//抹账失败
		    				DBUtil.executeUpdate("update adjustAcct set adjustStatus='3',reason='抹帐失败请核对',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");
				    		if ( !packNo.equals("") ) {
				    			updateSql="update batchPackDetail set checkStatus='3',chkDate='"+tipsDate+"',result='00002',addWord='抹帐失败请核对' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
				    		}else {
				    			updateSql="update realTimePayMent set checkStatus='3',chkDate='"+tipsDate+"',result='00002',addWord='抹帐失败请核对' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
				    		}
				    		logger.info("抹帐失败!修改状态SQL is:"+updateSql);
				    		DBUtil.executeUpdate(updateSql);
				    		updateSql="";
		    			}
		    		}else {								//抹账异常			    		
		    			DBUtil.executeUpdate("update adjustAcct set adjustStatus='3',reason='抹帐其它错误请核对',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");
			    		if ( !packNo.equals("") ) {
			    			updateSql="update batchPackDetail set checkStatus='3',chkDate='"+tipsDate+"',result='00003',addWord='抹帐其它错误请核对' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"'and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
			    		}else {
			    			updateSql="update realTimePayMent set checkStatus='3',chkDate='"+tipsDate+"',result='00003',addWord='抹帐其它错误请核对' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
			    		}
			    		logger.info("抹帐其它错误!修改状态SQL is:"+updateSql);
			    		DBUtil.executeUpdate(updateSql);
			    		updateSql="";
			    	}
		    		JRN_NO = VCH_NO = TR_CODE = RcvMsg = error = "";
	            }   
            }
        }
        return SUCCESS;
    }
	
	public static void main(String[] args){
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
		Message msg=new DefaultMessage();
		msg.set("ChkDate", DateTimeUtil.getDateString());
		msg.set("ChkAcctType", "1");
		(new ProcessCheckAcctChongZhang()).execute(msg);
	}
	
}
