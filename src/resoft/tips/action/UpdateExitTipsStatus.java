package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>ÐÞ¸ÄÍË³öExitTipsÇëÇó×´Ì¬</p>
 * Author:liwei
 * Date: 2007-07-10
 * Time: 10:10:10
 */
public class UpdateExitTipsStatus implements Action {
    public int execute(Message msg) throws Exception {
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
        String logoutResult = returnMsg.getString("//CFX/MSG/LogoutInfo9009/LogoutResult");
        String addWord = returnMsg.getString("//CFX/MSG/LogoutInfo9009/AddWord");
        msg.set("LogoutResult",logoutResult);
        msg.set("AddWord",addWord);

        if(logoutResult.equals("90000")) {
            String sql = "update SysStatus set loginStatus='1'";
            DBUtil.executeUpdate(sql);
        }

        return SUCCESS;
    }
}

