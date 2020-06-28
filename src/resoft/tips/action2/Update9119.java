package resoft.tips.action2;



import java.util.Map;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


public class Update9119 implements Action {

	public int execute(Message msg) throws Exception {
		Map packMap=(Map) msg.get("packInfo");
		String sendOrgCode = (String)(packMap.get("SendOrgCode"));
		String entrustDate = (String)(packMap.get("entrustDate"));
		String packNo = (String)(packMap.get("packNo"));
		String payeeBankNo = (String)(packMap.get("PayeeBankNo"));
				
		String sql = "UPDATE PayAcctProvePack SET sendReturn='2' WHERE SendOrgCode='" + sendOrgCode
		+ "' AND entrustDate='" + entrustDate + "' AND packNo='"
		+ packNo + "' AND PayeeBankNo='"+ payeeBankNo + "'";
DBUtil.executeUpdate(sql);
		
		return SUCCESS;
	}

}
