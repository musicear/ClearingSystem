package resoft.tips.chqxh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>三方协议删除/p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 11:02:00
 */
public class SfxySc implements Action {    
	private static final Log logger = LogFactory.getLog(SfxySc.class);    
    public int execute(Message msg) throws Exception {
    	    	
    	ACEPackager ace2010=(ACE2010)msg.get("ACEObj");
    	//初始化ACE交易报文体
    	ace2010.makeTransBody();
    	
    	String taxPayCode=(String)ace2010.pkTHBodyList.get("TaxPayCode");		//纳税人编码
    	String payAcct=(String)ace2010.pkTHBodyList.get("PayAcct");				//付款帐号
    	String protocolNo=(String)ace2010.pkTHBodyList.get("ProtocolNo");			//协议书号
    	
    	//查询是否有协议信息    	
    	int count=DBUtil.queryForInt("select count(*) from ProveInfo where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
    	logger.info("信合三方协议删除：select count(*) from ProveInfo where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"'  and protocolNo='"+protocolNo+"' ");
        if(count>0){
        	//如果协议为正常可以协议，不允许银行作废协议
        	count=DBUtil.queryForInt("select count(*) from ProveInfo where verifyResult='0' and EnabledFlag='Y' and payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
        	if (count<=0){
	        	//作废协议
	        	String removeTeller=(String)ace2010.pkTHHeadList.get("InputTeller");//删除柜员
	        	DateFormat dt=new SimpleDateFormat("yyyyMMddhhkkss");
	            String removeTime=dt.format(new Date());
	        	DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',removeTeller='"+removeTeller+"',removeTime='"+removeTime+"' where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
	        	
	        	ace2010.tradeStatus="000";
	        	msg.set("VCResult",ace2010.tradeStatus);
	        	msg.set("AddWord", "协议删除成功");        	
	        }else {
	        	ace2010.tradeStatus="002";
	        	msg.set("VCResult",ace2010.tradeStatus);
	        	msg.set("AddWord", "该协议不能作废");
	        }
        }else{        	
        	ace2010.tradeStatus="001";
        	msg.set("VCResult",ace2010.tradeStatus);
        	msg.set("AddWord", "协议不存在");        	
        }
    	    	
    	/**
    	 * 交易返回信息
    	 * 		成功：状态码[3]|成功信息
    	 * 		失败：状态码[3]|错误信息描述
    	 * */
    	if (ace2010.tradeStatus.equals("000")) {//成功
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	return SUCCESS;
    }
    
}
