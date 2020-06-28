package resoft.xlink.comm;

import java.util.Map;

/**
 * <p>异步监听器接口，比如JMS、MQ等</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 22:47:44
 */
public interface AsyncListener {
    public int onMessage(byte[] bytes, Map properties);
}
