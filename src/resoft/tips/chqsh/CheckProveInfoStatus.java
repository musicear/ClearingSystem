package resoft.tips.chqsh;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>查询是否已经签约</p>
 * Author: fanchengwei
 * 
 * Update by liwei 2008.10.29
 * Description：修改了对于账号的总账行号的验证查询SQL的调整
 */
public class CheckProveInfoStatus implements Action {
    
	private static final Log logger = LogFactory.getLog(CheckProveInfoStatus.class);
    
	public int execute(Message msg) throws Exception{
    	
		//String PAYBKCODE="313653000013";							//清算行行号		
		String payAcct = msg.getString("PayAcct");			//付款帐号
        String taxPayCode = msg.getString("TaxPayCode");   	//纳税人编码
        String taxPayName = msg.getString("TaxPayName");		//纳税人名称
        String protocolNo = msg.getString("ProtocolNo");		//协议书号
        String taxOrgCode = msg.getString("TaxOrgCode");		//征收机关代码
        String BranchNo=msg.getString("BranchNo").trim();           //节点代码
        String PayBkCode=msg.getString("PayBkCode");         //付款清算行行号
        String PayOpBkName= msg.getString("PayOpBkName");   //付款开户行名称
        String payOpBkCode=msg.getString("PayOpBkCode");     //付款开户行行号
        String handOrgName=msg.getString("HandOrgName");
       // String AcctSeq=msg.getString("AcctSeq").trim();             //账户类型
       // int len = payAcct.length();
        //String Check_type="";
        //Check_type = payAcct.substring(6, 8);
        //logger.info("Check_type is "+Check_type);
        if(payAcct == null || payAcct.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "付款账号不能为空！");
        	logger.info("付款账号不能为空！");
            return FAIL;
        }
        
        if(taxPayCode == null || taxPayCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "纳税人编码不能为空！");
        	logger.info("纳税人编码不能为空！");
            return FAIL;
        }
        
        if(taxPayName == null || taxPayName.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "纳税人名称不能为空！");
        	logger.info("纳税人名称不能为空！");
            return FAIL;
        }
        
        if(protocolNo == null || protocolNo.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "协议书号不能为空！");
        	logger.info("协议书号不能为空！");
            return FAIL;
        }
        
        if(taxOrgCode == null || taxOrgCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "征收机关代码不能为空！");
        	logger.info("征收机关代码不能为空！");
            return FAIL;
        }
       
        if(PayOpBkName == null || PayOpBkName.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "付款开户行名称不能为空！");
        	logger.info("付款开户行名称不能为空！");
            return FAIL;
        }
        
        if(payOpBkCode == null || payOpBkCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "付款开户行行号不能为空！");
        	logger.info("付款开户行行号不能为空！");
            return FAIL;
        }
        
        if(getStatus(PayBkCode) == 0){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "付款清算行行号不正确！");
        	logger.info("付款清算行行号不正确！");
            return FAIL;
        }
       /*
        if(AcctSeq.equals("0")){
        	if(len == 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号类型与账号不匹配或账号错误，请重新填写！");
            	logger.info("账号类型与账号不匹配");
                return FAIL;
        	}
        	if(!Check_type.equals("04")){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号类型与账号不匹配或账号错误，请重新填写！");
            	logger.info("账号类型与账号不匹配");
                return FAIL;
        	}
        }
        if(AcctSeq.equals("1")){
        	if(len != 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号类型与账号不匹配或账号错误，请重新填写！");
            	logger.info("账号类型与账号不匹配");
                return FAIL;
        	}
        }
        if(AcctSeq.equals("2")){
        	if(len == 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号类型与账号不匹配或账号错误，请重新填写！");
            	logger.info("账号类型与账号不匹配");
                return FAIL;
        	}
        	if(!Check_type.equals("10")){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号类型与账号不匹配或账号错误，请重新填写！");
            	logger.info("账号类型与账号不匹配");
                return FAIL;
        	}
        }*/
        if(PayBkCode == null){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "付款清算行行号不能为空");
        	logger.info("清算行行号为空");
            return FAIL;
        }
        
        String sql = "select count(*) from ProveInfo where EnabledFlag='Y' and protocolNo='" + protocolNo +"'";
        logger.info("验证是否已经有签约协议 sql:" + sql);
        int count = 0;
        try{
        	count = DBUtil.queryForInt(sql);
        }catch(Exception e){
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "数据库操作出错！");
            logger.info("数据库操作出错");
            return FAIL;
        }        
        if (count > 0){ //已经成功签约        
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "该协议书号已经签约，请重新录入！");
            return FAIL;
        } else{        	
        /*	try{
    	        int result=QueryAcct.checkSzhh(msg);        
    	        if ( result!=0 ) {
    	        	return result;
    	        }
            }catch(Exception e){
            	e.printStackTrace();
            	msg.set("ReturnResult", "N");
            	msg.set("AddWord", "账号错误");
            	return FAIL;
            }
        	*/
        	Map params=new HashMap();        	
    		params.put("PayOpBkName",PayOpBkName);                     //付款开户行名称
        	params.put("taxPayName", taxPayName);  							//纳税人名称
        	params.put("payacct", payAcct);        							//付款账户
        	params.put("protocolNo", protocolNo);  							//协议书号
        	params.put("taxOrgCode", taxOrgCode);  							//征收机关代码
        	params.put("taxPayCode", taxPayCode);  							//纳税人编码
        	params.put("payOpBkCode", payOpBkCode);//付款开户行行号
        	params.put("handOrgName", handOrgName);//缴款单位名称        	       	
        	params.put("sendTime", DateTimeUtil.getDateString());			//协议签订日期
            params.put("verifyResult","1");									//验证结果
            params.put("AddWord","协议签定成功!");    							//附言
            params.put("inputTeller",msg.getString("UserId"));				//柜员号
            params.put("EnabledFlag","Y");									//有效协议
            params.put("PAYBKCODE",PayBkCode);								//清算行行号
            params.put("BranchNo", BranchNo);								//开户机构号
            try{ 
            	String protocolNoquerysql = "select count(*) from ProveInfo where protocolNo='" + protocolNo +"'";
            	int protocolcount = DBUtil.queryForInt(protocolNoquerysql);
            	if(protocolcount>0)
            		DBUtil.executeUpdate("delete from ProveInfo where protocolNo='"+protocolNo+"'");
            	DBUtil.insert("ProveInfo",params);
            	msg.set("ReturnResult", "Y");
                msg.set("AddWord", "协议签定成功！");
                logger.info("协议签定成功");
            }catch(Exception e){
            	e.printStackTrace();
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "插入数据重复！");
                logger.info("插入数据库出错！");                
                return FAIL;
            }
            return SUCCESS;
        }
    }			
	//yangyuanxu add 
	public int getStatus(String PayBkCode) throws SQLException{
		
		int len;
		len=DBUtil.queryForInt("select count(*) from bank_relation where paybankcode='"+PayBkCode+"'");	
		if(len > 0)
			return 1;
		else
			return 0;
	}	
	
}
