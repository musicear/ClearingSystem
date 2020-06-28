package resoft.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

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
 * This example shows how to establish a connection
 * and send messages to the JMS queue. The classes in this
 * package operate on the same JMS queue. Run the classes together to
 * witness messages being sent and received, and to browse the queue
 * for messages. The class is used to send messages to the queue.
 *
 * @author Copyright (c) 1999-2003 by BEA Systems, Inc. All Rights Reserved.
 */
public class TestSendJMS {
// Defines the JNDI context factory.
    public final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";

// Defines the JNDI provider url.
    public final static String PROVIDER_URL = "t3://localhost:7001";

// Defines the JMS connection factory for the queue.
    public final static String JMS_FACTORY = "com.bea.wlpi.QueueConnectionFactory";

// Defines the queue.
    public final static String QUEUE = "ctaisQueue";


    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private Queue queue;
    private TextMessage msg;

    /**
     * Creates all the necessary objects for sending
     * messages to a JMS queue.
     *
     * @param ctx       JNDI initial context
     * @param queueName name of queue
     * @throws NamingException if operation cannot be performed
     * @throws JMSException    if JMS fails to initialize due to internal error
     */
    public void init(Context ctx, String queueName)
            throws NamingException, JMSException {
        qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
        qcon = qconFactory.createQueueConnection();
        qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) ctx.lookup(queueName);
        qsender = qsession.createSender(queue);
        msg = qsession.createTextMessage();
        qcon.start();
    }

    /**
     * Sends a message to a JMS queue.
     *
     * @param message message to be sent
     * @throws JMSException if JMS fails to send message due to internal error
     */
    public void send(String message) throws JMSException {
        msg.setText(message);
        qsender.send(msg);
    }

    /**
     * Closes JMS objects.
     *
     * @throws JMSException if JMS fails to close objects due to internal error
     */
    public void close() throws JMSException {
        qsender.close();
        qsession.close();
        qcon.close();
    }

    /**
     * main() method.
     *
     * @param args WebLogic Server URL
     * @throws Exception if operation fails
     */
    public static void main(String[] args) throws Exception {
        InitialContext ic = getInitialContext();
        TestSendJMS qs = new TestSendJMS();
        qs.init(ic, QUEUE);
        readAndSend(qs);
        qs.close();
    }

    private static void readAndSend(TestSendJMS qs)
            throws IOException, JMSException {
        BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        boolean quitNow = false;
        qs.send("Test.处理报文去吧");

    }

    private static InitialContext getInitialContext()
            throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        return new InitialContext(env);
    }

}
