package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function: ����Ctais2.0���˿�
 * User: albert lee
 * Date: 2005-11-4
 * Time: 14:30:46
 */
public class TestCtaisTuiku extends TestCase {
    public void testSend() {
        Message msg = new Message();
        msg.setValue("������",1300);
        msg.setValue("XTSPHM","370198980989099821");
        msg.setValue("THSH","423432432423");
        msg.setValue("NSRSBH","3710982901928");
        msg.setValue("NSRMC","����������");
        msg.setValue("SSSQ_Q","2005-11-01");
        msg.setValue("SSSQ_Z","2005-11-29");
        msg.setValue("KPRQ","2005-11-05");
        msg.setValue("ZSXM_DM1","06"); //������Ŀ����
        msg.setValue("ZSXM_MC1","��������˰");
        msg.setValue("ZSPM_DM1","0201");
        msg.setValue("ZSPM_MC1","���");
        msg.setValue("SKZL_DM","");  //˰��������ʲô��      ��˰�������ɽ�֮��
        msg.setValue("SKSX_DM","");   //˰��������ʲô��
        msg.setValue("SE1","8501.13");
        msg.setValue("PZZL_DM","411");
        msg.setValue("TTLX_DM","5");   //��������
        msg.setValue("CKTSGC_DM","");  //������˰���ɴ���
        msg.setValue("CKTS_QYLX_DM","");//������˰��ҵ����
        msg.setValue("TZLX_DM","");            //��������
        msg.setValue("YSFPBL_DM","005050");
        msg.setValue("YSKM_DM","060102");
        msg.setValue("SKGK_DM","37020201");
        msg.setValue("YHZL_DM","3703");
        msg.setValue("YH_MC","�������");
        msg.setValue("YHZH","43298108219281");
        msg.setValue("SKSS_SWJG_DM","13702020000");
        Controller.getInstance().execute(msg);
        assertEquals("SRTHSTK.TaxBank.Ctais",msg.getValue("������"));
        assertEquals("���׳ɹ�",msg.getValue("returnMessage"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("370198980989099821",msg.getValue("XTSPHM"));
        assertEquals("423432432423",msg.getValue("THSH"));
        assertEquals("13702009904",msg.getValue("LRR_DM"));

    }
}