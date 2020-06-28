package resoft.xlink.comm;

/**
 * <p>根据各种策略进行不同的解析处理。</p>
 * <p>比如根据不同的客户端IP来选择不同的解析器，确定后的Protocol存放于ThreadLocal</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 0:03:39
 */
public interface Dispatcher {
    /**
     * 进行处理，将设定之Protocol存储在ThreadLocal
     * */
    public void process();
}
