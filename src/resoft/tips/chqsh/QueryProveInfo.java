package resoft.tips.chqsh;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>三方协议查询</p>

 */
public class QueryProveInfo implements Action {
	 
	 private static final Log logger = LogFactory.getLog(QueryProveInfo.class);
	 
	 public int execute(Message msg) throws Exception {
		 
	        String TaxPayCode = msg.getString("TaxPayCode");
	        String ProtocolNo = msg.getString("ProtocolNo");	        
	        String verifyresult = "";
	        String sql = "",sqlWhere="",enabledflag="";
	        
     
           if((TaxPayCode==null ||TaxPayCode.equals("")) &&(ProtocolNo==null || ProtocolNo.equals("")) ){
        	   msg.set("ReturnResult", "N");
	            msg.set("AddWord", "纳税人编码与协议书号不能同时为空！");
	            return FAIL;
           }
	       if((TaxPayCode==null ||TaxPayCode.equals(""))&&(ProtocolNo!=null && !ProtocolNo.equals(""))) //纳税人编码为空，协议书号不为空	        
	       {
	    	   sqlWhere+=" and protocolNo='" + ProtocolNo + "' ";
	       }
	    	   //纳税人编码查询
	       if ( (TaxPayCode!=null && !TaxPayCode.equals(""))&& (ProtocolNo==null || ProtocolNo.equals(""))) {
	        	sqlWhere+=" and TaxPayCode='" + TaxPayCode + "' "; 
	        } 
	        //协议书号
	        if ( (ProtocolNo!=null && !ProtocolNo.equals(""))&&(TaxPayCode!=null && !TaxPayCode.equals("")) ) {
	        	sqlWhere+=" and TaxPayCode='" + TaxPayCode + "' "; 
	        	sqlWhere+=" and protocolNo='" + ProtocolNo + "' ";
	        }
	        	        
	        sql="select * from ProveInfo where 1=1"+sqlWhere+" order by enabledflag";	         
        
	        logger.info("三方协议查询SQL is:"+sql);
	        List proveinfos = QueryUtil.queryRowSet(sql);
	        if(proveinfos.size()>0){
	        	
	        	Map row = (Map) proveinfos.get(proveinfos.size()-1);	   
	        	String payOpbkCode = (String)row.get("PayOpbkCode");
	        	//String payOpbkName = DBUtil.queryForString("select bankorgname from bankorginfo where Bankorgcode='"+payOpbkCode.trim()+"'");
        		//msg.set("PayOpBkName", payOpbkName.trim()); //开户行名称  
        		String payBKCode = (String)row.get("PayBkCode");
        		msg.set("PayBkCode", payBKCode.trim());		//开户行代码
                msg.set("TaxPayName", (String)row.get("taxPayName"));	//纳税人名称
                msg.set("PayAcctName", (String)row.get("HandOrgName"));	//缴款单位名称
                msg.set("TaxPayCode", (String)row.get("TaxPayCode"));	//纳税人编号
                msg.set("ProtocolNo", (String)row.get("ProtocolNo"));	//协议书号
                String taxOrgCode = (String)row.get("TaxOrgCode");
                
                
                msg.set("TaxOrgCode", taxOrgCode.trim());	//征收机关代码
                String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + taxOrgCode + "'");
                if(taxOrgName.equals("")||taxOrgName.equals(null))
                	
                	msg.set("TaxOrgName", "");	//征收机关名称
                else
                	msg.set("TaxOrgName", taxOrgName);
                msg.set("PayOpBkCode", payOpbkCode.trim());	//付款开户行行号
                
                
                msg.set("PayAcct", (String)row.get("PayAcct"));			//付款帐户
                //String sendTime = (String)row.get("sendTime");
                //String newDate=sendTime.trim().substring(0, 4)+sendTime.trim().substring(5, 7)+sendTime.trim().substring(8, 10);
                //msg.set("ContactDate",newDate);		//付款账户三方协议签订时间      
             //   String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='"+taxOrgCode+"'");
             //   msg.set("TaxOrgName",taxOrgName);    //征收机关名称   
              //  String payBKName = DBUtil.queryForString("select bankorgname from bankorginfo where bankorgcode='"+payBKCode.trim()+"'");
              //  msg.set("PayBkName", payBKName);							//清算行名称
                msg.set("ReturnResult", "Y");
                msg.set("AddWord", "此协议存在");
                verifyresult = (String)row.get("verifyresult");
                enabledflag = (String)row.get("EnabledFlag");
                logger.info("状态为："+enabledflag);
                String addWord = (String)row.get("AddWord");
                String AddWord = addWord.substring(0, 4);

                if(verifyresult.equals("0")&& enabledflag.equals("Y")){
                	msg.set("AddWord", "此协议已录入已验证");					//协议状态
                }else if(verifyresult.equals("1")&&enabledflag.equals("Y")){
                	if("验证失败".equals(AddWord)){
                		msg.set("AddWord", addWord);
                	}else{
                	msg.set("AddWord", "此协议已录入未验证");
                	}
                }else if((verifyresult.equals("1")&&enabledflag.equals("N")) || (verifyresult.equals("1")&&enabledflag.equals("C"))){
                	msg.set("AddWord", "此协议已经删除");
                }else{
                	msg.set("AddWord", "此协议状态错误");
                }
                if(taxOrgName.equals("")||taxOrgName.equals(null))
                {
                	msg.set("AddWord", "征收机关不存在");
                	
                }
	        }else{
	            msg.set("ReturnResult", "N");
	            msg.set("AddWord", "此协议不存在");
	        }
	        return SUCCESS;
	    }
}
