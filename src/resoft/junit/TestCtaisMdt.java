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
        msg.setValue("������","1500");
        msg.setValue("pzxh","12345678901234567890");
        msg.setValue("dzbh", "00");
        msg.setValue("nsrsbh","37013298279208");
        msg.setValue("nsrmc","���ӡ");
        msg.setValue("swjg","13702020000");  //���ջ��ش���
        msg.setValue("skgk_dm","37020201");
         msg.setValue("tbrq","2005-11-07");
        msg.setValue("ysfpbl_dm_tz","60002020");
        msg.setValue("ysfpbl_dm_tj","60002020");
        msg.setValue("yskm_dm_tz","000000");
        msg.setValue("yskm_dm_tj","000000");
        msg.setValue("mdje","100.00");

        Controller.getInstance().execute(msg);
        assertEquals("MDTK.TaxBank.Ctais",msg.getValue("������"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("���׳ɹ�",msg.getValue("returnMessage"));
        assertEquals("12345678901234567890",msg.getValue("pzxh"));
        //assertEquals("000",msg.getValue("��Ӧ��"));
        System.out.println(msg.getValue("��Ӧ��"));
    }
    public static void main(String[] args)  {
      TestCtaisMdt testCtaisMdt = new TestCtaisMdt();
      testCtaisMdt.testSend();
    }
}
