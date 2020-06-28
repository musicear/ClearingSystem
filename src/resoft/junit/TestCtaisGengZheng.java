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
public class TestCtaisGengZheng
    extends TestCase {
  public void testSend() {
    Message msg = new Message();
    msg.setValue("交易码", "1400");
    msg.setValue("gzswjg", "13702020000");
    msg.setValue("skgk_dm", "37020201");
    msg.setValue("tbrq", "2005-11-07");
    msg.setValue("ysfpbl_dm", "60002020");
    msg.setValue("yskm_dm", "000000"); //征收机关代码
    msg.setValue("je", "100.00");
    msg.setValue("bz", "备注");
        msg.setValue("wsxh","01234567890123456789");
        msg.setValue("系统税票号码", "12345678901234567890");
//        msg.setValue("ysfpbl_dm_tj","60002020");
//        msg.setValue("yskm_dm_tz","000000");
//        msg.setValue("yskm_dm_tj","000000");
//        msg.setValue("mdje","100.00");

    Controller.getInstance().execute(msg);
    //assertEquals("Synchronization.Freeze.TaxBank.Ctais",msg.getValue("交易码"));
    //assertEquals("00",msg.getValue("returnCode"));
    //assertEquals("交易成功",msg.getValue("returnMessage"));
    //assertEquals("30", msg.getValue("splx"));
    //assertEquals("000",msg.getValue("响应码"));
    System.out.println("响应码：" + msg.getValue("响应码"));
  }

  public static void main(String[] args) {
    TestCtaisGengZheng testctaisgengzheng = new TestCtaisGengZheng();
    testctaisgengzheng.testSend();
  }
}
