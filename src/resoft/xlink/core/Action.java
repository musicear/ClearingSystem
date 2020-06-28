package resoft.xlink.core;

/**
 * <p>原子交易的基础类</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:41:58
 */
public interface Action {
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;

    /**
     * @param msg  Message
     * @return  int  返回值
     * @throws RuntimeException  @todo 是否要抛出Exception还是RuntimeException?
     * */
    int execute(Message msg) throws Exception;

    /** @link dependency */
    /*# Message lnkMessage; */
}
