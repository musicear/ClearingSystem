package resoft.xlink.core;

/**
 * <p>ԭ�ӽ��׵Ļ�����</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:41:58
 */
public interface Action {
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;

    /**
     * @param msg  Message
     * @return  int  ����ֵ
     * @throws RuntimeException  @todo �Ƿ�Ҫ�׳�Exception����RuntimeException?
     * */
    int execute(Message msg) throws Exception;

    /** @link dependency */
    /*# Message lnkMessage; */
}
