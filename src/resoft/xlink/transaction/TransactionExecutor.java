package resoft.xlink.transaction;

import resoft.xlink.core.Message;

/**
 * <p>���׵�����</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:58:23
 */
public interface TransactionExecutor {
    public void execute(Transaction transaction,Message msg);
}
