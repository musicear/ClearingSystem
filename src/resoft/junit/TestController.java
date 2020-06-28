package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Controller;

/**
 * function:
 * User: albert lee
 * Date: 2005-8-23
 * Time: 16:18:28
 */
public class TestController extends TestCase{
    public void testExecuteSingleTrans() {
        Controller controller = Controller.getInstance();
        String xml = "<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><交易码 val=\"1000\"/><预算级次 val=\"3\"/></MSG></CFX>";
        String returnValue = controller.execute(xml);
        String expactValue = "<CFX><HEAD><SRC>2000</SRC><DES>9999</DES><APP>财税</APP></HEAD><MSG><交易码 val=\"1000\"/><returnCode val=\"202\"/><returnMesg val=\"凭证号码须20位\"/></MSG></CFX>\r\n";
        assertEquals(expactValue,returnValue);
    }

    public void testExecuteWrongXml() {
        Controller controller = Controller.getInstance();
        String xml = "<CFX><MSG><交易码 val=\"1000\"/><预算级次 val=\"3\"/></MSG>";
        String returnValue = controller.execute(xml);
        String expactValue = "<CFX><MSG><响应码 val=\"905\"/><响应描述 val=\"数据包错误\"/></MSG></CFX>";
        assertEquals(expactValue,returnValue);
    }

    public void testExecuteTag() {
        Controller controller = Controller.getInstance();
        String xml = "1000@-@370282163945586@-@小二黑@-@0021@-@90206001020100057730@-@银行名称@-@00101@-@××××税收分局@-@13702821600@-@370205021260914@-@2004-12-1@-@2004-12-31@-@2005-1-7@-@37028201@-@12@-@隶属关系@-@202.22@-@2005-1-13@-@备注@-@101@-@010102@-@@-@1003@-@4001@-@0@-@@-@0.170000@-@87860.53@-@202.22@-@@-@@-@0@-@@-@0@-@0@-@0@-@@-@@-@0@-@@-@0@-@0@-@0@-@";
        String returnValue = controller.execute(xml);
    }
}
