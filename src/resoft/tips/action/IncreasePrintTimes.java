package resoft.tips.action;

import java.util.List;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>
 * ¥Ú”°»∑»œ£®T3101£©
 * </p>
 * Author: chenjianping Date: 2007-7-20 Time: 15:19:58
 */
public class IncreasePrintTimes implements Action {

	public int execute(Message msg) throws Exception {

		String traNo = msg.getString("TraNo").trim();
		String taxOrgCode = msg.getString("TaxOrgCode").trim();
		String entrustDate = msg.getString("EntrustDate").trim();
		String packNo = msg.getString("PackNo").trim();
		String payAcct = msg.getString("PayAcct").trim();

		List queryList1 = QueryUtil
				.queryRowSet("select printTimes  from RealTimePayment  where  PayAcct='"
						+ payAcct
						+ "' and TraNo='"
						+ traNo
						+ "' and TaxOrgCode='"
						+ taxOrgCode
						+ "' and EntrustDate='" + entrustDate + "'");
		String str = "select printTimes  from BatchPackDetail "
				+ " where   PayAcct='" + payAcct + "' and TraNo='" + traNo
				+ "' and TaxOrgCode='" + taxOrgCode + "'";
		if (packNo != null)
			str += " and PackNo='" + packNo + "'";
		str += " and EntrustDate='" + entrustDate + "'";
		List queryList2 = QueryUtil.queryRowSet(str);
		if (queryList1.size() > 0) {

			DBUtil
					.executeUpdate("update RealTimePayment set printTimes=printTimes+1 where  PayAcct='"
							+ payAcct
							+ "' and TraNo='"
							+ traNo
							+ "' and TaxOrgCode='"
							+ taxOrgCode
							+ "' and EntrustDate='" + entrustDate + "'");
			msg.set("ReturnResult", "T");

		} else if (queryList2.size() > 0) {

			DBUtil
					.executeUpdate("update BatchPackDetail set printTimes=printTimes+1 where  PayAcct='"
							+ payAcct
							+ "' and PackNo='"
							+ packNo
							+ "' and TraNo='"
							+ traNo
							+ "' and TaxOrgCode='"
							+ taxOrgCode
							+ "' and EntrustDate='"
							+ entrustDate
							+ "'");
			msg.set("ReturnResult", "T");
		} else {
			msg.set("ReturnResult", "N");
		}

		return SUCCESS;

	}
}
