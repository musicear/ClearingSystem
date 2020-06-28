package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.TransactionFlow;
import resoft.basLink.TransactionFlowManager;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-26
 * Time: 8:55:35
 */
public class TestTransactionManager extends TestCase{
    private TransactionFlowManager manager = TransactionFlowManager.getInstance();
    public void testCache() throws Exception {
        TransactionFlow one = manager.getTransactionFlow("1000");
        TransactionFlow two = manager.getTransactionFlow("1000");
        assertTrue(one==two);
    }
}
