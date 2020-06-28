package resoft.tips.chqsh;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>查询是否已经签约</p>
 * Author: liwei
 * Date: 2007-07-09
 * update: yangyuanxu
 */
public class CheckProveDelete implements Action {
    private static final Log logger = LogFactory.getLog(CheckProveDelete.class);
    public int execute(Message msg) throws Exception {
        String payAcct = msg.getString("PayAcct");//付款帐号
        String taxPayCode = msg.getString("TaxPayCode");//纳税人编码
        String ProtocolNo = msg.getString("ProtocolNo");//协议书号
       // String BranchNo = msg.getString("BranchNo");//网点号
        ///如果税务需要验证则需要加上 条件and verifyResult='0'
        String checkSql="select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='Y' "
            +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='0' and PAYACCT='"+payAcct+"'";
        logger.info("查询三方协议此三方协议是否已验证 sql:" + checkSql);

        
        String sql = "select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='Y' "
            +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='1' and PAYACCT='"+payAcct+"'";
        logger.info("查询未验证的三方协议 sql:" + sql);
        List rowSet = QueryUtil.queryRowSet(sql);

        String sqlchexiao = "select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='N' "
    +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='0' and PAYACCT='"+payAcct+"'";
        	logger.info("查询已撤销的三方协议 sql:" + sqlchexiao);
        List rowSetchexiao = QueryUtil.queryRowSet(sqlchexiao);

        List rowSetchecked = QueryUtil.queryRowSet(checkSql);
        if(rowSetchecked.size()>0)
        {
        	Map row=(Map)rowSetchecked.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//征收机关名称
            else
            	msg.set("TaxOrgName", taxOrgName);
        	msg.set("TaxPayName", (String)row.get("TaxPayName"));
        	msg.set("HandOrgName", (String)row.get("HandOrgName"));
        	msg.set("PayOpBkCode", (String)row.get("PayOpBkCode"));
        	msg.set("PayBkCode", (String)row.get("PayBkCode"));
        	msg.set("PayAcct", (String)row.get("PayAcct"));
//        	String EnabledFlag = (String)row.get("EnabledFlag");
//        	String verifyResult = (String)row.get("verifyResult");
            Map params = new HashMap();
            params.put("RemoveTeller", msg.getString("Teller"));//泉州固定为999999
            logger.info("授权柜员号："+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "该协议处于验证状态，不能删除!");
            logger.info("查询已验证的三方协议时的回复 ReturnResult:N,AddWord:该协议处于验证状态，不能删除!" );

            return FAIL;
        }
        
        
        else if (rowSet.size() > 0) {//三方协议未验证
        	Map row=(Map)rowSet.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//征收机关名称
            else
            	msg.set("TaxOrgName", taxOrgName);
        	msg.set("TaxPayName", (String)row.get("TaxPayName"));
        	msg.set("HandOrgName", (String)row.get("HandOrgName"));
        	msg.set("PayOpBkCode", (String)row.get("PayOpBkCode"));
        	msg.set("PayBkCode", (String)row.get("PayBkCode"));
        	msg.set("PayAcct", (String)row.get("PayAcct"));
//        	String EnabledFlag = (String)row.get("EnabledFlag");
//        	String verifyResult = (String)row.get("verifyResult");
            Map params = new HashMap();
            params.put("RemoveTeller", msg.getString("Teller"));//泉州固定为999999
            logger.info("授权柜员号："+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
             
            try
            { 
	            //msg.set("PayAcct", payAcct);
	            msg.set("EnabledFlag", "N");
	            msg.set("verifyResult", "1");
	            //logger.info("--------付款账户checkprovedelete----------"+payAcct);
	       //     String bankorgname = DBUtil.queryForString("select bankorgname from bankorginfo where netcode='"+BranchNo+"'");
	          //  msg.set("OrgName", bankorgname);
	            
	            msg.set("ReturnResult", "Y");
	            msg.set("AddWord", "协议删除成功");
	            
	            DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',RemoveTeller=#RemoveTeller#,RemoveTime=#RemoveTime# where EnabledFlag='Y' and TaxPayCode='" + taxPayCode +  "' and protocolNo='" + ProtocolNo + "' ", params);
	            return SUCCESS;
            }
            catch (Exception e)
            {
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "该协议不存在或者已经无效");
               return FAIL;
            } 
        } 
        else if (rowSetchexiao.size() > 0) //三方协议已撤销，可以删除
        {	
        	Map row=(Map)rowSetchexiao.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//征收机关名称
            else
            	msg.set("TaxOrgName", taxOrgName);
        	msg.set("TaxPayName", (String)row.get("TaxPayName"));
        	msg.set("HandOrgName", (String)row.get("HandOrgName"));
        	msg.set("PayOpBkCode", (String)row.get("PayOpBkCode"));
        	msg.set("PayBkCode", (String)row.get("PayBkCode"));
        	msg.set("PayAcct", (String)row.get("PayAcct"));
//        	String EnabledFlag = (String)row.get("EnabledFlag");
//        	String verifyResult = (String)row.get("verifyResult");
            Map params = new HashMap();
            params.put("RemoveTeller", msg.getString("Teller"));//泉州固定为999999
            logger.info("授权柜员号："+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
            
            
            
            try
            {
            
            //msg.set("PayAcct", payAcct);
            msg.set("EnabledFlag", "N");
            msg.set("verifyResult", "1");
            //logger.info("--------付款账户checkprovedelete----------"+payAcct);
       //     String bankorgname = DBUtil.queryForString("select bankorgname from bankorginfo where netcode='"+BranchNo+"'");
          //  msg.set("OrgName", bankorgname);
            
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "协议删除成功");
            
            DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',RemoveTeller=#RemoveTeller#,RemoveTime=#RemoveTime# where EnabledFlag='Y' and TaxPayCode='" + taxPayCode +  "' and protocolNo='" + ProtocolNo + "' ", params);
            return SUCCESS;
            }
            catch (Exception e)
            {
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "该协议不存在或者已经无效");
               return FAIL;
            }
            
            
        }
        
        else { 
            
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "该协议不存在，已经无效或已签定");
           return FAIL;

      }
    }
    private String getRandomTraNo() {
        String time = DateTimeUtil.getTimeByFormat("hhmmss");
        NumberFormat nf = new DecimalFormat("00");
        return time + nf.format(Math.random() * 100);
    }
    }