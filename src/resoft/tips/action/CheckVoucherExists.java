package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>
 * 请求税票信息（T3100）
 * </p>
 * Author: chenjianping Date: 2007-7-20 Time: 15:19:58
 */
public class CheckVoucherExists implements Action {
	public int execute(Message msg) throws Exception {
		String payAcct = msg.getString("PayAcct").trim();
		String taxVouNo = msg.getString("TaxVouNo").trim();
		String taxPayCode = msg.getString("TaxPayCode").trim();
		List queryList1 = QueryUtil
				.queryRowSet("select RealTimePayment.TraNo,RealTimePayment.TaxOrgCode,RealTimePayment.EntrustDate"
						+ " from RealTimePayment ,ProveInfo"
						+ " where  ProveInfo.taxOrgCode=RealTimePayment.taxOrgCode "
						+ "and RealTimePayment.payAcct='" + payAcct + "' and RealTimePayment.taxVouNo='"
						+ taxVouNo + "' and ProveInfo.taxPayCode='" + taxPayCode
						+ "' and RealTimePayment.result='90000' and RealTimePayment.checkStatus ='2'");
		List queryList2 = QueryUtil
				.queryRowSet("select BatchPackDetail.TraNo,BatchPackDetail.TaxOrgCode,BatchPackDetail.EntrustDate,BatchPackDetail.PackNo"
						+ " from ProveInfo ,BatchPackDetail"
						+ " where  ProveInfo.taxOrgCode=BatchPackDetail.taxOrgCode "
						+ "and BatchPackDetail.payAcct='"
						+ payAcct
						+ "' and BatchPackDetail.taxVouNo='"
						+ taxVouNo
						+ "' and ProveInfo.taxPayCode='"
						+ taxPayCode
						+ "' and BatchPackDetail.result='90000' and BatchPackDetail.checkStatus ='2' ");
		
		if (queryList1.size() > 0) {
			msg.set("ReturnResult", "T");
			if (queryList1.size() == 1) {
				Map row = (Map) queryList1.get(0);
				msg.set("TraNo", row.get("TraNo"));
				msg.set("TaxOrgCode", row.get("TaxOrgCode"));
				msg.set("EntrustDate", row.get("EntrustDate"));
				msg.set("TableName", "RealTimePayment");
			}
		} else if (queryList2.size() > 0) {
			msg.set("ReturnResult", "T");
			if (queryList2.size() == 1) {
				Map row = (Map) queryList1.get(0);
				msg.set("TraNo", row.get("TraNo"));
				msg.set("TaxOrgCode", row.get("TaxOrgCode"));
				msg.set("EntrustDate", row.get("EntrustDate"));
				msg.set("PackNo", row.get("PackNo"));
				msg.set("TableName", "BatchPackDetail");

			}
		}

		else {
			msg.set("ReturnResult", "N");
		}
		return SUCCESS;
	}
}
