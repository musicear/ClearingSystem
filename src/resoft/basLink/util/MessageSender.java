package resoft.basLink.util;

import resoft.xlink.core.Message;

/**
 * function: 消息发送接口
 * User: albert lee
 * Date: 2005-10-27
 * Time: 16:57:49
 */
public interface MessageSender {
    /**
     * 设置属性。如Socket通讯中，设置host,port;MQ通讯中，设置队列名
     * */
    public void setProperty(String name,String value);
    /**
     * 发送数据
     * */
    public Message send(Message msg) throws MessageSendException;
}
