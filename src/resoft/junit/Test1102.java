package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;

/**
 * function: 测试正划撤销
 * User: zhangzhiqiang
 * Date: 2005-9-27
 * Time: 13:14:09
 */
public class Test1102 extends TestCase{
    public void test1102() throws Exception{
        Message msg = new Message();
        msg.setValue("xtspHm","11119876590987654387");
        msg.setValue("kpJe","35000.00");

        Message returnMsg = BasLinkClient.send("127.0.0.1",1200,"1102",msg);
        assertEquals("000",returnMsg.getValue("returnCode"));
        assertEquals("交易成功",returnMsg.getValue("returnMesg"));
        //System.out.println(returnMsg);
    }

   /* public static void main(String[] args) {

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
}*/
}
