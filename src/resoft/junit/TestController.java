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
        String xml = "<CFX><HEAD><SRC>1000</SRC></HEAD><MSG><������ val=\"1000\"/><Ԥ�㼶�� val=\"3\"/></MSG></CFX>";
        String returnValue = controller.execute(xml);
        String expactValue = "<CFX><HEAD><SRC>2000</SRC><DES>9999</DES><APP>��˰</APP></HEAD><MSG><������ val=\"1000\"/><returnCode val=\"202\"/><returnMesg val=\"ƾ֤������20λ\"/></MSG></CFX>\r\n";
        assertEquals(expactValue,returnValue);
    }

    public void testExecuteWrongXml() {
        Controller controller = Controller.getInstance();
        String xml = "<CFX><MSG><������ val=\"1000\"/><Ԥ�㼶�� val=\"3\"/></MSG>";
        String returnValue = controller.execute(xml);
        String expactValue = "<CFX><MSG><��Ӧ�� val=\"905\"/><��Ӧ���� val=\"���ݰ�����\"/></MSG></CFX>";
        assertEquals(expactValue,returnValue);
    }

    public void testExecuteTag() {
        Controller controller = Controller.getInstance();
        String xml = "1000@-@370282163945586@-@С����@-@0021@-@90206001020100057730@-@��������@-@00101@-@��������˰�շ־�@-@13702821600@-@370205021260914@-@2004-12-1@-@2004-12-31@-@2005-1-7@-@37028201@-@12@-@������ϵ@-@202.22@-@2005-1-13@-@��ע@-@101@-@010102@-@@-@1003@-@4001@-@0@-@@-@0.170000@-@87860.53@-@202.22@-@@-@@-@0@-@@-@0@-@0@-@0@-@@-@@-@0@-@@-@0@-@0@-@0@-@";
        String returnValue = controller.execute(xml);
    }
}
