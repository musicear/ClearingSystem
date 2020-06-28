package resoft.basLink.service;

/**
 * function: JMS文本消息监听器
 * User: albert lee
 * Date: 2005-11-7
 * Time: 17:46:07
 */
public interface JMSTextListener {
    public void onMessage(String textMsg);
}
