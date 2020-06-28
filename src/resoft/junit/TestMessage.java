package resoft.junit;

import java.util.Collection;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.MessageVariableIterator;

/**
 * function:
 * User: albert lee
 * Date: 2005-8-23
 * Time: 11:16:31
 */
public class TestMessage extends TestCase{
    private Message msg = null;
    public void setUp() {
        msg = new Message();
        msg.setHeadValue("SRC","1000");
        msg.setValue("交易码","1100");
        msg.setValue("流水号","1231342432432");
    }
    public void testValues() {
        assertEquals("1100",msg.getValue("交易码"));
        assertEquals("",msg.getValue("NotFound"));
        assertEquals(2,msg.findAll().size());
    }
    public void testRemove() {
        msg.remove("交易码");
        assertEquals("",msg.getValue("交易码"));
    }
    public void testHeadValue() {
        assertEquals("1000",msg.getHeadValue("SRC"));
    }
    public void testSetOneExistsKey() {
        msg.setValue("流水号","123");
        assertEquals("123",msg.getValue("流水号"));
    }

    public void testSysProperty() {
        msg.setSysProperty("ip","127.0.0.1");
        assertEquals("127.0.0.1",msg.getSysProperty("ip"));
        assertEquals("",msg.getSysProperty("queue"));
    }

    public void testFindByPrefix() {
        msg.setValue("预算科目1","abc");
        msg.setValue("科目代码","def");
        msg.setValue("预算科目2","010202");
        Collection result = msg.findByPrefix("预算科目");
        assertEquals(2,result.size());
    }

    public void testForEach() {
        msg.setValue("预算科目1","010201");
        //msg.setValue("预算科目2","010201");
        final StringBuffer flag = new StringBuffer();
        msg.forEach("预算科目",new MessageVariableIterator() {

            public void next(String prefix, int n) {
                assertEquals("预算科目",prefix);
                assertEquals(1,n);
                flag.append("success");
            }

            public void end(int size) {
                assertEquals(1,size);
            }
        });
        assertEquals("success",flag.toString());
    }
    public void testClone() throws CloneNotSupportedException {
        Message msg = new Message();
        msg.setValue("abc","123");
        msg.setHeadValue("SRC","1000");
        msg.setSysProperty("ip","127.0.0.1");
        Message newMsg = (Message) msg.clone();
        assertEquals("123",newMsg.getValue("abc"));
        assertEquals("1000",newMsg.getHeadValue("SRC"));
        assertEquals("127.0.0.1",newMsg.getSysProperty("ip"));

    }

}
