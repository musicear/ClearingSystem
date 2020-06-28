package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置"包明细重发请求"信息</p>
 * Author: zhuchangwu
 * Date: 2007-10-18
 * Time: 22:37:30
 */
public class SetGetMsg9111Info implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/GetMsg9111/OriPackMsgNo",msg.getString("OriPackMsgNo"));
        msg.set("//CFX/MSG/GetMsg9111/OriSendOrgCode",msg.getString("OriSendOrgCode"));
        msg.set("//CFX/MSG/GetMsg9111/OriEntrustDate",msg.getString("OriEntrustDate"));
        msg.set("//CFX/MSG/GetMsg9111/OriPackNo",msg.getString("OriPackNo"));
        return SUCCESS;
    }
}
