package resoft.basLink.util;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>function:实现JMS消息发送</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-9</p>
 * <p>Time: 10:50:11</p>
 */
public class JmsSender implements Sender{
    public static final String Property_QueueFactoryName = "queueFactoryName";
    public static final String Property_QueueName = "queueName";
    public void setProperty(String name, String value) {
        if(name.equals(Property_QueueFactoryName)) {
            queueFactoryName = value;
        } else if(name.equals(Property_QueueName)) {
            queueName = value;
        } else {
            props.put(name,value);
        }
    }

    public byte[] send(byte[] b) throws NamingException,JMSException {
        Context ctx = new InitialContext(props);
        //建立JMS连接
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(queueFactoryName);
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        QueueSession queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup(queueName);
        QueueSender sender = queueSession.createSender(queue);
        TextMessage message = queueSession.createTextMessage();
        message.setText(new String(b));
        sender.send(message);
        queueConnection.close();
        return new byte[0];
    }
    private String queueFactoryName,queueName;
    private Properties props = new Properties();
}
