package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>付款清算行查询</p>
 * Author: liwei
 * Date: 2007-7-5
 * Time: 18:06:06
 */
public class QueryBankName implements Action {
	 public int execute(Message msg) throws Exception {
        String payBkCode=msg.getString("PayBkCode").trim(); 	        
        String payBkName = DBUtil.queryForString("select GenBankName from BankMng where EnabledFlag='Y' and ReckBankNo='" + payBkCode + "'");        
        msg.set("PayBkName",payBkName);	        
        return SUCCESS;
    }
}
