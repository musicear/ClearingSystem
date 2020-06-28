package resoft.junit;

import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import resoft.basLink.Controller;
import resoft.basLink.monitor.Monitor;

/**
 * function: 测试监控
 * User: albert lee
 * Date: 2005-9-15
 * Time: 16:57:01
 */
public class TestMonitor extends TestCase{
    public void testGetRecentMsg() {
        Monitor.getInstance().clear();
        Controller.getInstance().execute("<CFX><MSG><交易码 val=\"1000\"/><流水号 val=\"1\"/></MSG></CFX>");
        Controller.getInstance().execute("<CFX><MSG><交易码 val=\"1000\"/><流水号 val=\"2\"/></MSG></CFX>");
        Controller.getInstance().execute("<CFX><MSG><交易码 val=\"1000\"/><流水号 val=\"3\"/></MSG></CFX>");
        Map msg = Monitor.getInstance().getRecentMsgs();

        for(Iterator itr = msg.values().iterator();itr.hasNext();) {
            System.out.println(itr.next());
        }
        assertEquals(3,msg.size());

        //System.out.println(Monitor.getInstance().getRecentMsgs());
    }
}
