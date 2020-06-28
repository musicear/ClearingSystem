package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>连接测试FROM TIPS</p>
 * Author: liwei
 * Date: 2007-07-12
 * Time: 10:10:10
 */
public class ConnTestFromTips implements Action {
	 public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/MsgReturn9120/OriMsgNo","9005");
        msg.set("//CFX/MSG/MsgReturn9120/Result","90000");
        msg.set("//CFX/MSG/MsgReturn9120/AddWord","连接成功");
        msg.set("//CFX/MSG/MsgReturn9120/Information","连接测试成功");
        return SUCCESS;
    }
}



