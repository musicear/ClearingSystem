package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>修改自由发送报文信息状态</p>
 * Author:liwei
 * Date: 2007-07-17
 * Time: 13:13:00
 */
public class UpdateSentFreeMsg implements Action {
    public int execute(Message msg) throws Exception {

        String sql = "update SentFreeMessage set status='1',sendTime='" + DateTimeUtil.getDateTimeString() +
                "' where id=" + msg.getString("freeMsgId");
        DBUtil.executeUpdate(sql);
        return SUCCESS;
    }
}



