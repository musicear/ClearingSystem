package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TestCtaisMdt extends TestCase{
  public void testSend() {
        Message msg = new Message();
        msg.setValue("交易码","1500");
        msg.setValue("pzxh","12345678901234567890");
        msg.setValue("dzbh", "00");
        msg.setValue("nsrsbh","37013298279208");
        msg.setValue("nsrmc","李国印");
        msg.setValue("swjg","13702020000");  //征收机关代码
        msg.setValue("skgk_dm","37020201");
         msg.setValue("tbrq","2005-11-07");
        msg.setValue("ysfpbl_dm_tz","60002020");
        msg.setValue("ysfpbl_dm_tj","60002020");
        msg.setValue("yskm_dm_tz","000000");
        msg.setValue("yskm_dm_tj","000000");
        msg.setValue("mdje","100.00");

        Controller.getInstance().execute(msg);
        assertEquals("MDTK.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("交易成功",msg.getValue("returnMessage"));
        assertEquals("12345678901234567890",msg.getValue("pzxh"));
        //assertEquals("000",msg.getValue("响应码"));
        System.out.println(msg.getValue("响应码"));
    }
    public static void main(String[] args)  {
      TestCtaisMdt testCtaisMdt = new TestCtaisMdt();
      testCtaisMdt.testSend();
    }
}
