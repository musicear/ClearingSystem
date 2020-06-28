package junit.core.mock;

import resoft.xlink.core.Message;
import resoft.xlink.transaction.TransactionInterceptor;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-25
 * Time: 8:36:04
 */
public class DBTransactionInterceptor implements TransactionInterceptor {
    /**
     * 交易前执行
     *
     * @return boolean 是否继续向下执行。true:继续；false:停止
     */
    public boolean before(Message msg) {
        return true;  
    }

    /**
     * 交易后执行
     */
    public void after(Message msg) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
