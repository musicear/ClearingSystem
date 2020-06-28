package resoft.tips.action2;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.tips.util.GenerateTransNo;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 构造2200回执报文
 * @author chenlujia
 *
 */
public class SetMsg2200 implements Action {

	private final static String RETURN_UNSEND="0";
	private final static String RETURN_SENDING="1";
	private final static String RETURN_SENDED="2";
	public int execute(Message msg) throws Exception {
		List unReturnList = queryUnReturnList();
		if (unReturnList.size() == 0) {
			return FAIL;
		}
		Map payPack = (Map)(unReturnList.get(0));
		String packId=(String)(payPack.get("ID"));
		String finOrgCode = (String)(payPack.get("FinOrgCode"));
		String entrustDate = (String)(payPack.get("entrustDate"));
		String packNo = (String)(payPack.get("packNo"));
		String PayoutVouType=(String)(payPack.get("PayoutVouType"));
		String PayMode=(String)(payPack.get("PayMode"));
		String newEntrustDate = DateTimeUtil.getDateString();
		String newPackNo = GenerateTransNo.generate();
		msg.set("//CFX/MSG/BatchHead2200/AgentBnkCode", payPack.get("AgentBnkCode"));
		msg.set("//CFX/MSG/BatchHead2200/FinOrgCode", finOrgCode);
		msg.set("//CFX/MSG/BatchHead2200/TreCode", payPack.get("TreCode"));
		msg.set("//CFX/MSG/BatchHead2200/OriEntrustDate", entrustDate);
		msg.set("//CFX/MSG/BatchHead2200/OriPackNo", packNo);
		msg.set("//CFX/MSG/BatchHead2200/EntrustDate", newEntrustDate);
		msg.set("//CFX/MSG/BatchHead2200/PackNo", newPackNo);
		msg.set("//CFX/MSG/BatchHead2200/PayoutVouType", PayoutVouType);
		msg.set("//CFX/MSG/BatchHead2200/PayMode", PayMode);
		double packAmt = 0;
		setReturnStaus(packId, RETURN_SENDING);
		List payOrderList = queryPayOrders(packId);
		msg.set("//CFX/MSG/BatchHead2200/AllNum",  String.valueOf(payOrderList.size()));
		for (int i=0; i<payOrderList.size(); i++) {
			Map payParams = (Map)(payOrderList.get(i));
			String billId=(String)(payParams.get("ID"));
			String traNo = (String)(payParams.get("trano"));
			String payState = (String)(payParams.get("PayState"));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/VouNo", payParams.get("VouNo"));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/VouDate", payParams.get("VouDate"));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/OriTraNo", traNo);
			
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/PayState", payState);
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Description", payParams.get("Description"));
			int billAllNum=0, billSuccNum=0;
			double billAllAmt=0, billSuccAmt=0;
			List detailList = queryDetailList(billId);
			for (int j=0; j<detailList.size(); j++) {
				Map detailParams = (Map)(detailList.get(j));
				msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/SeqNo", detailParams.get("SeqNo"));
				msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/BdgOrgCode", detailParams.get("BdgOrgCode"));
				String ProcStatus=(String)(detailParams.get("ProcStatus"));
				double detailAmt = Double.parseDouble((String)(detailParams.get("Amt")));
				billAllNum ++;
				billAllAmt += detailAmt;
				if (ProcStatus.equals("2")) {
					msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/PayResult", "0");
					billSuccNum ++;
					billSuccAmt += detailAmt;
				} else {
					msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/PayResult", "1");
				}
				msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/AddWord", detailParams.get("AddWord"));
				msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/FuncSbtCode", detailParams.get("FuncSbtCode"));
				if ( (detailParams.get("EconSubjectCode") != null)
						&& (!"".equals(detailParams.get("EconSubjectCode")))) {
					msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/EcnomicSubjectCode", detailParams.get("EconSubjectCode"));
				}
				msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/Amt", Double.toString(detailAmt));
				if ((detailParams.get("AgentBnkPayDate") != null)
						&& (!"".equals(detailParams.get("AgentBnkPayDate")))) {
					msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/Detail2200[" + (j+1) + "]/AgentBnkPayDate", detailParams.get("AgentBnkPayDate"));
				}
			}
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/AllNum", String.valueOf(billAllNum));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/AllAmt", String.valueOf(billAllAmt));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/SuccNum", String.valueOf(billSuccNum));
			msg.set("//CFX/MSG/Bill2200[" + (i+1) + "]/SuccAmt", String.valueOf(billSuccAmt));
			packAmt += billAllAmt;
		}
		msg.set("//CFX/MSG/BatchHead2200/AllAmt", String.valueOf(packAmt));
		setReturnStaus(packId, RETURN_SENDED);
		setReturnInfo(newPackNo, newEntrustDate, payOrderList.size(), packAmt, packId);
		return 0;
	}

	private void setReturnInfo(String newPackNo, String newEntrustDate, int allNum, double amt,
			String packId) {
		String sql = "UPDATE PayOrderPack SET returnPackNo='" + newPackNo
				+ "', returnEntrustDate='" + newEntrustDate + "', returnTime='"
				+ DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "', returnNum=" + allNum + ", returnAmt=" + amt 
				+ " WHERE id="+packId;
		DBUtil.executeUpdate(sql);
	}

	private List queryDetailList(String billId) throws SQLException {
		String sql = "SELECT * FROM PayOrderDetail WHERE payid="+billId +" and procStatus='2'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	private void setReturnStaus(String packId, String returnStatus) {
		String sql = "UPDATE PayOrderPack SET returnStatus='" + returnStatus + "' WHERE id="+packId;
		DBUtil.executeUpdate(sql);
	}

	private List queryPayOrders(String packId) throws SQLException {
		String sql = "SELECT * FROM PayOrder WHERE packId="+packId;
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	private List queryUnReturnList() throws SQLException {
		String sql="SELECT * FROM PayOrderPack WHERE returnStatus='" + RETURN_UNSEND + "' AND ProcStatus='"+RETURN_SENDED+"'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

}
