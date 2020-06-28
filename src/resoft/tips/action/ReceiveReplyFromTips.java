package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>得到Tips的响应</p>
 * User: Albert Li
 * Date: 2006-5-22
 * Time: 16:50:31
 */

public class ReceiveReplyFromTips implements Action {
    private static final Log logger = LogFactory.getLog(ReceiveReplyFromTips.class);
    public int execute(Message msg) throws Exception {
        String messageId = (String) msg.get("//CFX/HEAD/MsgID");
        String reply;
        try {
            MQQueueManager qMgr = new MQQueueManager(this.qmName);
            //waiting for reply
            MQQueue inQueue = qMgr.accessQueue(this.queueName, MQC.MQOO_INQUIRE | MQC.MQOO_INPUT_AS_Q_DEF);
            MQMessage inMessage = new MQMessage();
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.waitInterval = 60 * 1000;
            gmo.options = MQC.MQGMO_WAIT;
            inMessage.correlationId = messageId.getBytes();
            inMessage.messageId = MQC.MQMI_NONE;
            inQueue.get(inMessage, gmo);
            byte[] replyBytes = new byte[inMessage.getDataLength()];
            inMessage.readFully(replyBytes);
            reply = new String(replyBytes,"GBK");
            msg.set("packet",reply);
            inQueue.close();
        } catch(MQException e) {
            logger.error("接收MQ消息错误。队列管理器:" + qmName + ";队列名称:" + queueName,e);
            return FAIL;
        }
        logger.info("接收到TIPS响应:" + reply);

        return SUCCESS;
    }

    /**
     * 设置队列管理器
     * */
    public void setQueueManagerName(String qmName) {
    	this.qmName =qmName;
    }
    /**
     * 设置队列
     * */
    public void setQueueName(String queueName) {    	
    	this.queueName = queueName;
    }

    private String qmName,queueName;
}
