package resoft.basLink.monitor;

import java.util.Map;
import java.util.TreeMap;

import resoft.basLink.Message;
import resoft.basLink.Packer;
import resoft.basLink.packer.CfxPacker;

/**
 * function: 通讯监控
 * User: albert lee
 * Date: 2005-9-14
 * Time: 11:21:03
 */
public class Monitor {
    private Monitor() {
        packer = new CfxPacker();
    }
    /**
     * 得到其唯一实例
     * */
    public static Monitor getInstance() {
        return instance;
    }
    /**
     * 清空所有消息
     * */
    public void clear() {
        recentMsgs.clear();
    }
    /**
     * 放入消息
     * */
    public void putMessage(Message msg) {
        String xml = new String(packer.unpackByFile(msg,"monitor.xml"));
        synchronized(lock) {
            num ++;
            if(recentMsgs.size()>=capacity) {
                recentMsgs.remove(new Integer(num - capacity));
            }
            recentMsgs.put(new Integer(num),xml);
        }
    }

    /**
     * 得到最新的报文信息
     * */
    public Map getRecentMsgs() {
        return recentMsgs;
    }

    private static Monitor instance = new Monitor();
    private Packer packer;
    private Map recentMsgs = new TreeMap();
    private int num = 0;
    private static final int capacity = 20;

    private final Object lock = new Object();
}
