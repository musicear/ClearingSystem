package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>…Ë÷√µ«¬ºø⁄¡Ó</p>
 *
 * @Author: Date: 2007-5-15
 * Time: 2:37:14
 */
public class SetPassword implements Action {
    public int execute(Message msg) throws Exception {
        String password = DBUtil.queryForString("select tipsPassword from SysStatus");
        msg.set(passwordNodePath, password);
        return SUCCESS;
    }

    public void setPasswordNodePath(String passwordNodePath) {
        this.passwordNodePath = passwordNodePath;
    }

    private String passwordNodePath;
}
