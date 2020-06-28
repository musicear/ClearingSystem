package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * <p>function:测试退库撤销</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-9</p>
 * <p>Time: 18:12:36</p>
 */
public class TestCtaisTuiKuCheXiao extends TestCase{
    public void testSend() {
        Message msg = new Message();
        msg.setValue("交易码","1307");
        msg.setValue("XTSPHM","341343214");
        msg.setValue("NSRSBH","34234324");
        msg.setValue("THSH","3424234");
        Controller.getInstance().execute(msg);
        assertEquals("CXTK.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("交易成功",msg.getValue("returnMessage"));
        assertEquals("341343214",msg.getValue("XTSPHM"));
        assertEquals("3424234",msg.getValue("THSH"));
    }
}
