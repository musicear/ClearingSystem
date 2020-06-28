package resoft.tips.action2;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置CorrelationId为"REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"。用于银行发起的报文</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 17:05:30
 */
public class SetCorrelationIdAsREQ implements Action {
    /**
     * @param msg Message
     * @return int  返回值
     * @throws RuntimeException
     */
    public int execute(Message msg) throws Exception {
        msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
        return SUCCESS;
    }
}
