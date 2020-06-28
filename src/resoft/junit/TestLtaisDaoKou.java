package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;

/**
 * function: 测试倒扣
 * User: albert lee
 * Date: 2005-9-27
 * Time: 13:14:09
 */
public class TestLtaisDaoKou extends TestCase{
    public void testDaoKou() throws Exception{
        Message msg = new Message();
        msg.setValue("nsrsbh","3701020304098");
        msg.setValue("nsrmc","济南新华书店");
        msg.setValue("yhzlDm","0011");
        msg.setValue("yhMc","工行");
        msg.setValue("yhzh","3898209182028");
        msg.setValue("pzzlDm","501");
        msg.setValue("zsjgDm","23701020000");
        msg.setValue("xtspHm","37019871190988611328");
        msg.setValue("sssqQ","2005-09-01");
        msg.setValue("sssqZ","2005-09-30");
        msg.setValue("tfRq","2005-09-26");
        msg.setValue("skgkDm","23701020");
        msg.setValue("hyDm","010");
        msg.setValue("hyMc","批发零售业");
        msg.setValue("djzclxDm","101");
        msg.setValue("djzclxMc","国有企业");
        msg.setValue("kpJe","35000.00");
        msg.setValue("xjRq","2005-09-30");
        msg.setValue("bz","");
        msg.setValue("sbfsDm","1");  //申报方式代码

        msg.setValue("zsxmDm1","211");
        msg.setValue("yskmDm1","1501");
        msg.setValue("ysfpblDm1","2370104302");
        msg.setValue("zspmDm1","0105");
        msg.setValue("kssl1","35");
        msg.setValue("xssr1","37000123.00");
        msg.setValue("sl1","15%");
        msg.setValue("yjkce1","0.00");
        msg.setValue("sjSe1","30000.00");

        msg.setValue("zsxmDm2","212");
        msg.setValue("yskmDm2","1502");
        msg.setValue("ysfpblDm2","2370104302");
        msg.setValue("zspmDm2","01");
        msg.setValue("kssl2","35");
        msg.setValue("xssr2","37000123.00");
        msg.setValue("sl2","15%");
        msg.setValue("yjkce2","0.00");
        msg.setValue("sjSe2","5000.00");

        Message returnMsg = BasLinkClient.send("127.0.0.1",1200,"1000",msg);
        assertEquals("000",returnMsg.getValue("returnCode"));
        assertEquals("交易成功",returnMsg.getValue("returnMesg"));
        
    }
}
