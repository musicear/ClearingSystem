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
 * <p>��ѯ�Ƿ��Ѿ�ǩԼ</p>
 * Author: liwei
 * Date: 2007-07-09
 * update: yangyuanxu
 */
public class CheckProveDelete implements Action {
    private static final Log logger = LogFactory.getLog(CheckProveDelete.class);
    public int execute(Message msg) throws Exception {
        String payAcct = msg.getString("PayAcct");//�����ʺ�
        String taxPayCode = msg.getString("TaxPayCode");//��˰�˱���
        String ProtocolNo = msg.getString("ProtocolNo");//Э�����
       // String BranchNo = msg.getString("BranchNo");//�����
        ///���˰����Ҫ��֤����Ҫ���� ����and verifyResult='0'
        String checkSql="select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='Y' "
            +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='0' and PAYACCT='"+payAcct+"'";
        logger.info("��ѯ����Э�������Э���Ƿ�����֤ sql:" + checkSql);

        
        String sql = "select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='Y' "
            +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='1' and PAYACCT='"+payAcct+"'";
        logger.info("��ѯδ��֤������Э�� sql:" + sql);
        List rowSet = QueryUtil.queryRowSet(sql);

        String sqlchexiao = "select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='N' "
    +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' and verifyResult='0' and PAYACCT='"+payAcct+"'";
        	logger.info("��ѯ�ѳ���������Э�� sql:" + sqlchexiao);
        List rowSetchexiao = QueryUtil.queryRowSet(sqlchexiao);

        List rowSetchecked = QueryUtil.queryRowSet(checkSql);
        if(rowSetchecked.size()>0)
        {
        	Map row=(Map)rowSetchecked.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//���ջ�������
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
            params.put("RemoveTeller", msg.getString("Teller"));//Ȫ�ݹ̶�Ϊ999999
            logger.info("��Ȩ��Ա�ţ�"+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "��Э�鴦����֤״̬������ɾ��!");
            logger.info("��ѯ����֤������Э��ʱ�Ļظ� ReturnResult:N,AddWord:��Э�鴦����֤״̬������ɾ��!" );

            return FAIL;
        }
        
        
        else if (rowSet.size() > 0) {//����Э��δ��֤
        	Map row=(Map)rowSet.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//���ջ�������
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
            params.put("RemoveTeller", msg.getString("Teller"));//Ȫ�ݹ̶�Ϊ999999
            logger.info("��Ȩ��Ա�ţ�"+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
             
            try
            { 
	            //msg.set("PayAcct", payAcct);
	            msg.set("EnabledFlag", "N");
	            msg.set("verifyResult", "1");
	            //logger.info("--------�����˻�checkprovedelete----------"+payAcct);
	       //     String bankorgname = DBUtil.queryForString("select bankorgname from bankorginfo where netcode='"+BranchNo+"'");
	          //  msg.set("OrgName", bankorgname);
	            
	            msg.set("ReturnResult", "Y");
	            msg.set("AddWord", "Э��ɾ���ɹ�");
	            
	            DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',RemoveTeller=#RemoveTeller#,RemoveTime=#RemoveTime# where EnabledFlag='Y' and TaxPayCode='" + taxPayCode +  "' and protocolNo='" + ProtocolNo + "' ", params);
	            return SUCCESS;
            }
            catch (Exception e)
            {
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "��Э�鲻���ڻ����Ѿ���Ч");
               return FAIL;
            } 
        } 
        else if (rowSetchexiao.size() > 0) //����Э���ѳ���������ɾ��
        {	
        	Map row=(Map)rowSetchexiao.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + (String)row.get("TaxOrgCode") + "'");
            if(taxOrgName.equals("")||taxOrgName.equals(null))
            	
            	msg.set("TaxOrgName", "");	//���ջ�������
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
            params.put("RemoveTeller", msg.getString("Teller"));//Ȫ�ݹ̶�Ϊ999999
            logger.info("��Ȩ��Ա�ţ�"+msg.getString("Teller"));
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
            
            
            
            try
            {
            
            //msg.set("PayAcct", payAcct);
            msg.set("EnabledFlag", "N");
            msg.set("verifyResult", "1");
            //logger.info("--------�����˻�checkprovedelete----------"+payAcct);
       //     String bankorgname = DBUtil.queryForString("select bankorgname from bankorginfo where netcode='"+BranchNo+"'");
          //  msg.set("OrgName", bankorgname);
            
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "Э��ɾ���ɹ�");
            
            DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',RemoveTeller=#RemoveTeller#,RemoveTime=#RemoveTime# where EnabledFlag='Y' and TaxPayCode='" + taxPayCode +  "' and protocolNo='" + ProtocolNo + "' ", params);
            return SUCCESS;
            }
            catch (Exception e)
            {
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "��Э�鲻���ڻ����Ѿ���Ч");
               return FAIL;
            }
            
            
        }
        
        else { 
            
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "��Э�鲻���ڣ��Ѿ���Ч����ǩ��");
           return FAIL;

      }
    }
    private String getRandomTraNo() {
        String time = DateTimeUtil.getTimeByFormat("hhmmss");
        NumberFormat nf = new DecimalFormat("00");
        return time + nf.format(Math.random() * 100);
    }
    }