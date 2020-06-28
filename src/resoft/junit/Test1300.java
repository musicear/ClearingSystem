package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;

/**
 * function: 测试退库
 * User: albert lee
 * Date: 2005-9-27
 * Time: 13:14:09
 */
public class Test1300 extends TestCase{
    public void testTuiKu() throws Exception{
        Message msg = new Message();
        msg.setValue("srthsh","21300111000656006219");
        msg.setValue("kpJe","1554.41");
        msg.setValue("yhzh","1001134457694456");
        msg.setValue("zsjgDm","23701040000");
        msg.setValue("tkgkDm","23701040");
        msg.setValue("pzzlDm","411");
        msg.setValue("kpRq","2005-09-01");
        msg.setValue("nsrsbh","23701020000");
        msg.setValue("nsrmc","dsfsdf");
        msg.setValue("djzclxDm","12144");
        msg.setValue("yhzlDm","0011");
        msg.setValue("yskmDm","8408");
        msg.setValue("ysfpblDm","001090");
        msg.setValue("ttlxDm","20");
        msg.setValue("zsxmDm1","203");
        msg.setValue("zspmDm1","0103");
        msg.setValue("ttSe1","1554.41");


        Message returnMsg = BasLinkClient.send("127.0.0.1",1200,"1300",msg);
        assertEquals("000",returnMsg.getValue("returnCode"));
        assertEquals("交易成功",returnMsg.getValue("returnMesg"));
        //System.out.println(returnMsg);
    }


}
