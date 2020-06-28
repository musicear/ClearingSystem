package resoft.tips.action;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>三方协议查询</p>
 * Author: zhuchangwu
 * Date: 2007-8-23
 * Time: 18:06:06
 */
public class QueryProveInfo implements Action {
	 public int execute(Message msg) throws Exception {
	        String TaxPayCode = msg.getString("TaxPayCode");
	        String ProtocolNo = msg.getString("ProtocolNo");
	        //String PayAcct = msg.getString("AcctNo");

	        //查询帐户是否存在QueryUtil.queryRowSet
	        String sql="select EnabledFlag,taxPayName,TaxPayCode,ProtocolNo,TaxOrgCode,PayOpBkCode," +
	        		"PayBkCode,PayAcct,HandOrgName,sendTime " +
	        		"from ProveInfo where TaxPayCode='" + TaxPayCode + "' and protocolNo='" + ProtocolNo + "'";
	        List proveinfos = QueryUtil.queryRowSet(sql);
	        if(proveinfos.size()>0){
	        	Map row = (Map) proveinfos.get(0);
	        	String EnabledFlag=(String)row.get("EnabledFlag");
	        	if("Y".equals(EnabledFlag)){
	                msg.set("TaxPayName", (String)row.get("taxPayName"));//纳税人名称
	                msg.set("PayAcctName", (String)row.get("HandOrgName"));//缴款单位名称
	                msg.set("TaxPayCode", (String)row.get("TaxPayCode"));//纳税人编号
	                msg.set("ProtocolNo", (String)row.get("ProtocolNo"));//协议书号
	                msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));//征收机关代码
	                //msg.set("PayOpBkCode", (String)row.get("ReckBankNo"));//付款行行号(人行下发的)
	                msg.set("PayAcct", (String)row.get("PayAcct"));//付款帐户
	                msg.set("PayBkCode",(String)row.get("PayBkCode"));//付款清算行行号(人行下发的)
	                //msg.set("BankOrgCode",(String)row.get("ReckBankNo"));//付款行行号(各地对应的)
	                //msg.set("ContactDate", (String)row.get("sendTime"));//付款帐户
	                String date = (String)row.get("sendTime");
	                //char[] as = date.toCharArray();
	                String newDate=date.substring(0, 4)+date.substring(5, 7)+date.substring(8, 10);
	             
	                msg.set("ContactDate",newDate);//付款帐户
//	                msg.set("PayBkCode",payBkCode);//写死了
//	                msg.set("PayBkName",payBkName);//写死了
	                String sql1="select TaxOrgName from TaxOrgMng where TaxOrgCode='"+(String)row.get("TaxOrgCode")+"'";
	                List proveinfos1 = QueryUtil.queryRowSet(sql1);
	                if(proveinfos1.size()>0){
	                Map row1 = (Map) proveinfos1.get(0);
	                msg.set("TaxOrgName",(String)row1.get("TaxOrgName"));
	                }else{
	                	msg.set("TaxOrgName","征收机关不存在!");
	                }
	                
	                String sql3="select bm.genBankName from BankMng bm where bm.reckBankNo='"+(String)row.get("PayBkCode")+"'";
	                //System.out.println("sql3:............"+sql3);
	                List proveinfos3 = QueryUtil.queryRowSet(sql3);
	                if(proveinfos3.size()>0){
	                Map row3 = (Map) proveinfos3.get(0);
	                msg.set("PayBkName",(String)row3.get("genBankName"));
	                //String aaa = (String)row3.get("genBankName");
	                //System.out.println("aaa:............."+aaa);
	                }else{
	                	msg.set("PayBkName","付款清算行名不存在!");
	                }
	                
	                String sql2="select bo.ReckBankNo from bankorginfo bo where bo.bankorgcode='"+(String)row.get("PayOpBkCode")+"'";
	                List proveinfos2 = QueryUtil.queryRowSet(sql2);
	                if(proveinfos2.size()>0){
	                Map row2 = (Map) proveinfos2.get(0);
	                msg.set("PayOpBkCode", (String)row2.get("ReckBankNo"));//付款行行号(人行下发的)
	                
	                String sql4="select bm.genBankName from BankMng bm where bm.reckBankNo='"+(String)row2.get("ReckBankNo")+"'";
	                List proveinfos4 = QueryUtil.queryRowSet(sql4);
	                if(proveinfos4.size()>0){
	                Map row4 = (Map) proveinfos4.get(0);
	                msg.set("PayOpBkName",(String)row4.get("GenBankName"));
	                }else{
	                	msg.set("PayOpBkName","付款行名不存在!");
	                }
	                }
	                //msg.set("TaxOrgName",(String)row.get("TaxOrgName"));
	                //msg.set("PayOpBkName",(String)row.get("GenBankName"));
	                msg.set("ReturnResult", "Y");
	                msg.set("AddWord", "此协议存在");
	                msg.set("ProveStatus", "协议有效");
	        	}else{
	                msg.set("ReturnResult", "N");
	                msg.set("AddWord", "此协议已被删除");
	        	}
	        }else{
	            msg.set("ReturnResult", "N");
	            msg.set("AddWord", "此协议不存在");
	        }

	        return SUCCESS;
	    }
//	 private String payBkCode;
//	 private String payBkName;
//	public void setPayBkCode(String payBkCode) {
//		this.payBkCode = payBkCode;
//	}
//	public void setPayBkName(String payBkName) {
//		this.payBkName = payBkName;
//	}
}
