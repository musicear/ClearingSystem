package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.util.IDPackager;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 *
 * @Author: liguoyin
 * Date: 2007-5-20
 * Time: 16:28:36
 */
public class TestIDPackager extends TestCase {
    public void testPack() {
        Message msg = new DefaultMessage();
        msg.set("BusinessCode","01");
        Packager packager = new IDPackager();
        String xml = "<CFX><MSG><BusinessCode>01</BusinessCode></MSG></CFX>";
        assertEquals(xml,new String(packager.pack(msg)));
    }
    public void testUnpack() {
        String xml = "<CFX><MSG><BusinessCode>01</BusinessCode></MSG></CFX>";
        Packager packager = new IDPackager();
        Message msg = packager.unpack(xml.getBytes());
        assertEquals("01",msg.get("BusinessCode"));
    }
}
