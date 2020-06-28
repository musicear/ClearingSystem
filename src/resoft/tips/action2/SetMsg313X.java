package resoft.tips.action2;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 构造3133，3134回执包
 * @author chenlujia
 *
 */
public class SetMsg313X implements Action {
	public static final String RETURN_UNSEND="0";
	public static final String RETURN_SENDING="1";
	public static final String RETURN_SENDED="2";
	private String payType;
	private String tableName;
	public int execute(Message msg) throws Exception {
		if (msgNo.equals("3133")) {
			payType="1";
			tableName="DirectPayQuota";
		} else if (msgNo.equals("3134")) {
			payType="2";
			tableName="AuthPayQuota";
		} else {
			return FAIL;
		}
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		List packList = getPackList();
		if (packList.size() == 0) {
			return FAIL;
		}
		Map packMap = (Map)(packList.get(0));
		String treCode = (String)(packMap.get("TreCode"));
		String entrustDate = (String)(packMap.get("entrustDate"));
		String packNo = (String)(packMap.get("packNo"));
		setReturnStatus(treCode, entrustDate, packNo, RETURN_SENDING);
		//	准备回执包
    	msg.set("//CFX/MSG/BatchHead"+ msgNo +"/TreCode", packMap.get("TreCode"));
    	msg.set("//CFX/MSG/BatchHead"+ msgNo +"/BillOrg", packMap.get("BillOrg"));
    	msg.set("//CFX/MSG/BatchHead"+ msgNo +"/OriEntrustDate", packMap.get("EntrustDate"));
    	msg.set("//CFX/MSG/BatchHead"+ msgNo +"/OriPackNo", packMap.get("PackNo"));
    	msg.set("//CFX/MSG/BatchHead"+ msgNo +"/PayoutVouType", packMap.get("PayoutVouType"));
		List quotaList = getQuotaList(treCode, entrustDate, packNo);
		for (int i = 0; i < quotaList.size(); i++) {
			Map quotaParams = (Map)(quotaList.get(i));
			// 准备回执包
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/VouNo", quotaParams.get("VouNo"));
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/VouDate",	quotaParams.get("VouDate"));
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/OriTraNo", quotaParams.get("TraNo"));
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/SumAmt",	quotaParams.get("SumAmt"));

			// TCBS日期无法获取，此处填当前系统日期
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/AcctDate", df.format(new Date()));
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/Result", "90000");
			msg.set("//CFX/MSG/BatchReturn"+ msgNo +"[" + (i+1) + "]/Description", "处理成功");
		}
		msg.set("packInfo", packMap);
		return SUCCESS;
	}

	private void setReturnStatus(String treCode, String entrustDate, String packNo, String sendStatus) {
		String sql = "UPDATE QuotaPack SET returnStatus='" + sendStatus + "' WHERE TreCode='" + treCode
				+ "' AND entrustDate='" + entrustDate + "' AND packNo='"
				+ packNo + "' AND payType='"+ payType + "'";
		DBUtil.executeUpdate(sql);
		
	}

	private List getQuotaList(String treCode, String entrustDate, String packNo) throws SQLException {
		String sql = "SELECT * FROM " + tableName + " WHERE TreCode='" + treCode
				+ "' AND entrustDate='" + entrustDate + "' AND packNo='"
				+ packNo + "'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	private List getPackList() throws SQLException {
		String sql = "select * from QuotaPack where returnStatus='" + RETURN_UNSEND +
				"' and payType='"+ payType + "'";
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}
	
	private String msgNo;
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	

}
