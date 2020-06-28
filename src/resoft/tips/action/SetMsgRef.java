package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置MsgRef与MsgId相同</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 18:18:25
 */
public class SetMsgRef implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/HEAD/MsgRef",msg.getString("msgRef"));
        return SUCCESS;
    }
}