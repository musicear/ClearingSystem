package resoft.basLink.util;

import resoft.xlink.core.Message;

/**
 * function: ��Ϣ���ͽӿ�
 * User: albert lee
 * Date: 2005-10-27
 * Time: 16:57:49
 */
public interface MessageSender {
    /**
     * �������ԡ���SocketͨѶ�У�����host,port;MQͨѶ�У����ö�����
     * */
    public void setProperty(String name,String value);
    /**
     * ��������
     * */
    public Message send(Message msg) throws MessageSendException;
}
