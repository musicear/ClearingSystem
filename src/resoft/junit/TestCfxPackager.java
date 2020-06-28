package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.util.CfxPackager;
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
public class TestCfxPackager extends TestCase {
    public void testPack() {
        Message msg = new DefaultMessage();
        msg.set("BusinessCode","01");
        Packager packager = new CfxPackager();
        String xml = "<CFX><MSG><BusinessCode val=\"01\"/></MSG></CFX>";
        assertEquals(xml,new String(packager.pack(msg)));
    }
    public void testPackWithFormatFile() {
        Message msg = new DefaultMessage();
        msg.set("packFile","./conf/pack/01.xml");
        msg.set("BusinessCode","01");
        msg.set("head","SRC","1000");
        msg.set("pri1","abc");
        Packager packager = new CfxPackager();
        String xml = "<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><BusinessCode val=\"01\"/></MSG><PRI><pri1 val=\"abc\"/></PRI></CFX>\r\n";
        assertEquals(xml,new String(packager.pack(msg)));
    }
    public void testUnpack() {
        String xml = "<CFX><MSG><BusinessCode val=\"01\"/></MSG></CFX>";
        Packager packager = new CfxPackager();
        Message msg = packager.unpack(xml.getBytes());
        assertEquals("01",msg.get("BusinessCode"));
    }
}
