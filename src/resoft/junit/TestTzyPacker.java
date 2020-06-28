package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.Packer;
import resoft.basLink.packer.TagPacker;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-14
 * Time: 14:20:00
 */
public class TestTzyPacker extends TestCase{
    private Packer packer = new TagPacker();
    public void testPack() {
        //��ֵ��ĸ�ʽ
        String str = "1500@-@ƾ֤���@-@���@-@���ջ���@-@�տ����@-@��Ʊ����@-@��˰�˱���@-@������Ŀ@-@������Ŀ@-@��������@-@��������@-@";
        Message msg = packer.pack(str.getBytes());
        assertEquals(msg.getValue("������"),"1500");
        assertEquals(msg.getValue("ƾ֤����"),"ƾ֤���");
        assertEquals(msg.getValue("��˰�˱��"),"��˰�˱���");
        assertEquals(msg.getValue("�տ����"),"�տ����");
        assertEquals(msg.getValue("���"),"���");
        assertEquals(msg.getValue("��Ʊ����"),"��Ʊ����");
        assertEquals(msg.getValue("��������"),"��������");
    }
    public void test1000() {
        String str = "1000@-@37020261420979X@-@С����@-@0011@-@3803021109024818857@-@��������@-@00101@-@��������˰�շ־�@-@13702020000@-@370205021261879@-@2004-12-1@-@2004-12-31@-@2005-1-7@-@37020201@-@12@-@������ϵ@-@3157.71@-@2005-1-13@-@��ע@-@101@-@010106@-@@-@1053@-@1601@-@0@-@@-@0.170000@-@18297.6@-@3157.71@-@@-@@-@0@-@@-@0@-@0@-@0@-@@-@@-@0@-@@-@0@-@0@-@0@-@";
        Message msg = packer.pack(str.getBytes());
        assertEquals(msg.getValue("������"),"1000");
        assertEquals(msg.getValue("���ջ���"),"13702020000");
        assertEquals(msg.getValue("�տ����"),"37020201");
        assertEquals("0011",msg.getValue("�������"));
    }
    public void testStringTokenizer() {
        StringTokenizer st = new StringTokenizer("2004-01-02@-@abc","@-@");
        assertEquals("2004-01-02",st.nextToken());
        assertEquals("abc",st.nextToken());

    }
}
