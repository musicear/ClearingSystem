package junit.transaction;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import resoft.xlink.impl.DefaultTransactionManager;
import resoft.xlink.transaction.Activity;
import resoft.xlink.transaction.Transaction;
import resoft.xlink.transaction.TransactionInterceptor;
import resoft.xlink.transaction.TransactionManager;

/**
 * <p>²âÊÔ½»Ò×¼ÓÔØÆ÷</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:47:24
 */
public class TestTransactionManager extends TestCase {
    private TransactionManager transactionManager;

    public void setUp() {
        transactionManager = new DefaultTransactionManager();
    }

    public void testGetTransaction() {
        transactionManager.setGlobalProperties(new HashMap() {
            {
                put("name", "Albert Li");
            }
        });
        Transaction transaction = transactionManager.getTransaction("100");
        assertEquals(2, transaction.findAllActivities().size());
        Activity act = (Activity) transaction.findAllActivities().get(0);
        assertEquals("junit.core.mock.MockAction", act.getName());
        assertEquals("junit.core.mock.ExceptionAction",transaction.getInternalErrorAction().getClass().getName());

        Map props = act.listAllProperties();
        assertEquals(2, props.size());
        String value = (String) props.get("name");
        assertEquals("Albert Li", value);
        assertEquals(1, transaction.findAllLabels().size());
        assertEquals(1, transaction.findAllTransactionInterceptors().size());
        assertEquals(1, transaction.findAllActionInterceptors().size());
        TransactionInterceptor transInterceptor = (TransactionInterceptor) transaction.findAllTransactionInterceptors().get(0);
        assertEquals("junit.core.mock.DBTransactionInterceptor", transInterceptor.getClass().getName());
    }
}
