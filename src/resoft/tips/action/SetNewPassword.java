package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>…Ë÷√µ«¬º–¬√‹¬Î</p>
 * Author: liguoyin
 * Date: 2007-8-20
 * Time: 23:10:56
 */
public class SetNewPassword implements Action {
    public int execute(Message msg) throws Exception {
    	
    	String password = DBUtil.queryForString("select tipsPassword from SysStatus");
        msg.set("//CFX/MSG/LoginInfo9006/NewPassword",password);
        return SUCCESS;
    }
}
