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
     * ����ǰִ��
     *
     * @return boolean �Ƿ��������ִ�С�true:������false:ֹͣ
     */
    public boolean before(Message msg) {
        return true;  
    }

    /**
     * ���׺�ִ��
     */
    public void after(Message msg) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
