package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-30
 * Time: 23:11:58
 */
public class TestNewMessage extends TestCase{
    public void test() {
        Message msg = new Message();
        msg.setValue("CFX/MSG/流水号","123432432");
        assertEquals("123432432",msg.getValue("CFX/MSG/流水号"));
        
    }
}
