package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;

/**
 * function: 测试正划
 * User: albert lee
 * Date: 2005-9-27
 * Time: 13:14:09
 */
public class TestLtaisZhengHua extends TestCase{
    public void testZhengHua() throws Exception{
        Message msg = new Message();
        msg.setValue("nsrsbh","3701020304098");
        msg.setValue("nsrmc","济南新华书店");
        msg.setValue("yhzlDm","0011");
        msg.setValue("yhMc","工行");
        msg.setValue("yhzh","3898209182028");
        msg.setValue("pzzlDm","501");
        msg.setValue("zsjgDm","23701020000");
        msg.setValue("xtspHm","37013876590118824311");
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
        msg.setValue("sbfsDm","41");  //申报方式代码

        msg.setValue("zsxmDm1","203");
        msg.setValue("yskmDm1","1501");
        msg.setValue("ysfpblDm1","2370104302");
        msg.setValue("zspmDm1","0603");
        msg.setValue("kssl1","35");
        msg.setValue("xssr1","37000123.00");
        msg.setValue("sl1","15%");
        msg.setValue("yjkce1","0.00");
        msg.setValue("sjSe1","30000.00");

        msg.setValue("zsxmDm2","203");
        msg.setValue("yskmDm2","1501");
        msg.setValue("ysfpblDm2","2370104302");
        msg.setValue("zspmDm2","0603");
        msg.setValue("kssl2","35");
        msg.setValue("xssr2","37000123.00");
        msg.setValue("sl2","15%");
        msg.setValue("yjkce2","0.00");
        msg.setValue("sjSe2","5000.00");

        Message returnMsg = BasLinkClient.send("127.0.0.1",1200,"1100",msg);
        assertEquals("000",returnMsg.getValue("returnCode"));
        assertEquals("交易成功",returnMsg.getValue("returnMesg"));
        //System.out.println(returnMsg);
    }

    public static void main(String[] args) {

        for(int i=0;i<1000;i++) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    TestLtaisZhengHua h = new TestLtaisZhengHua();
                    try {
                        h.testZhengHua();
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            });
            t.start();
        }
    }



}
