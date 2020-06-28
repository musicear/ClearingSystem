package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.Message;

/**
 * function: 测试Ctais的正划
 * User: albert lee
 * Date: 2005-11-3
 * Time: 17:13:16
 */
public class TestCtaisZhengHua extends TestCase {
    public void testSend() {
        Message msg = new Message();
        msg.setValue("交易码","1100");
        msg.setValue("xtsphm", "37020298190928782909");
        msg.setValue("jyrq", "2005-11-04");
        msg.setValue("nsrsbh", "37020839280282");
        msg.setValue("nsrmc", "自由、民主");
        msg.setValue("zsjg_dm", "13702120000");
        msg.setValue("djzclx_dm", "102");
        msg.setValue("djzclx_mc", "私营企业");
        msg.setValue("yhzl_dm", "0032");
        msg.setValue("yh_mc", "招行晓望路支行");
        msg.setValue("yhzh", "3423432423432");
        msg.setValue("sssq_q", "2005-11-01");
        msg.setValue("sssq_z", "2005-11-30");
        msg.setValue("spzl_dm", "00102");
        msg.setValue("yskm_dm", "041400");
        msg.setValue("yskm_mc", "国有核工业所得税");
        msg.setValue("ysfpbl_dm", "1011");
        msg.setValue("ysfpbl_mc", "中央75%省市12.5%地市12.5%");
        msg.setValue("skgk_dm", "37029601");
        msg.setValue("skgk_mc", "国家金库保税区支库");
        msg.setValue("lsgx_dm", "103");
        msg.setValue("lsgx_jc", "部级");
        msg.setValue("spjkqx", "2005-11-15");
        msg.setValue("zsxm_dm1", "04");
        msg.setValue("zsxm_mc1", "企业所得税");
        msg.setValue("zspm_dm1", "0203");
        msg.setValue("zspm_mc1", "采矿业所得税");
        msg.setValue("kssl1", "20000000");
        msg.setValue("xssr1", "1000000");
        msg.setValue("sl1","0.20");
        msg.setValue("yjhkcje1","500000");
        msg.setValue("sjse1","5000000");
        msg.setValue("bz1","与太阳争辉");

        Controller.getInstance().execute(msg);
        assertEquals("SKZH.Ctais.TaxBank",msg.getValue("交易码"));
        assertEquals("00",msg.getValue("returnCode"));
        assertEquals("交易成功",msg.getValue("returnMessage"));
        assertEquals("30",msg.getValue("splx"));
        assertEquals("37020298190928782909",msg.getValue("freezeNo"));

    }
}
