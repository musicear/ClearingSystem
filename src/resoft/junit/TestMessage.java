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
        msg.setValue("������","1100");
        msg.setValue("��ˮ��","1231342432432");
    }
    public void testValues() {
        assertEquals("1100",msg.getValue("������"));
        assertEquals("",msg.getValue("NotFound"));
        assertEquals(2,msg.findAll().size());
    }
    public void testRemove() {
        msg.remove("������");
        assertEquals("",msg.getValue("������"));
    }
    public void testHeadValue() {
        assertEquals("1000",msg.getHeadValue("SRC"));
    }
    public void testSetOneExistsKey() {
        msg.setValue("��ˮ��","123");
        assertEquals("123",msg.getValue("��ˮ��"));
    }

    public void testSysProperty() {
        msg.setSysProperty("ip","127.0.0.1");
        assertEquals("127.0.0.1",msg.getSysProperty("ip"));
        assertEquals("",msg.getSysProperty("queue"));
    }

    public void testFindByPrefix() {
        msg.setValue("Ԥ���Ŀ1","abc");
        msg.setValue("��Ŀ����","def");
        msg.setValue("Ԥ���Ŀ2","010202");
        Collection result = msg.findByPrefix("Ԥ���Ŀ");
        assertEquals(2,result.size());
    }

    public void testForEach() {
        msg.setValue("Ԥ���Ŀ1","010201");
        //msg.setValue("Ԥ���Ŀ2","010201");
        final StringBuffer flag = new StringBuffer();
        msg.forEach("Ԥ���Ŀ",new MessageVariableIterator() {

            public void next(String prefix, int n) {
                assertEquals("Ԥ���Ŀ",prefix);
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
