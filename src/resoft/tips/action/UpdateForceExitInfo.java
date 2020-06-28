package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>强制退出通知</p>
 * Author:liwei
 * Date: 2007-07-10
 * Time: 14:06:06
 */
public class UpdateForceExitInfo implements Action {
    public int execute(Message msg) throws Exception {
        String forceQuitReason=msg.getString("//CFX/MSG/ForceLogout9103/ForceQuitReason");
        DBUtil.executeUpdate("update SysParams set paramCode='ForceQuitReason',paramValue='"+forceQuitReason+"'");
        return SUCCESS;
    }
}

