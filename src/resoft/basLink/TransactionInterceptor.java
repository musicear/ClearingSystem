package resoft.basLink;

/**
 * function: 交易拦截器。在每交易执行前后执行
 * User: albert lee
 * Date: 2005-11-1
 * Time: 9:37:29
 */
public interface TransactionInterceptor {
    /**
     * 交易前执行
     * @return boolean 是否继续向下执行。true:继续；false:停止
     * */
    public boolean before(Message msg);
    /**
     * 交易后执行
     * //@todo 将转换为外系统键值部分可放在此处。类似finally
     * */
    public void after(Message msg);
}
