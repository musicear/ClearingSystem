package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>¹ÊÕÏÍ¨Öª</p>
 * Author:liwei
 * Date: 2007-07-10
 * Time: 14:06:06
 */
public class UpdateTroubleInfo implements Action {
    public int execute(Message msg) throws Exception {
        String troubleReason=msg.getString("//CFX/MSG/TroubleInfo9102/TroubleReason");
        DBUtil.executeUpdate("update SysParams set paramCode='troubleReason',paramValue='"+troubleReason+"'");
        return SUCCESS;
    }
}

