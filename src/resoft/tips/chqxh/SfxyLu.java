package resoft.tips.chqxh;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>三方协议信息录入</p>
 * Author: liwei
 * Date: 2007-08-08
 * Time: 17:54:00
 */
public class SfxyLu implements Action {    
	private static final Log logger = LogFactory.getLog(SfxyLu.class);	
    
	public int execute(Message msg) throws Exception {
    	String payBkCode="402653000011",taxOrgName="",payOpBkCode="",payOpBkName="";    	
    	ACEPackager ace2008=(ACE2008)msg.get("ACEObj");
    	//初始化ACE交易报文体
    	ace2008.makeTransBody();
    	
    	Map params=new HashMap();
    	params.put("PayAcct", ace2008.pkTHBodyList.get("PayAcct"));				//帐号
    	params.put("TaxOrgCode", ace2008.pkTHBodyList.get("TaxOrgCode"));		//征收机关代码
    	params.put("TaxPayCode", ace2008.pkTHBodyList.get("TaxPayCode"));		//纳税人编码
    	params.put("TaxPayName", ace2008.pkTHBodyList.get("TaxPayName"));		//纳税人名称
    	params.put("HandOrgName", ace2008.pkTHBodyList.get("PayAcctName"));		//缴款单位名称
    	params.put("ProtocolNo", ace2008.pkTHBodyList.get("ProtocolNo"));		//协议号    	    	
    	params.put("RegisterOrgCode", ace2008.pkTHHeadList.get("BankOrgCode")); //登记机构
    	params.put("payBkCode", payBkCode);										//付款行行号
    	
    	//取征收机关名称    	
    	taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and TaxOrgCode='"+ace2008.pkTHBodyList.get("TaxOrgCode")+"'");
    	if (taxOrgName!=null && !taxOrgName.equals("")){
    		msg.set("TaxOrgName", taxOrgName);
    		ace2008.tradeStatus="000";
    	}else{		
    		ace2008.tradeStatus="011";				//征收机关代码错误
    		msg.set("VCResult", ace2008.tradeStatus);			
    		msg.set("AddWord", "征收机关代码错");    		
    	}
        //如果征收机关代码争取，验证银行代码
    	if (ace2008.tradeStatus.equals("000")) { 	
    		//进行银行代码转换
			payOpBkCode=DBUtil.queryForString("select DMBENO from pmdma where DMSBNO='"+ace2008.pkTHHeadList.get("BankOrgCode")+"' ");			    	
    		if (payOpBkCode!=null && !payOpBkCode.equals("")){
    			msg.set("PayOpBkCode", payOpBkCode);            
    	        params.put("PayOpBkCode", payOpBkCode);	//付款开户行行号                	        
    	        ace2008.tradeStatus="000";
    		}else {	
    			ace2008.tradeStatus="012";				//征收机关代码错误
    			msg.set("VCResult", ace2008.tradeStatus);			
    			msg.set("AddWord", "银行代码错");
    		}
        }
    	//进行协议的录入
        if (ace2008.tradeStatus.equals("000")) {
        	//检索此协议书号是否存在   注：整个数据库中协议书好唯一，只表示一条协议记录
        	int count=DBUtil.queryForInt("select count(*) from proveInfo where ProtocolNo='"+(String)ace2008.pkTHBodyList.get("ProtocolNo")+"'");
        	if (count<=0) {
	        	//检索是否有有效的协议     注：一个纳税人只能有一份有效的协议，不能要签多份协议   	
	        	count=DBUtil.queryForInt("select count(*) from ProveInfo where EnabledFlag='Y' and taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' ");
		        if(count<=0){
		        	//查询是否有成功签约的信息
			    	count=DBUtil.queryForInt("select count(*) from ProveInfo where verifyResult='0' and EnabledFlag='Y' and taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' and payAcct='"+params.get("PayAcct")+"' and protocolNo='"+params.get("ProtocolNo")+"' ");
			    	if (count>0) {//已经正常签约    		
			    		ace2008.tradeStatus="001";			//已签约
			    		msg.set("VCResult", ace2008.tradeStatus);			
			    		msg.set("AddWord", "已签约");    		
			    	}else {	    		
			    		//先删除再录入
			    		//DBUtil.executeUpdate("delete from ProveInfo where taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' and payAcct='"+params.get("PayAcct")+"' and protocolNo='"+params.get("ProtocolNo")+"'");	    		
			    		//录入信息
			    		params.put("InputTeller", ace2008.pkTHHeadList.get("InputTeller"));
			        	DateFormat dt=new SimpleDateFormat("yyyyMMddhhkkss");
			            params.put("SendTime",dt.format(new Date()));			//发送日期
			            params.put("VerifyResult", "1");						//0:通过；1:未通过
			            params.put("EnabledFlag", "Y");							//Y:有效；1:无效
			            params.put("RegisterTime", dt.format(new Date()));		//登记日期		                        
			    		logger.info(params);
			            DBUtil.insert("ProveInfo", params);	           
			            //交易成功
			            ace2008.tradeStatus="000";
			            msg.set("VCResult", ace2008.tradeStatus);			//成功
			            msg.set("AddWord", "成功签约");            	            	    	
			    	}
		        }else {
		        	ace2008.tradeStatus="002";			//已有该纳税人的协议信息，不能在录入
		    		msg.set("VCResult", ace2008.tradeStatus);			
		    		msg.set("AddWord", "已有该纳税人协议信息");   
		        }
        	}else {
        		ace2008.tradeStatus="004";				//已有协议书号信息，不能在录入
	    		msg.set("VCResult", ace2008.tradeStatus);			
	    		msg.set("AddWord", "已有该协议书号，不能重复使用");
        	}
        }    	
    	
    	/**
    	 * 交易返回信息
    	 * 		成功：状态码[3]|征收机关名称|付款行行号
    	 * 		失败：状态码[3]|错误信息描述
    	 * */
    	if (ace2008.tradeStatus.equals("000")) {//成功
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("TaxOrgName")+"|"+msg.getString("PayOpBkCode"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	
    	logger.info("返回交易报文体：["+msg.getString("ACETrandBody")+"]");
    	return SUCCESS;
    }    
}
