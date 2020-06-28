package resoft.basLink.monitor;

import java.util.Map;
import java.util.TreeMap;

import resoft.basLink.Message;
import resoft.basLink.Packer;
import resoft.basLink.packer.CfxPacker;

/**
 * function: ͨѶ���
 * User: albert lee
 * Date: 2005-9-14
 * Time: 11:21:03
 */
public class Monitor {
    private Monitor() {
        packer = new CfxPacker();
    }
    /**
     * �õ���Ψһʵ��
     * */
    public static Monitor getInstance() {
        return instance;
    }
    /**
     * ���������Ϣ
     * */
    public void clear() {
        recentMsgs.clear();
    }
    /**
     * ������Ϣ
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
     * �õ����µı�����Ϣ
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
