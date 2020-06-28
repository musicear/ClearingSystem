package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>更新批量包,明细数据库状态</p>
 * Author: zhuchangwu
 * Date: 2007-9-12
 * Time: 23:02:58
 */
public class UsualuseDelResult implements Action {
	public int execute(final Message msg) throws Exception {
		String oriMsgNo = msg.getString("OriMsgNo");
		String returnPackNo = msg.getString("OriRequestNo");// ReturnPackNo
		String payBkCode = msg.getString("OriSendOrgCode");
		String entrustDate = msg.getString("OriEntrustDate");
		String workDate = msg.getString("WorkDate");
		if (oriMsgNo.equals("2102")) {
			String sql1 = "update BatchPackage set procFlag='5' "
					+ " where returnPackNo='" + returnPackNo
					+ "' and payBkCode='" + payBkCode + "' and entrustDate='"
					+ entrustDate + "'";
			DBUtil.executeUpdate(sql1);
			String sql2 = "update BatchPackDetail set chkDate='" + workDate
					+ "' where packNo=(select packNo from BatchPackage where returnPackNo='" + returnPackNo+"' and payBkCode='" + payBkCode + "' and entrustDate='" + entrustDate + "') and entrustDate='"
					+ entrustDate + "' ";
			DBUtil.executeUpdate(sql2);
		}
		return SUCCESS;
	}

}
