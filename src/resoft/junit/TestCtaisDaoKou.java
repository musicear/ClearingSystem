package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function:  测试Ctais倒扣
 * User: albert lee
 * Date: 2005-11-3
 * Time: 1:28:43
 */
public class TestCtaisDaoKou extends TestCase{
    public void testSend() {
        Message msg = new Message();
        msg.setValue("交易码","1000");
        msg.setValue("xtsphm","37010987898765434567");
        msg.setValue("jyrq","2005-11-4");          //什么意思？
        msg.setValue("nsrsbh","37013298279208");
        msg.setValue("nsrmc","李国印");
        msg.setValue("nsr_swjg_dm","13702020000");  //征收机关代码
        msg.setValue("zsjg_dm","13702020000");
        msg.setValue("zsjg_jc","青岛市市南国家税务局");
        msg.setValue("zgswry_dm","00");
        msg.setValue("lrr_dm","00");
        msg.setValue("djzclx_dm","101");
        msg.setValue("djzclx_mc","国有企业");
        msg.setValue("yhzl_dm","0011"); //银行代码
        msg.setValue("yh_dm","0011");
        msg.setValue("yh_mc","工行");
        msg.setValue("yhzh","1298198273928272");
        msg.setValue("sssq_q","2005-11-1");
        msg.setValue("sssq_z","2005-11-30");
        msg.setValue("spzl_dm","00101");
        msg.setValue("yskm_dm","010101");
        msg.setValue("yskm_mc","国有企业增值税");
        msg.setValue("ysfpbl_dm","1001");
        msg.setValue("ysfpbl_mc","中央75%省25%");
        msg.setValue("skgk_dm","37020201");
        msg.setValue("skgk_mc","市南国库");
        msg.setValue("lsgx_dm","10");
        msg.setValue("lsgx_jc","省级企业");
        msg.setValue("spjkqx","2005-11-10");//限缴日期
        msg.setValue("zsxm_dm1","10");
        msg.setValue("zsxm_mc1","增值税");
        msg.setValue("zspm_dm1","010010");
        msg.setValue("zspm_mc","采矿业");
        msg.setValue("kssl1","100000");
        msg.setValue("xssr1","0");
        msg.setValue("sl1","10");
        msg.setValue("yjhkcje1","800");
        msg.setValue("sjse1","1000");

        msg.setValue("bz1","好好缴税，天天挣钱");

        Controller.getInstance().execute(msg);
        assertEquals("Synchronization.Freeze.TaxBank.Ctais",msg.getValue("交易码"));
        assertEquals("交易成功",msg.getValue("returnMessage"));
        assertEquals("00",msg.getValue("returnCode"));

        assertEquals("30",msg.getValue("splx"));
        assertEquals("37010987898765434567",msg.getValue("freezeNo"));


    }
}
