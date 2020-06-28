package resoft.tips.action2;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 构造9119回执包
 * @author chenlujia
 * Date: 2008-8-25
 *
 */
public class SetMsg9119 implements Action {
	public static final String RETURN_UNSEND="0";
	public static final String RETURN_SENDING="1";
	public static final String RETURN_SENDED="2";

	public int execute(Message msg) throws Exception {
		List packList = getPackList();
		if (packList.size() == 0) {
			return FAIL;
		}
		Map packMap = (Map)(packList.get(0));
		String sendOrgCode = (String)(packMap.get("SendOrgCode"));
		String entrustDate = (String)(packMap.get("entrustDate"));
		String packNo = (String)(packMap.get("packNo"));
		String payeeBankNo = (String)(packMap.get("PayeeBankNo"));
		setReturnStatus(sendOrgCode, entrustDate, packNo, payeeBankNo, RETURN_SENDING);
		//	准备回执包
    	msg.set("//CFX/MSG/BatchHead9119/OriSendOrgCode", sendOrgCode);
    	msg.set("//CFX/MSG/BatchHead9119/OriEntrustDate", entrustDate);
    	msg.set("//CFX/MSG/BatchHead9119/OriPackNo", packNo);
    	msg.set("//CFX/MSG/BatchHead9119/PayeeBankNo", payeeBankNo);
		List proveList = getProveList(sendOrgCode, entrustDate, packNo, payeeBankNo);
		msg.set("//CFX/MSG/BatchHead9119/AllNum", Integer.toString(proveList.size()));
		for (int i = 0; i < proveList.size(); i++) {
			Map proveParams = (Map)(proveList.get(i));
			// 准备回执包
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/OriVCNo", proveParams.get("VcNo"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PayeeAcct",proveParams.get("PayeeAcct"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PayeeName", proveParams.get("PayeeName"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/BdgOrgCode", proveParams.get("BdgOrgCode"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/BudgetOrgName", proveParams.get("BudgetOrgName"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PaperType", proveParams.get("PaperType"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PaperNo", proveParams.get("PaperNo"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PayeeOpBkNo", proveParams.get("PayeeOpBkNo"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/AddWord", proveParams.get("AddWord"));
			msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/VCResult", proveParams.get("VCResult"));
			if (proveParams.containsKey("BdgOrgCode") && proveParams.get("BdgOrgCode") != null && !"".equals(proveParams.get("BdgOrgCode"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/BdgOrgCode",	proveParams.get("BdgOrgCode"));	
			}
			if (proveParams.containsKey("BudgetOrgName") && proveParams.get("BudgetOrgName") != null && !"".equals(proveParams.get("BudgetOrgName"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/BudgetOrgName",	proveParams.get("BudgetOrgName"));	
			}
			if (proveParams.containsKey("PaperType") && proveParams.get("PaperType") != null && !"".equals(proveParams.get("PaperType"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PaperType",	proveParams.get("PaperType"));	
			}
			if (proveParams.containsKey("PaperNo") && proveParams.get("PaperNo") != null && !"".equals(proveParams.get("PaperNo"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PaperNo",	proveParams.get("PaperNo"));	
			}
			if (proveParams.containsKey("PayeeOpBkNo") && proveParams.get("PayeeOpBkNo") != null && !"".equals(proveParams.get("PayeeOpBkNo"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/PayeeOpBkNo",	proveParams.get("PayeeOpBkNo"));	
			}
			if (proveParams.containsKey("AddWord") && proveParams.get("AddWord") != null && !"".equals(proveParams.get("AddWord"))) {
				msg.set("//CFX/MSG/ProveReturn9119[" + (i+1) + "]/AddWord",	proveParams.get("AddWord"));	
			}

		}
		msg.set("packInfo", packMap);
		//setReturnStatus(sendOrgCode, entrustDate, packNo, payeeBankNo, RETURN_SENDED);
	
		return SUCCESS;
	}

	private void setReturnStatus(String sendOrgCode, String entrustDate, String packNo,String payeeBankNo, String sendStatus) {
		String sql = "UPDATE PayAcctProvePack SET sendReturn='" + sendStatus + "' WHERE SendOrgCode='" + sendOrgCode
				+ "' AND entrustDate='" + entrustDate + "' AND packNo='"
				+ packNo + "' AND PayeeBankNo='"+ payeeBankNo + "'";
		DBUtil.executeUpdate(sql);
		
	}

	private List getProveList(String treCode, String entrustDate, String packNo, String payeeBankNo) throws SQLException {
		String sql = "SELECT * FROM PayAcctProveDetail WHERE SendOrgCode='" + treCode
				+ "' AND entrustDate='" + entrustDate + "' AND packNo='"
				+ packNo + "' AND PayeeBankNo='" + payeeBankNo + "'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	private List getPackList() throws SQLException {
		String sql = "select * from PayAcctProvePack where sendReturn='" + RETURN_UNSEND +
				"' and procStatus='2'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}
	

}
