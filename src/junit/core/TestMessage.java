package junit.core;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>≤‚ ‘Message</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:09:22
 */
public class TestMessage extends TestCase {
    private Message msg = null;

    public void setUp() {
        msg = new DefaultMessage();
    }

    public void test() {
        msg.set("id", "123");
        msg.set("abc", "def");
        msg.set("ia", "321");
        msg.set("ie", "2981");
        assertEquals("123", msg.get("default", "id"));
        Collection keys = msg.findAllKeysByCategory(Message.DefaultCategory);
        assertEquals(1, msg.findAllCategories().size());
        assertEquals(4, keys.size());
        int i = 0;
        for (Iterator itr = keys.iterator(); itr.hasNext();) {
            String value = (String) itr.next();
            switch (i) {
                case 0:
                    assertEquals("id", value);
                    break;
                case 1:
                    assertEquals("abc", value);
                    break;
                case 2:
                    assertEquals("ia", value);
                    break;
                case 3:
                    assertEquals("ie", value);
                    break;
            }
            i ++;
        }
        assertEquals(null, msg.get("notFound"));
    }

    public void test01() {
        msg.set("HEAD", "VER", "1.0");
        assertEquals("1.0", msg.get("HEAD", "VER"));
    }

    public void testNoExistsCategory() {
        assertEquals(null, msg.get("abc", "def"));
    }
}
