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
        //免抵调的格式
        String str = "1500@-@凭证编号@-@金额@-@征收机关@-@收款国库@-@开票日期@-@纳税人编码@-@调增科目@-@调减科目@-@调增级次@-@调减级次@-@";
        Message msg = packer.pack(str.getBytes());
        assertEquals(msg.getValue("交易码"),"1500");
        assertEquals(msg.getValue("凭证号码"),"凭证编号");
        assertEquals(msg.getValue("纳税人编号"),"纳税人编码");
        assertEquals(msg.getValue("收款国库"),"收款国库");
        assertEquals(msg.getValue("金额"),"金额");
        assertEquals(msg.getValue("开票日期"),"开票日期");
        assertEquals(msg.getValue("调减级次"),"调减级次");
    }
    public void test1000() {
        String str = "1000@-@37020261420979X@-@小二黑@-@0011@-@3803021109024818857@-@银行名称@-@00101@-@××××税收分局@-@13702020000@-@370205021261879@-@2004-12-1@-@2004-12-31@-@2005-1-7@-@37020201@-@12@-@隶属关系@-@3157.71@-@2005-1-13@-@备注@-@101@-@010106@-@@-@1053@-@1601@-@0@-@@-@0.170000@-@18297.6@-@3157.71@-@@-@@-@0@-@@-@0@-@0@-@0@-@@-@@-@0@-@@-@0@-@0@-@0@-@";
        Message msg = packer.pack(str.getBytes());
        assertEquals(msg.getValue("交易码"),"1000");
        assertEquals(msg.getValue("征收机关"),"13702020000");
        assertEquals(msg.getValue("收款国库"),"37020201");
        assertEquals("0011",msg.getValue("付款机构"));
    }
    public void testStringTokenizer() {
        StringTokenizer st = new StringTokenizer("2004-01-02@-@abc","@-@");
        assertEquals("2004-01-02",st.nextToken());
        assertEquals("abc",st.nextToken());

    }
}
