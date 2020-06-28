package resoft.xlink.transaction;

import resoft.xlink.core.Message;

/**
 * <p>交易拦截器。在每交易执行前后执行</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:39:25
 */
public interface TransactionInterceptor {
    /**
     * 交易前执行
     * @return boolean 是否继续向下执行。true:继续；false:停止
     * */
    public boolean before(Message msg);
    /**
     * 交易后执行     
     * */
    public void after(Message msg);
}
