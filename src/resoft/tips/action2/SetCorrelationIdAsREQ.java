package resoft.tips.action2;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����CorrelationIdΪ"REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"���������з���ı���</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 17:05:30
 */
public class SetCorrelationIdAsREQ implements Action {
    /**
     * @param msg Message
     * @return int  ����ֵ
     * @throws RuntimeException
     */
    public int execute(Message msg) throws Exception {
        msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
        return SUCCESS;
    }
}
