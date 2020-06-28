package resoft.basLink.mq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import resoft.xlink.comm.AsyncListener;

/**
 * <p>MQ监听器</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 23:01:15
 */
class MQListener implements Runnable {
    private static final Log logger = LogFactory.getLog(MQListener.class);

    MQListener(Map props) {
        this.serviceProps = props;
    }

    public void run() {
        while (true) {
            try {
                startListener();
            } catch (Exception e) {
                logger.error("MQ监听失败");
                //等待2分钟后重新连接
                try {
                    Thread.sleep(1000 * 60 * 2);
                } catch (InterruptedException e1) {
                }
            }
        }

    }

    private void startListener() throws MQException, IOException, InterruptedException {
        String qmName = (String) serviceProps.get("QueueManager");
        String queueName = (String) serviceProps.get("Queue");
        String charsetName = (String) serviceProps.get("charsetName");
        if (charsetName == null) {
            charsetName = "UTF-8";
        }
        MQQueueManager qMgr;
        try {
            qMgr = new MQQueueManager(qmName);
        } catch (MQException e) {
            logger.error("error connect to queue manager:" + qmName, e);
            throw e;
        }
        MQQueue queue;
        try {
            queue = qMgr.accessQueue(queueName, CMQC.MQOO_INPUT_AS_Q_DEF | CMQC.MQOO_INQUIRE);
        } catch (MQException e) {
            logger.error("error to access queue:" + queueName, e);
            throw e;
        }
        //noinspection InfiniteLoopStatement
        int maxPoolSize = 1;
        if(serviceProps.containsKey("maxPoolSize")) {
            maxPoolSize = Integer.parseInt((String)serviceProps.get("maxPoolSize"));
        }
        Executor executor = new PooledExecutor(maxPoolSize);
        logger.info("线程池大小：" + maxPoolSize);

        while (true) {
            MQMessage inMessage = new MQMessage();
            String correlationId = (String) serviceProps.get("correlationId");
            if(correlationId==null) {
                correlationId = "REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
                inMessage.correlationId = correlationId.getBytes();//为保持兼容性
            } else if(!correlationId.equals("")) {
                inMessage.correlationId = correlationId.getBytes();
            }

            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options = MQC.MQGMO_WAIT;
            gmo.waitInterval = MQC.MQWI_UNLIMITED;

            queue.get(inMessage, gmo);
            Map messageProps = new HashMap();
            messageProps.put("className", serviceProps.get("className"));
            messageProps.put("messageId", inMessage.messageId);
            messageProps.put("correlationId", inMessage.correlationId);
            //inMessage.messageSequenceNumber

            byte[] bytes = new byte[inMessage.getDataLength()];
            inMessage.readFully(bytes);
            String str = new String(bytes, charsetName);
            logger.info("接收消息为:" + str);

            executor.execute(new TextListenerThread(str, messageProps));
        }
    }

    private Map serviceProps;
}

class TextListenerThread implements Runnable {
    private static final Log logger = LogFactory.getLog(TextListenerThread.class);

    TextListenerThread(String text, Map messageProps) {
        this.text = text;
        this.messageProps = messageProps;
    }

    public void run() {
        String className = (String) messageProps.get("className");

        AsyncListener listener;
        try {
            listener = (AsyncListener) Class.forName(className).newInstance();
        } catch (Exception e) {
            logger.error("创建类" + className + "失败", e);
            return;
        }
        listener.onMessage(text.getBytes(), messageProps);
    }

    private String text;
    private Map messageProps;
}
