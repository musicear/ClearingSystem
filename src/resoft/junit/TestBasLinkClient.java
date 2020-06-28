package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-21
 * Time: 15:58:30
 */
public class TestBasLinkClient extends TestCase{
    public void testSend() throws Exception{
        Message msg = new Message();
        msg.setValue("������","9999");
        Message returnData = BasLinkClient.send("1200",msg);
        assertNotNull(returnData);
        assertEquals(returnData.getValue("��Ӧ��"),"905");
        assertEquals(returnData.getValue("��Ӧ����"),"���ݰ�����");
    }
}
