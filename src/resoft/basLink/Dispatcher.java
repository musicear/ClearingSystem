package resoft.basLink;

/**
 * function: 根据各种策略进行不同的解析处理。确定的Protocal存放于ThreadLocal
 * User: albert lee
 * Date: 2005-9-20
 * Time: 15:02:13
 */
public interface Dispatcher {
    /**
     * 进行处理，将设定之Protocol存储在ThreadLocal
     * */
    public void process();
}
