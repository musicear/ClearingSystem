package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置CorrelationId为请求报文的MessageId。用于接收TIPS报文后返回回执时用</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 17:05:30
 */
public class SetCorrelationIdAsMessageId implements Action {
    /**
     * @param msg Message
     * @return int  返回值
     * @throws RuntimeException
     */
	private static final Log logger = LogFactory.getLog(PrepareForSendMQ.class);
    public int execute(Message msg) throws Exception {
        msg.set("correlationId",msg.get("messageId"));
        logger.info("messageId = [" + msg.get("messageId") + "]");
        return SUCCESS;
    }
}
