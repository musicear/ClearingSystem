package junit.comm;

import java.util.HashMap;

import junit.framework.TestCase;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>²âÊÔÖ÷¿ØÖÐÐÄ</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 5:25:17
 */
public class TestController extends TestCase {
    Controller controller = null;

    public void setUp() {
        controller = new Controller();
    }

    public void testSuccess() {
        controller.setGlobalProperties(new HashMap() {
            {
                put("name", "Albert Li");
            }
        });
        controller.setNameOfTransCode("txCode");
        Message msg = new DefaultMessage();
        msg.set("txCode", "100");
        controller.execute(msg);
        assertEquals("Albert Li", msg.get("name"));
        assertEquals("28", msg.get("age"));
        assertEquals("000", msg.get("respCode"));
    }
    public void testGlobalProperties() {
        controller.setGlobalPropertiesReader("junit.core.mock.GlobalPropertiesReaderMock");
        controller.setNameOfTransCode("txCode");
        Message msg = new DefaultMessage();
        msg.set("txCode", "100");
        controller.execute(msg);
        assertEquals("Albert Li", msg.get("name"));
        assertEquals("28", msg.get("age"));
        assertEquals("000", msg.get("respCode"));
    }
}
