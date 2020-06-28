package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function: ����Ctais2.0��������
 * User: albert lee
 * Date: 2005-11-4
 * Time: 14:01:08
 */
public class TestCtaisZhengHuaCheXiao extends TestCase{
    public void testSend() {
        Message msg = new Message();
        msg.setValue("������","1102");
        msg.setValue("xtsphm","37020198290987654289");
        msg.setValue("nsrsbh","3701028727812");
        msg.setValue("skss_swjg_dm","13702050000");
        msg.setValue("yhzl_dm","0013");
        msg.setValue("yh_mc","���м��Ϸ���");
        msg.setValue("yhzh","3242342135325");
        msg.setValue("spzl_dm","30");
        msg.setValue("sjse","812.00");
        Controller.getInstance().execute(msg);
        assertEquals("CXZH.Ctais.TaxBank",msg.getValue("������"));
        assertEquals("�ɹ�",msg.getValue("returnMessage"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("30",msg.getValue("returnType"));
        assertEquals("37020198290987654289",msg.getValue("xtsphm"));
    }
}
