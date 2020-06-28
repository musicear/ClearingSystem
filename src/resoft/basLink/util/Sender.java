package resoft.basLink.util;

/**
 * <p>function:发送接口。可实现发送Socket、JMS消息等</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-9</p>
 * <p>Time: 10:30:55</p>
 */
public interface Sender {
    /**
     * 设置属性
     * */
    public void setProperty(String name,String value) ;
    /**
     * 发送数据
     * */
    public byte[] send(byte[] b) throws Exception;
}
