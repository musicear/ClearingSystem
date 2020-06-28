package resoft.tips.action;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Э���ѯ</p>
 * Author: zhuchangwu
 * Date: 2007-8-23
 * Time: 18:06:06
 */
public class QueryProveInfo implements Action {
	 public int execute(Message msg) throws Exception {
	        String TaxPayCode = msg.getString("TaxPayCode");
	        String ProtocolNo = msg.getString("ProtocolNo");
	        //String PayAcct = msg.getString("AcctNo");

	        //��ѯ�ʻ��Ƿ����QueryUtil.queryRowSet
	        String sql="select EnabledFlag,taxPayName,TaxPayCode,ProtocolNo,TaxOrgCode,PayOpBkCode," +
	        		"PayBkCode,PayAcct,HandOrgName,sendTime " +
	        		"from ProveInfo where TaxPayCode='" + TaxPayCode + "' and protocolNo='" + ProtocolNo + "'";
	        List proveinfos = QueryUtil.queryRowSet(sql);
	        if(proveinfos.size()>0){
	        	Map row = (Map) proveinfos.get(0);
	        	String EnabledFlag=(String)row.get("EnabledFlag");
	        	if("Y".equals(EnabledFlag)){
	                msg.set("TaxPayName", (String)row.get("taxPayName"));//��˰������
	                msg.set("PayAcctName", (String)row.get("HandOrgName"));//�ɿλ����
	                msg.set("TaxPayCode", (String)row.get("TaxPayCode"));//��˰�˱��
	                msg.set("ProtocolNo", (String)row.get("ProtocolNo"));//Э�����
	                msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));//���ջ��ش���
	                //msg.set("PayOpBkCode", (String)row.get("ReckBankNo"));//�������к�(�����·���)
	                msg.set("PayAcct", (String)row.get("PayAcct"));//�����ʻ�
	                msg.set("PayBkCode",(String)row.get("PayBkCode"));//�����������к�(�����·���)
	                //msg.set("BankOrgCode",(String)row.get("ReckBankNo"));//�������к�(���ض�Ӧ��)
	                //msg.set("ContactDate", (String)row.get("sendTime"));//�����ʻ�
	                String date = (String)row.get("sendTime");
	                //char[] as = date.toCharArray();
	                String newDate=date.substring(0, 4)+date.substring(5, 7)+date.substring(8, 10);
	             
	                msg.set("ContactDate",newDate);//�����ʻ�
//	                msg.set("PayBkCode",payBkCode);//д����
//	                msg.set("PayBkName",payBkName);//д����
	                String sql1="select TaxOrgName from TaxOrgMng where TaxOrgCode='"+(String)row.get("TaxOrgCode")+"'";
	                List proveinfos1 = QueryUtil.queryRowSet(sql1);
	                if(proveinfos1.size()>0){
	                Map row1 = (Map) proveinfos1.get(0);
	                msg.set("TaxOrgName",(String)row1.get("TaxOrgName"));
	                }else{
	                	msg.set("TaxOrgName","���ջ��ز�����!");
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
	                	msg.set("PayBkName","������������������!");
	                }
	                
	                String sql2="select bo.ReckBankNo from bankorginfo bo where bo.bankorgcode='"+(String)row.get("PayOpBkCode")+"'";
	                List proveinfos2 = QueryUtil.queryRowSet(sql2);
	                if(proveinfos2.size()>0){
	                Map row2 = (Map) proveinfos2.get(0);
	                msg.set("PayOpBkCode", (String)row2.get("ReckBankNo"));//�������к�(�����·���)
	                
	                String sql4="select bm.genBankName from BankMng bm where bm.reckBankNo='"+(String)row2.get("ReckBankNo")+"'";
	                List proveinfos4 = QueryUtil.queryRowSet(sql4);
	                if(proveinfos4.size()>0){
	                Map row4 = (Map) proveinfos4.get(0);
	                msg.set("PayOpBkName",(String)row4.get("GenBankName"));
	                }else{
	                	msg.set("PayOpBkName","��������������!");
	                }
	                }
	                //msg.set("TaxOrgName",(String)row.get("TaxOrgName"));
	                //msg.set("PayOpBkName",(String)row.get("GenBankName"));
	                msg.set("ReturnResult", "Y");
	                msg.set("AddWord", "��Э�����");
	                msg.set("ProveStatus", "Э����Ч");
	        	}else{
	                msg.set("ReturnResult", "N");
	                msg.set("AddWord", "��Э���ѱ�ɾ��");
	        	}
	        }else{
	            msg.set("ReturnResult", "N");
	            msg.set("AddWord", "��Э�鲻����");
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
