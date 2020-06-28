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
public class Test1400 extends TestCase{
    public void testGengZheng() throws Exception{
        Message msg = new Message();
        msg.setValue("tzpzxh","21387111474656006219");
        msg.setValue("a","2005-09-11");
        msg.setValue("skgkDm","23701030");
        msg.setValue("xtspHm","34233876590118824311");
        msg.setValue("se","35000.00");
        msg.setValue("ySkssSwjgDm","23790000000");
        msg.setValue("yYskmDm","1501");
        msg.setValue("yYsfpblDm","2370102301");
        msg.setValue("xSkssSwjgDm","23790000000");
        msg.setValue("xYskmDm","1501");
        msg.setValue("xYsfpblDm","2370102301");

        Message returnMsg = BasLinkClient.send("127.0.0.1",1200,"1400",msg);
        assertEquals("000",returnMsg.getValue("returnCode"));
        assertEquals("交易成功",returnMsg.getValue("returnMesg"));
        //System.out.println(returnMsg);
    }


}
