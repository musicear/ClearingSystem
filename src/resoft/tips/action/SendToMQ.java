package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import resoft.basLink.Configuration;
import resoft.tips.mq.PacketWriter;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>发送信息至MQ</p>
 * User: liwei
 * Date: 2007-4-25
 * Time: 17:51:02
 * Update:2007-08-02 09:31:00
 */
public class SendToMQ implements Action { 
    private static final Log logger = LogFactory.getLog(SendToMQ.class);
    Configuration conf = Configuration.getInstance();
    public int execute(Message msg) throws RuntimeException {
        String packet = (String) msg.get("报文内容");
        
        String correlationId = (String) msg.get("correlationId");        
        logger.info("sendtomqmsg:" + packet);
        MQEnvironment.addConnectionPoolToken();
        try { 
        	if(qmName==null)
        		qmName=conf.getProperty("MQ", "QueueManager");
            MQQueueManager qMgr = new MQQueueManager(qmName);
            MQQueue outQueue = qMgr.accessQueue(queueName, CMQC.MQOO_OUTPUT);
            MQMessage outMessage = new MQMessage();
            if(correlationId!=null && !correlationId.equals("")) {
                outMessage.correlationId = correlationId.getBytes();
            }
            outMessage.messageId = ((String)msg.get("//CFX/HEAD/MsgID")).getBytes();
            logger.info("messageId:" + msg.get("//CFX/HEAD/MsgID"));
            outMessage.write(packet.getBytes("GBK"));
            outQueue.put(outMessage);
            outQueue.close(); 
            qMgr.disconnect();
            int pos=packet.indexOf((String)msg.getString("strEncData"));
            String msgStr=packet.substring(0, pos);
            String filePath = PacketWriter.writePacket(msgStr,"_1_"+(String)msg.get("//CFX/HEAD/MsgNo")+"_");
            //SaveRecvLogUtil.saveRecvLog((String)msg.get("//CFX/HEAD/MsgID"), (String)msg.get("//CFX/HEAD/WorkDate"), (String)msg.get("//CFX/HEAD/MsgRef"), (String)msg.get("//CFX/HEAD/MsgNo"),filePath,"1");
        } catch (Exception e) {
            logger.error("发送MQ消息失败。队列管理器:" + qmName + ";队列名称:" + queueName,e);            
            return FAIL;
        }
        logger.info("发送MQ消息成功。队列名称：" + queueName);
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

