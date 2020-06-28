package resoft.tips.action2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.IDUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class PrepareMsg2201 implements Action {
	private static final Log logger = LogFactory.getLog(Process3143.class);
	public int execute(Message msg) throws Exception {
		List unRemittedList = queryUnremitted();
		if (unRemittedList.size() == 0) {
			return FAIL;
		}
		//ÿ��ֻ�õ�һ��������������
		Map remittedMap = (Map) (unRemittedList.get(0));
		//Ҫ����֧��ָ�����ID
		long PayPackID=Long.parseLong(remittedMap.get("ID").toString());
		//���ɻ����������ID
		IDUtil util=IDUtil.getInstance();
		long RequestPackID=util.getMaxIDFromPayRequestPack();
		//�õ��������������ˮ�ţ���ϵͳ����	
		//String RequestPackNo = GenerateTransNo.generate();
	    DecimalFormat dt = new DecimalFormat("0.00");
		//�����ܱ������ܽ��
		double packAmt=0.00, billAmt=0.00;
		msg.set("//CFX/MSG/BatchHead2201/AgentBnkCode", remittedMap	.get("AgentBnkCode"));
		msg.set("//CFX/MSG/BatchHead2201/FinOrgCode", remittedMap.get("FinOrgCode"));
		msg.set("//CFX/MSG/BatchHead2201/TreCode", remittedMap.get("TreCode"));
		//ί�����ڲ������ݿ���ֱ��ȡ������ǰ��������,��ʱ��������ת������
		//remittedMap.put("EntrustDate", DateTimeUtil.getDateString());
		msg.set("//CFX/MSG/BatchHead2201/EntrustDate", remittedMap.get("EntrustDate"));
		//����ͨ�����ڲ���ת������PackNo����
		msg.set("//CFX/MSG/BatchHead2201/PackNo", remittedMap.get("PackNo"));
		msg.set("//CFX/MSG/BatchHead2201/PayoutVouType", remittedMap.get("PayoutVouType"));
		msg.set("//CFX/MSG/BatchHead2201/PayMode", remittedMap.get("PayMode"));
		
		//���ݰ�֧������������ˮ�ţ��õ�֧��ƾ֤��Ϣ
		List billList = queryBillList(remittedMap);
		logger.info("ID information is: "+remittedMap.get("ID").toString());
		//����˰���û��֧����ص���Ϣ�������֧������״̬Ϊ������ϣ����ָ��ʻ���Ϊ0
		if(billList.size()==0)
		{
			DBUtil.executeUpdate("update PayOrderPack set PROCSTATUS = 5 where ID="+PayPackID);
		}
		//List sqlList = new ArrayList();
		for (int i = 0; i < billList.size(); i++) {
			Map billMap = (Map) (billList.get(i));
			//֧��ƾ֤��Ϣ��ID���ҵ���Ӧ��ϸ
			long payID=Long.parseLong(billMap.get("ID").toString());
			//����������Ϣ���ID
			long RequestID=util.getMaxIDFromPayRequest();
			//����������ˮ�ţ���ϵͳ����	
			//String traNo = GenerateTransNo.generate();
			
			String traNo_Fin = (String) billMap.get("TraNo");
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/TraNo", billMap.get("TraNo"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/VouNo", billMap.get("VouNo"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/VouDate", billMap.get("VouDate"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayerAcct", billMap.get("PayerAcct"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayerName", billMap.get("PayerName"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayerAddr", billMap.get("PayerAddr"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayeeAcct", billMap.get("PayeeAcct"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayeeName", billMap.get("PayeeName"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayeeAddr", billMap.get("PayeeAddr"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/PayeeOpnBnkNo", billMap.get("PayeeOpnBnkNo"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/AddWord", billMap.get("AddWord"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/BudgetType", billMap.get("BudgetType"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/TrimSign", billMap.get("TrimSign"));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/OfYear", billMap.get("OfYear"));
			List detailList = queryDetail(payID);
			billAmt=0.00;
			for (int j=0; j<detailList.size(); j++) {
				Map detailMap = (Map)(detailList.get(j));
				msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/SeqNo", detailMap.get("SeqNo"));
				msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/BdgOrgCode", detailMap.get("BdgOrgCode"));
				msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/FuncSbtCode", detailMap.get("FuncSbtCode"));
				if (detailMap.containsKey("EconSubjectCode")
						&& (detailMap.get("EconSubjectCode") != null)
						&& (!"".equals(detailMap.get("EconSubjectCode")))) {
					msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/EcnomicSubjectCode", detailMap.get("EconSubjectCode"));
				}
				msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/AcctProp", "1");
				logger.info("detailMap.get(amt).toString() :"+ detailMap.get("Amt").toString());
				
				
				BigDecimal amt=new BigDecimal(Double.parseDouble(detailMap.get("Amt").toString()));
				amt= amt.setScale(2, BigDecimal.ROUND_HALF_UP);
				logger.info("amt :"+ amt);
				msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Detail2201[" + (j+1) + "]/Amt", dt.format(amt.doubleValue()));
				billAmt = add(billAmt,amt.doubleValue());
				
				logger.info("billAmt is :" + dt.format(billAmt));
			}
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/Amt", dt.format(billAmt));
			msg.set("//CFX/MSG/Bill2201[" + (i+1) + "]/StatInfNum", billMap.get("StatInfNum"));
			
			//����PayRequest�������Ϣ,���ͻ�������ǰ
			String insertPay="INSERT INTO PayRequest (ID, REQUESTPACKID, payID, TraNo, TraNo_Fin, RequestAmt,StatInfNum) values ("+
			RequestID+", "+RequestPackID+", "+payID+", '"+traNo_Fin+"', '"+traNo_Fin+"', "+billAmt+", "+detailList.size()+")";
			//����PayOrder��״̬,������״̬��Ϊ3�����벦���У�
			String updateSql="UPDATE PayOrder SET PROCSTATUS=3 where ID ="+payID;
			DBUtil.executeUpdate(insertPay);	
			DBUtil.executeUpdate(updateSql);
			//sqlList.add(insertPay);
			//sqlList.add(updateSql);
			//���İ����ܽ��
			packAmt = add(packAmt,billAmt);
			logger.info("packAmt is :" + dt.format(packAmt));
		}//endfor billList
		msg.set("//CFX/MSG/BatchHead2201/AllAmt", dt.format(packAmt));
		msg.set("//CFX/MSG/BatchHead2201/AllNum", Integer.toString(billList.size()));	
		Map packMap = new HashMap();
		packMap.put("FinOrgCode", remittedMap.get("FinOrgCode"));
		packMap.put("AgentBnkCode", remittedMap	.get("AgentBnkCode"));
		packMap.put("TreCode", remittedMap.get("TreCode"));
		packMap.put("EntrustDate", remittedMap.get("EntrustDate"));
		packMap.put("PackNo", remittedMap.get("PackNo"));
		packMap.put("AllAmt", dt.format(packAmt));
		packMap.put("AllNum", new Integer(billList.size()));
		packMap.put("PayoutVouType", remittedMap.get("PayoutVouType"));
		packMap.put("PayMode", remittedMap.get("PayMode"));
		
		
		
		if(packAmt==0)
		{//˵������֧��ָ��ȫ��ʧ�ܣ�����°���״̬Ϊ�������,���µ�����֧��ָ��Ϊ�������
			DBUtil.executeUpdate("update PayOrderPack set PROCSTATUS = 5 where ID="+PayPackID);
			DBUtil.executeUpdate("update PayOrder set PROCSTATUS = 5 where PackID="+PayPackID);
			return FAIL;
		}
		
		String updatePackSql = "INSERT INTO PayRequestPack (ID, PAYPACKID, FinOrgCode, AgentBnkCode, TreCode, PackNo, " +
				"entrustDate, REQUESTPACKNO, AllNum, AllAmt, PayoutVouType, PayMode, RemitStatus)" +
				" VALUES("+RequestPackID+", "+PayPackID+", '"+ packMap.get("FinOrgCode") + "', '" + packMap.get("AgentBnkCode") + "', '" + 
				packMap.get("TreCode") + "', '" + packMap.get("PackNo") + "', '" +packMap.get("EntrustDate") + "', '" + packMap.get("PackNo") + "', '"
				 + packMap.get("AllNum") + "', '" + packMap.get("AllAmt") + "', '" + packMap.get("PayoutVouType") + "', '"
				 + packMap.get("PayMode") + "', '1')";	
		DBUtil.executeUpdate(updatePackSql);
		DBUtil.executeUpdate("update PayOrderPack set PROCSTATUS = 3 where ID="+PayPackID);
		//update(sqlList);
		return SUCCESS;
	}


	private void update(List sqlList) throws SQLException {
		String[] sqls= new String[sqlList.size()];
		for (int i=0;i<sqlList.size();i++) {
			sqls[i]=(String)(sqlList.get(i));
		}
		DBUtil.executeUpdate(sqls);
	}


	private List queryDetail(long payID) throws SQLException {
		//�ҵ���Ӧ֧���ɹ�����ϸ
		String sql = "SELECT * FROM PayOrderDetail WHERE payID = "+payID+" and PROCSTATUS = '2'" ;
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	private List queryBillList(Map remittedMap) throws SQLException {
		long PayPackID=Long.parseLong(remittedMap.get("ID").toString());
		String sql="SELECT A.* FROM PayOrder A WHERE A.PackID ="+PayPackID+
			      " and A.PROCSTATUS='2'";
		List result = QueryUtil.queryRowSet(sql);
		/*�޸���� һ��֧��ָ�����Ӧһ�����������
		if (result.size() > 1000) {
			result = result.subList(0,1000);
		}
		*/
		return result;
	}

	private List queryUnremitted() throws SQLException {
		//��֧��ָ����Ĵ���״̬Ϊ�������
		String sql="SELECT B.ID, B.FinOrgCode, B.AgentBnkCode, B.TreCode,B.entrustDate, B.PayoutVouType, B.PayMode, B.PackNo "+
		"FROM PayOrderPack B WHERE B.PROCSTATUS='2'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}
	public static double add(double v1, double v2) {  
		  BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		  BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		  
		  return b1.add(b2).doubleValue();  
		}  

}
