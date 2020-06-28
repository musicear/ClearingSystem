package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function: 测试Ctais2.0的退库
 * User: albert lee
 * Date: 2005-11-4
 * Time: 14:30:46
 */
public class TestCtaisTuiku extends TestCase {
    public void testSend() {
        Message msg = new Message();
        msg.setValue("交易码",1300);
        msg.setValue("XTSPHM","370198980989099821");
        msg.setValue("THSH","423432432423");
        msg.setValue("NSRSBH","3710982901928");
        msg.setValue("NSRMC","国际主义者");
        msg.setValue("SSSQ_Q","2005-11-01");
        msg.setValue("SSSQ_Z","2005-11-29");
        msg.setValue("KPRQ","2005-11-05");
        msg.setValue("ZSXM_DM1","06"); //征收项目代码
        msg.setValue("ZSXM_MC1","个人所得税");
        msg.setValue("ZSPM_DM1","0201");
        msg.setValue("ZSPM_MC1","稿费");
        msg.setValue("SKZL_DM","");  //税款种类是什么？      正税还是滞纳金之分
        msg.setValue("SKSX_DM","");   //税款属性是什么？
        msg.setValue("SE1","8501.13");
        msg.setValue("PZZL_DM","411");
        msg.setValue("TTLX_DM","5");   //提退类型
        msg.setValue("CKTSGC_DM","");  //出口退税构成代码
        msg.setValue("CKTS_QYLX_DM","");//出口退税企业类型
        msg.setValue("TZLX_DM","");            //调帐类型
        msg.setValue("YSFPBL_DM","005050");
        msg.setValue("YSKM_DM","060102");
        msg.setValue("SKGK_DM","37020201");
        msg.setValue("YHZL_DM","3703");
        msg.setValue("YH_MC","恒丰银行");
        msg.setValue("YHZH","43298108219281");
        msg.setValue("SKSS_SWJG_DM","13702020000");
        Controller.getInstance().execute(msg);
        assertEquals("SRTHSTK.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("交易成功",msg.getValue("returnMessage"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("370198980989099821",msg.getValue("XTSPHM"));
        assertEquals("423432432423",msg.getValue("THSH"));
        assertEquals("13702009904",msg.getValue("LRR_DM"));

    }
}