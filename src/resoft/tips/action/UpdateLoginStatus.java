package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>ÐÞ¸ÄµÇÂ¼×´Ì¬</p>
 *
 * @Author: Date: 2007-5-16
 * Time: 15:10:17
 */
public class UpdateLoginStatus implements Action {

    public int execute(Message msg) throws Exception {
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
        String loginResult = returnMsg.getString("//CFX/MSG/LoginReturn9007/LoginResult");
        String sysStat = returnMsg.getString("//CFX/MSG/LoginReturn9007/SysStat");

        msg.set("LoginResult",loginResult);
        msg.set("AddWord",returnMsg.getString("//CFX/MSG/LoginReturn9007/AddWord"));
        msg.set("SysStat",sysStat);

        loginResult = loginResult.equals("90000") ? "0" : "1";            //0:µÇÂ¼³É¹¦;1:Î´µÇÂ¼
        String sql = "update SysStatus set loginStatus='" + loginResult + "',sysStatus='" + sysStat+"'";

        DBUtil.executeUpdate(sql);
        return SUCCESS;
    }
}
