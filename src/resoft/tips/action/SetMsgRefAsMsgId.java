package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����MsgRef��MsgId��ͬ</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 18:18:25
 */
public class SetMsgRefAsMsgId implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/HEAD/MsgRef",msg.getString("//CFX/HEAD/MsgID"));
        return SUCCESS;
    }
}
