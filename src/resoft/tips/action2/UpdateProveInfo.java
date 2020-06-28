package resoft.tips.action2;


import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class UpdateProveInfo implements Action {

	public int execute(Message message) throws Exception {
		String sendOrgCode = (String) message.get("SendOrgCode");
		String payeeBankNo = (String) message.get("PayeeBankNo");
		String packNo = (String) message.get("packNo");
		String entrustDate = (String) message.get("entrustDate");
		String vcNo = (String) message.get("VcNo");
		String result = (String) message.get("Result");
		String addWord = (String) message.get("AddWord");
		
		String whereClause = "where SendOrgCode='" + sendOrgCode + "' and PayeeBankNo='" + payeeBankNo + "' " +
		 					 "and packNo='" + packNo + "' and entrustDate='" + entrustDate + "' ";
		// 更新支出信息验证明细表
		DBUtil.executeUpdate("update PayAcctProveDetail set VCResult='" + result + "', AddWord='" + 
							 addWord + "' " + whereClause + " and VcNo='" + vcNo + "'");
		return SUCCESS;
	}

}
