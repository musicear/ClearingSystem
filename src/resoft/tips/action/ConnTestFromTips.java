package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���Ӳ���FROM TIPS</p>
 * Author: liwei
 * Date: 2007-07-12
 * Time: 10:10:10
 */
public class ConnTestFromTips implements Action {
	 public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/MsgReturn9120/OriMsgNo","9005");
        msg.set("//CFX/MSG/MsgReturn9120/Result","90000");
        msg.set("//CFX/MSG/MsgReturn9120/AddWord","���ӳɹ�");
        msg.set("//CFX/MSG/MsgReturn9120/Information","���Ӳ��Գɹ�");
        return SUCCESS;
    }
}



