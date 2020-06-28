package resoft.junit;

import java.util.List;

import junit.framework.TestCase;
import resoft.basLink.Configuration;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-20
 * Time: 16:54:51
 */
public class TestConfiguration extends TestCase {
    public void test1() {
        List list = (List) Configuration.getInstance().listAllProperties("head");
        assertEquals("信息类型",list.get(0));
        assertEquals("b",Configuration.getInstance().getProperty("test","title"));
    }
    public void test2() {
        List list = (List) Configuration.getInstance().listAllProperties("notExists");
        assertEquals(0,list.size());
    }
    public void testSequenceValues() {
        List list = (List) Configuration.getInstance().getPropertiesMap("head").values();
        assertEquals("C2",list.get(0));
        assertEquals("C8",list.get(1));
        assertEquals("C1",list.get(2));
    }
}
