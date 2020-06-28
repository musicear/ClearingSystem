package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Activity;
import resoft.basLink.Message;
import resoft.basLink.TransactionFlow;

/**
 * function: ≤‚ ‘Ωª“◊–Ú¡–
 * User: albert lee
 * Date: 2005-9-26
 * Time: 10:02:33
 */
public class TestTransactionFlow extends TestCase{
    private TransactionFlow flow = null;
    public void setUp() {
        flow = new TransactionFlow();
        Activity a1 = new Activity("resoft.junit.testTransactionFlow.Module1");
        a1.addTransition("-1","end");
        flow.addActivity(a1);
        flow.addActivity("resoft.junit.testTransactionFlow.Module2");
        flow.addLabel("end");
        flow.addActivity("resoft.junit.testTransactionFlow.Module3");

        //flow.setStartState("resoft.junit.testTransactionFlow.Module1");

//        flow.addActivity(new ModuleBase() {
//
//            public int execute(Message msg) throws Exception {
//                msg.setValue("œÏ”¶¬Î","123");
//                return SUCCESS;
//            }
//        });
//        flow.addActivity(new ModuleBase() {
//
//            public int execute(Message msg) throws Exception {
//                msg.setValue("œÏ”¶√Ë ˆ","≤‚ ‘Flow");
//                return 0;
//            }
//        });
    }
    public void testSUCCESS() {
        Message msg = new Message();
        msg.setValue("go","true");
        flow.execute(msg);
        assertNotNull(msg);
        assertEquals("123",msg.getValue("œÏ”¶¬Î"));
        assertEquals("≤‚ ‘Flow",msg.getValue("œÏ”¶√Ë ˆ"));
        assertEquals("true",msg.getValue("flag"));
    }
    public void testFAIL() {
        Message msg = new Message();
        msg.setValue("go","false");
        flow.execute(msg);
        assertNotNull(msg);
        assertEquals("456",msg.getValue("œÏ”¶¬Î"));
        assertEquals("",msg.getValue("œÏ”¶√Ë ˆ"));
        assertEquals("true",msg.getValue("flag"));
    }
}
