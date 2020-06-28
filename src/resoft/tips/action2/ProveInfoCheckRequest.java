package resoft.tips.action2;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

public class ProveInfoCheckRequest implements Action {
	private static final Log logger = LogFactory.getLog(ProveInfoCheckRequest.class);
	
	public int execute(Message msg) throws Exception {
		List rowSet = QueryUtil.queryRowSet("select * from PayAcctProvePack where procStatus='0'");
		for(int index=0; index<rowSet.size(); index++) {
			Map row = (Map) rowSet.get(index);
			String sendOrgCode = (String) row.get("SendOrgCode");
			String payeeBankNo = (String) row.get("PayeeBankNo");
			String packNo = (String) row.get("packNo");
			String entrustDate = (String) row.get("entrustDate");
			String sql = "select * from PayAcctProveDetail ";
			String whereClause = "where SendOrgCode='" + sendOrgCode + "' and PayeeBankNo='" + payeeBankNo + "' " +
				 				 "and packNo='" + packNo + "' and entrustDate='" + entrustDate + "' ";
			DBUtil.executeUpdate("update PayAcctProvePack set procStatus='1' " + whereClause);
			List detailRowSet = QueryUtil.queryRowSet(sql + whereClause);
			logger.info(sql+whereClause);
			for(int i=0; i<detailRowSet.size(); i++) {
					Map detailRow = (Map) detailRowSet.get(i);
					Message newMsg = new DefaultMessage();
					newMsg.set("SendOrgCode", (String) detailRow.get("SendOrgCode"));
					newMsg.set("PayeeBankNo", (String) detailRow.get("PayeeBankNo"));
					newMsg.set("packNo", (String) detailRow.get("packNo"));
					newMsg.set("entrustDate", (String) detailRow.get("entrustDate"));
					newMsg.set("VcNo", (String) detailRow.get("VcNo"));
					newMsg.set("PayeeAcct", (String) detailRow.get("PayeeAcct"));
					newMsg.set("PayeeName", (String) detailRow.get("PayeeName"));
					newMsg.set("BdgOrgCode", (String) detailRow.get("BdgOrgCode"));
					newMsg.set("BudgetOrgName", (String) detailRow.get("BudgetOrgName"));
					newMsg.set("PaperType", (String) detailRow.get("PaperType"));
					newMsg.set("PaperNo", (String) detailRow.get("PaperNo"));
					newMsg.set("PayeeOpBkNo", (String) detailRow.get("PayeeOpBkNo"));
					newMsg.set("交易码","9118DoCheck");
					Controller controller = new Controller();
					controller.setNameOfTransCode("交易码");
		            controller.execute(newMsg);
			}
			List list = QueryUtil.queryRowSet("select * from PayAcctProveDetail " + whereClause + " and VCResult is null ");
			if(list.isEmpty()) {
				DBUtil.executeUpdate("update PayAcctProvePack set procStatus='2' " + whereClause);
			}
		}
	
		return SUCCESS;
	}

}
