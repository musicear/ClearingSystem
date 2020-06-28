package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;
import org.zerone.util.StringUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>
 * 请求税票信息（T3100）
 * </p>
 * Author: chenjianping Date: 2007-7-20 Time: 15:19:58
 */
public class QueryVoucherInfo implements Action {
	public int execute(Message msg) throws Exception {
		String traNo = msg.getString("TraNo");
		String taxOrgCode = msg.getString("TaxOrgCode");
		String entrustDate = msg.getString("EntrustDate");
		String packNo = msg.getString("PackNo");
		String tableName = msg.getString("TableName");
		List queryList = null;
		if (tableName != null && tableName.equals("RealTimePayment")) {
			queryList = QueryUtil
					.queryRowSet("select RealTimePayment.TaxPayName,ProveInfo.TaxPayCode,RealTimePayment.HandOrgName,RealTimePayment.PayAcct,RealTimePayment.TaxOrgCode,TaxOrgMng.TaxOrgName"
							+ ",RealTimePayment.TraAmt,RealTimePayment.TraNo,RealTimePayment.TaxVouNo,RealTimePayment.PrintTimes,VoucherTaxType.TaxTypeName,VoucherTaxType.TaxStartDate,VoucherTaxType.TaxEndDate,VoucherTaxType.TaxTypeAmt "
							+ " from RealTimePayment,ProveInfo,TaxOrgMng,VoucherTaxType "
							+ " where  RealTimePayment.traNo=VoucherTaxType.traNo and ProveInfo.taxOrgCode=RealTimePayment.taxOrgCode and TaxOrgMng.taxOrgCode=RealTimePayment.taxOrgCode"
							+ " and RealTimePayment.TraNo='"
							+ traNo
							+ "' and RealTimePayment.TaxOrgCode='"
							+ taxOrgCode
							+ "' and RealTimePayment.EntrustDate='"
							+ entrustDate
							+ "' and RealTimePayment.result='90000' and RealTimePayment.checkStatus ='2' and VoucherTaxType.bizCode='1' ");
		} else if (tableName != null && tableName.equals("BatchPackDetail")) {
			queryList = QueryUtil
					.queryRowSet("select BatchPackDetail.TaxPayName,ProveInfo.TaxPayCode,BatchPackDetail.HandOrgName,BatchPackDetail.PayAcct,BatchPackDetail.TaxOrgCode,BatchPackDetail.PayeeName,TaxOrgMng.TaxOrgName"
							+ ",BatchPackDetail.TraAmt,BatchPackDetail.TraNo,BatchPackDetail.TaxVouNo,BatchPackDetail.PrintTimes,VoucherTaxType.TaxTypeName,VoucherTaxType.TaxStartDate,VoucherTaxType.TaxEndDate,VoucherTaxType.TaxTypeAmt "
							+ " from BatchPackDetail ,ProveInfo,TaxOrgMng,VoucherTaxType"
							+ " where  BatchPackDetail.traNo=VoucherTaxType.traNo and ProveInfo.taxOrgCode=BatchPackDetail.taxOrgCode and BatchPackDetail.taxOrgCode=TaxOrgMng.taxOrgCode"
							+ " and BatchPackDetail.TraNo='"
							+ traNo
							+ "' and BatchPackDetail.TaxOrgCode='"
							+ taxOrgCode
							+ "' and BatchPackDetail.EntrustDate='"
							+ entrustDate
							+ "' and BatchPackDetail.PackNo='"
							+ packNo
							+ "' and BatchPackDetail.result='90000' and BatchPackDetail.checkStatus ='2' and VoucherTaxType.bizCode='2'");
		}
		if (queryList != null && queryList.size() > 0) {
			System.out.println("********");
			int TaxTypeNum = queryList.size();
			int i = 0;
			Map row = (Map) queryList.get(0);
			msg.set("TaxPayName", row.get("TaxPayName"));
			msg.set("TaxPayCode", row.get("TaxPayCode"));
			msg.set("HandOrgName", row.get("HandOrgName"));
			msg.set("PayAcct", row.get("PayAcct"));
			msg.set("TaxOrgCode", row.get("TaxOrgCode"));
			msg.set("TaxOrgName", row.get("TaxOrgName"));
			if (tableName != null && tableName.equals("BatchPackDetail"))
				msg.set("PayeeName", row.get("PayeeName"));
			msg.set("TraAmt", row.get("TraAmt"));
			String TraAmtChinesestr = row.get("TraAmt").toString();
			double TraAmtChinesedouble = Double.valueOf(TraAmtChinesestr)
					.doubleValue();
			msg.set("TraAmtChinese", StringUtil
					.convertNumber2Chinese(TraAmtChinesedouble));
			msg.set("TraNo", row.get("TraNo"));
			msg.set("TaxVouNo", row.get("TaxVouNo"));
			msg.set("PrintTimes", row.get("PrintTimes"));
			msg.set("TaxTypeNum", Integer.toString(TaxTypeNum));
			while (i < TaxTypeNum) {
				i++;
				msg.set("TaxTypeName" + i, row.get("TaxTypeName"));
				msg.set("TaxStartDate" + i, row.get("TaxStartDate"));
				msg.set("TaxEndDate" + i, row.get("TaxEndDate"));
				msg.set("TaxTypeAmt" + i, row.get("TaxTypeAmt"));

			}

		}

		return SUCCESS;
	}
}
