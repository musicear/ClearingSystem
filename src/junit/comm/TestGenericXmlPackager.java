package junit.comm;

import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import junit.framework.TestCase;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlMessage;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>测试通用Xml解析器</p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 20:16:15
 */
public class TestGenericXmlPackager extends TestCase {
    private Packager packager = null;

    public void setUp() {
        packager = new GenericXmlPackager();
    }

    public void testUnpack() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<TIPS>\n" +
                "\t<HEAD>\n" +
                "\t\t<VER>1.0</VER>\n" +
                "\t</HEAD>\n" +
                "\t<BODY>\n" +
                "\t\t<RealHead>\n" +
                "\t\t\t<MSGID>132423432</MSGID>\n" +
                "\t\t\t<MSGID>98918712</MSGID>\n" +
                "\t\t\t<CONTENT><![CDATA[This is ]]></CONTENT>\n" +
                "\t\t</RealHead>\t\t\n" +
                "\t</BODY>\n" +
                "</TIPS>";
        Message msg = packager.unpack(xml.getBytes());
        assertEquals("1.0", msg.get("//TIPS/HEAD/VER"));
        assertEquals("132423432", msg.get("//TIPS/BODY/RealHead/MSGID[1]"));
        assertEquals("98918712", msg.get("//TIPS/BODY/RealHead/MSGID[2]"));
        assertEquals("", msg.get("//TIPS/BODY/RealHead/MSGID1"));
        List list = (List) msg.get("list", "//TIPS/BODY/RealHead/MSGID");
        assertEquals(2, list.size());
        assertEquals("132423432", list.get(0));
        assertEquals("98918712", list.get(1));
        assertEquals("This is ", msg.get("//TIPS/BODY/RealHead/CONTENT"));

    }

    public void testPack() {
        Message msg = new GenericXmlMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.set("CDATA","//CFX/HEAD/SRC", "4324324");
        msg.set("//CFX/HEAD/APP", "TIPS");
        msg.set("//CFX/MSG/SingleReturn2001/Result", "90000");
        msg.set("//CFX/MSG/SingleReturn2001/AddWord", "交易成功");
        msg.set("//CFX/MSG/SingleReturn2001/A",null);
        msg.set("//CFX/MSG/BatchHead2102[1]/Result", "90000");
        msg.set("//CFX/MSG/BatchHead2102[2]/Result", "11111");
        msg.set("//CFX/MSG/BatchHead2102[2]/Test[1]/Title","def");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<CFX><HEAD><VER>1.0</VER><SRC><![CDATA[4324324]]></SRC><APP>TIPS</APP></HEAD><MSG><SingleReturn2001><Result>90000</Result><AddWord>交易成功</AddWord></SingleReturn2001><BatchHead2102><Result>90000</Result></BatchHead2102><BatchHead2102><Result>11111</Result><Test><Title>def</Title></Test></BatchHead2102></MSG></CFX>";
        assertEquals(xml, new String(packager.pack(msg)));
    }

    public void testPackByFile() {
        //@todo 此案例通不过。BatchHead2102[1]/Test[1]不存在，但BatchHead2102[1]/Test[2]存在。这种情况没往下循环处理
        Message msg = new GenericXmlMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.set("CDATA", "//CFX/HEAD/SRC", "4324324");
        msg.set("//CFX/HEAD/APP", "TIPS");
        msg.set("//CFX/MSG/SingleReturn2001/Result", "90000");
        msg.set("//CFX/MSG/SingleReturn2001/AddWord", "交易成功");
        msg.set("//CFX/MSG/BatchHead2102[1]/Result", "90000");
        msg.set("//CFX/MSG/BatchHead2102[2]/Result", "11111");
        msg.set("//CFX/MSG/BatchHead2102[2]/Test[1]/Title","def");
        msg.set("packFile", "./conf/pack/1000.xml");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<CFX><HEAD><VER>1.0</VER><SRC><![CDATA[4324324]]></SRC></HEAD><MSG><SingleReturn2001><Result>90000</Result></SingleReturn2001><BatchHead2102><Result>90000</Result></BatchHead2102><BatchHead2102><Result>11111</Result><Test><Title>def<Title></Test></BatchHead2102></MSG></CFX>";
        assertEquals(xml, new String(packager.pack(msg)));
    }
    public void testPackByFileDefaultMessage() {
        Message msg = new DefaultMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.set("CDATA", "//CFX/HEAD/SRC", "4324324");
        msg.set("//CFX/HEAD/APP", "TIPS");
        msg.set("//CFX/MSG/SingleReturn2001/Result", "90000");
        msg.set("//CFX/MSG/SingleReturn2001/AddWord", "交易成功");
        msg.set("//CFX/MSG/BatchHead2102[1]/Result", "90000");
        msg.set("//CFX/MSG/BatchHead2102[2]/Result", "11111");
        msg.set("packFile", "./conf/pack/1000.xml");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<CFX><HEAD><VER>1.0</VER></HEAD><MSG><SingleReturn2001><Result>90000</Result></SingleReturn2001><BatchHead2102><Result>90000</Result></BatchHead2102><BatchHead2102><Result>11111</Result></BatchHead2102></MSG></CFX>";
        assertEquals(xml, new String(packager.pack(msg)));
    }
    /**
     * 双重嵌套测试
     * */
    public void testPackByFileDefaultMessageDualLoop() {
        Message msg = new DefaultMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.set("CDATA", "//CFX/HEAD/SRC", "4324324");
        msg.set("//CFX/HEAD/APP", "TIPS");
        msg.set("//CFX/MSG/SingleReturn2001/Result", "90000");
        msg.set("//CFX/MSG/SingleReturn2001/AddWord", "交易成功");
        msg.set("//CFX/MSG/BatchHead2102[1]/Result", "90000");
        msg.set("//CFX/MSG/BatchHead2102[2]/Result", "11111");
        msg.set("//CFX/MSG/BatchHead2102[1]/Test[1]/Title","abc");
        msg.set("//CFX/MSG/BatchHead2102[1]/Test[2]/Title","def");
        msg.set("//CFX/MSG/BatchHead2102[2]/Test[1]/Title","abcd");
        msg.set("//CFX/MSG/BatchHead2102[2]/Test[2]/Title","defd");
        msg.set("packFile", "./conf/pack/1000.xml");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<CFX><HEAD><VER>1.0</VER></HEAD><MSG><SingleReturn2001><Result>90000</Result></SingleReturn2001><BatchHead2102><Result>90000</Result><Test><Title>abc</Title></Test><Test><Title>def</Title></Test></BatchHead2102><BatchHead2102><Result>11111</Result><Test><Title>abcd</Title></Test><Test><Title>defd</Title></Test></BatchHead2102></MSG></CFX>";
        assertEquals(xml, new String(packager.pack(msg)));
    }

    public void testOtherMessage() {
        Message msg = new GenericXmlMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.set("other", "other");
        assertEquals("1.0", msg.get("//CFX/HEAD/VER"));
        assertEquals("other", msg.get("other"));
    }

    public void testClear() {
        Message msg = new GenericXmlMessage();
        msg.set("//CFX/HEAD/VER", "1.0");
        msg.clear();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        assertEquals(xml, new String(packager.pack(msg)));
    }

    public void testPackNameSpace() {
        Message msg = new GenericXmlMessage();

        msg.set("//CFX/HEAD/VER", "1.0");

        msg.set(GenericXmlMessage.NAMESPACE,"xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        msg.set(GenericXmlMessage.NAMESPACE,"xsi:noNamespaceSchemaLocation","Msg201.xsd");
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<CFX xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"Msg201.xsd\"><HEAD><VER>1.0</VER></HEAD></CFX>";
        assertEquals(xml, new String(packager.pack(msg)));
    }

    public void testCDATA() {
        Element e = DocumentHelper.createElement("test");
        e.add(DocumentHelper.createElement("another").addCDATA("abc"));
        System.out.println(e.asXML());
        //System.out.println(DocumentHelper.createDocument(e).asXML());
    }
}
