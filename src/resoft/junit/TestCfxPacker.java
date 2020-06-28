package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.PackException;
import resoft.basLink.Packer;
import resoft.basLink.packer.CfxPacker;

/**
 * function:
 * User: albert lee
 * Date: 2005-8-23
 * Time: 13:31:07
 */
public class TestCfxPacker extends TestCase{
    private Packer packer = null;
    public void setUp() {
        packer = new CfxPacker();
    }
    public void testPack() throws Exception{
        String xml = "<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><������ val=\"abc\"/></MSG></CFX>";
        Message msg = packer.pack(xml.getBytes());
        assertEquals("abc",msg.getValue("������"));
        assertEquals("1000",msg.getHeadValue("SRC"));
    }

    public void testInvalidXml(){
        String wrongXml = "</CFX>";
        try {
            packer.pack(wrongXml.getBytes());
        } catch(PackException e) {
            return;
        }

        fail("It haven't throw exception");
    }

    public void testUnpackByFile() {
        Message msg = new Message();
        msg.setHeadValue("SRC","1000");
        msg.setValue("������","1000");
        msg.setValue("��ˮ��","121232342423");
        msg.setValue("slRq","2005/08/08");
        msg.setValue("�ڲ�ֵ","1234");

        byte[] bytes = packer.unpackByFile(msg);
        String returnValue = new String(bytes);
        assertEquals("<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><������ val=\"1000\"/><slRq val=\"2005/08/08\"/></MSG></CFX>\r\n",returnValue);
    }
    public void testUnpack() {
        Message msg = new Message();
        msg.setValue("��ˮ��","1234");
        
        String returnValue = new String(packer.unpack(msg));
        assertEquals("<CFX><MSG><��ˮ�� val=\"1234\"/></MSG></CFX>",returnValue);

    }
    public void testUnpackWithSpecialValue() {
        Message msg = new Message();
        msg.setHeadValue("SRC","1000");
        msg.setValue("���λ����","�»����<ɽ��·>");
        String returnValue = new String(packer.unpack(msg));
        assertEquals("<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><���λ���� val=\"�»����&lt;ɽ��·&gt;\"/></MSG></CFX>",returnValue);
    }

    public void testUnpackHeadValue() {
        Message msg = new Message();
        msg.setHeadValue("SRC","1000");
        String returnValue = new String(packer.unpack(msg));
        assertEquals("<CFX><HEAD><SRC>1000</SRC></HEAD><MSG></MSG></CFX>",returnValue);
    }
    /*
    * ���Ա���������PRI�ε����
    */
    public void testPriPack() {
        String xml = "<CFX><MSG><������ val=\"abc\"/></MSG><PRI><ƷĿ val=\"028\"/></PRI></CFX>";
        Message msg = packer.pack(xml.getBytes());
        assertEquals("abc",msg.getValue("������"));
        assertEquals("028",msg.getValue("ƷĿ"));
    }

    public void testPriUnpackByFile() {
        Message msg = new Message();
        msg.setHeadValue("SRC","1000");
        msg.setValue("������","1000");
        msg.setValue("��ˮ��","121232342423");

        msg.setValue("������ϵ","");
        msg.setValue("ע������","������ҵ");

        byte[] bytes = packer.unpackByFile(msg,"1000F.xml");
        String returnValue = new String(bytes);
        assertEquals("<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><������ val=\"1000\"/><��ˮ�� val=\"121232342423\"/></MSG><PRI><ע������ val=\"������ҵ\"/></PRI></CFX>\r\n",returnValue);
    }

    public void testPackThenUnpack() {
        String xml = "<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><������ val=\"1000\"/><��ˮ�� val=\"121232342423\"/></MSG><PRI><ע������ val=\"������ҵ\"/></PRI></CFX>\r\n";
        Message m = packer.pack(xml.getBytes());
        String returnXml = new String(packer.unpackByFile(m,"1000F.xml"));
        assertEquals(xml,returnXml);
    }

    /**
     * ����unPackByFile֮����
     * */
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        for(int i=0;i<100000;i++) {
            TestCfxPacker t = new TestCfxPacker();
            t.setUp();
            t.testPriUnpackByFile();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
