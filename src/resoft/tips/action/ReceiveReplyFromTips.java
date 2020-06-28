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
 * <p>�õ�Tips����Ӧ</p>
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
            logger.error("����MQ��Ϣ���󡣶��й�����:" + qmName + ";��������:" + queueName,e);
            return FAIL;
        }
        logger.info("���յ�TIPS��Ӧ:" + reply);

        return SUCCESS;
    }

    /**
     * ���ö��й�����
     * */
    public void setQueueManagerName(String qmName) {
    	this.qmName =qmName;
    }
    /**
     * ���ö���
     * */
    public void setQueueName(String queueName) {    	
    	this.queueName = queueName;
    }

    private String qmName,queueName;
}
