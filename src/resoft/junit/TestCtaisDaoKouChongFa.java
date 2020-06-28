package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function: 测试倒扣重发
 * User: albert lee
 * Date: 2005-11-5
 * Time: 9:57:17
 */
public class TestCtaisDaoKouChongFa extends TestCase{
    public void testNotExistVoucher() {
        Message msg = new Message();
        msg.setValue("交易码","1010");
        msg.setValue("xtsphm","00000098900000");
        msg.setValue("nsrsbh","37103424322");
        msg.setValue("kpje","8192.16");
        Controller.getInstance().execute(msg);
        assertEquals("ResoftDKCF.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("34",msg.getValue("returnCode"));
        assertEquals("无此税票",msg.getValue("returnMessage"));
    }
    public void testExistVoucher() {
        Message msg = new Message();
        msg.setValue("交易码","1010");
        msg.setValue("xtsphm","370205000000066205");
        msg.setValue("nsrsbh","370282163966344");
        msg.setValue("kpje","190.00");
        Controller.getInstance().execute(msg);
        assertEquals("ResoftDKCF.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("成功",msg.getValue("returnMessage"));
        assertEquals("00",msg.getValue("returnCode"));

        assertEquals("370205000000066205",msg.getValue("freezeNo"));
        assertEquals("30",msg.getValue("splx"));
    }
}
