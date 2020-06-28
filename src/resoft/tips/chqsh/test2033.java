package resoft.tips.chqsh;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.AbstractSyncAction;
//import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;

public class test2033 extends AbstractSyncAction {

	private static final Log logger = LogFactory.getLog(test2033.class);
	private static final String TL_TD = "880";

	public int execute(Message msg) throws Exception {
		test(msg);
		return SUCCESS;
	}

	public void test(Message msg) throws Exception {
		String JRN_NO = ""; // 交易日志号
		String VCH_NO = ""; // 传票号
		String TR_CODE = ""; // 交易码
		String rcvMsg = "";
		String result = "";
		String revokeStatus = "";
		String PAYACCT = "";
		double j = 0;
		double k = 0;
		SendMsgToBankSystem send = new SendMsgToBankSystem();
		List rowSet = QueryUtil.queryRowSet("select * from test where result='9004' and flag='Y' ");
		logger.info("查询到的结果："+rowSet.size());
		synchronized (TuxedoPackager.getTuxedoPackager()) {
			for (int i = 0; i < rowSet.size()-1; i++) {
				j = System.currentTimeMillis();
				Map row = (Map) rowSet.get(i+1);
				PAYACCT = (String) row.get("payAcct");
				JRN_NO = (String) row.get("JRN_NO");
				VCH_NO = (String) row.get("VCH_NO");
				TR_CODE = (String) row.get("TR_CODE");
				rcvMsg = send.sendOutMsg(JRN_NO, VCH_NO, TR_CODE,TL_TD + (1 + System.currentTimeMillis()%2));
				result = rcvMsg.substring(81, 85);
				if (result.equals("9004")) {
					revokeStatus = "Y";
				} else {
					revokeStatus = "N";
				}
				DBUtil.executeUpdate("update test set revokeStatus='" + revokeStatus
						+ "' where payacct='" + PAYACCT + "' and  JRN_NO='"
						+ JRN_NO + "'");
				k = System.currentTimeMillis();
				logger.info("接收到的字段为" + JRN_NO + "|" + VCH_NO + "|" + TR_CODE
						+ "|" + (k - j));
			}
		}
		// return SUCCESS;
	}

}
