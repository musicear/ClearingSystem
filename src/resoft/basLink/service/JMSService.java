package resoft.basLink.service;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.Service;

/**
 * function: JMS����
 * User: albert lee
 * Date: 2005-11-7
 * Time: 17:09:47
 */
public class JMSService implements Service{
    private static Log logger = LogFactory.getLog(JMSService.class);
    public void start() throws Exception {
        //�õ�������
        String listenerClassName = Configuration.getInstance().getProperty("server","jmsListener");
        if(listenerClassName.equals("")) {
            logger.info("δ����JMS������,JMS����������ֹ");
            return;
        }
        JMSTextListener listener = null;
        try {
            listener = (JMSTextListener) Class.forName(listenerClassName).newInstance();
        } catch(Exception e) {
            logger.error("����JMS������" + listenerClassName + "ʧ��,JMS����������ֹ",e);
            return;
        }
        //��ʼ��Context
        Configuration conf = Configuration.getInstance();

        Properties prop = new Properties();
        String initialContextFactory = conf.getProperty("jmsService",Context.INITIAL_CONTEXT_FACTORY);
        logger.info("JMS INITIAL_CONTEXT_FACTORY:" + initialContextFactory);
        prop.put(Context.INITIAL_CONTEXT_FACTORY,initialContextFactory);
        String providerUrl = conf.getProperty("jmsService",Context.PROVIDER_URL);
        logger.info("JMS PROVIDER_URL:" + providerUrl);
        prop.put(Context.PROVIDER_URL,providerUrl);

        Context ctx = new InitialContext(prop);
        //��������
        String queueConnectionFactoryStr = conf.getProperty("jmsService","queueConnectionFactory");
        logger.info("jms����ConnectionFactory:" + queueConnectionFactoryStr);
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(queueConnectionFactoryStr);
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        QueueSession queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
        String queueStr = conf.getProperty("jmsService","queue");
        logger.info("jms����queue:" + queueStr);
        Queue queue = (Queue) ctx.lookup(queueStr);
        QueueReceiver queueReceiver = queueSession.createReceiver(queue);
        //����
        queueReceiver.setMessageListener(new TextMessageDispatcher(listener));
        logger.info("jms service is running");
        queueConnection.start();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
            }
        }
        //@JMS Server��ֹ�󣬴˷��񽫳����⡣��������˷���
    }
    /**
     * JMS�����࣬���ı���Ϣת����JMSTextListener
     * */
    private class TextMessageDispatcher implements MessageListener{
        TextMessageDispatcher(JMSTextListener listener) {
            this.listener = listener;
        }

        public void onMessage(Message message) {
            if(message instanceof TextMessage) {
                    try {
                        String msgText = ((TextMessage)message).getText();
                        listener.onMessage(msgText);
                    } catch (JMSException e) {
                        logger.error("������Ϣʧ��",e);
                    }
                }
        }
        private JMSTextListener listener;
    }

    public void stop() throws Exception {

    }

    public boolean isRunning() {
        return false;
    }
}
