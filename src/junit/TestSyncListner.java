package junit;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;

import junit.framework.TestCase;
import resoft.basLink.util.DBUtil;
import resoft.tips.mq.SyncListener;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 22:31:45
 */
public class TestSyncListner extends TestCase {
    public void test3001() throws Exception{
    	Connection conn = DBUtil.getNewConnection();  	
    	resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
    	String xml = readFile("E:\\飞鸽\\测试报文\\TIPS转发实时扣税请求(3001).xml");
    	SyncListener listener=new SyncListener();
        listener.onMessage(xml.getBytes(),null);
    }

    private String readFile(String name) throws IOException {
        StringBuffer content = new StringBuffer();
        LineNumberReader lnr = new LineNumberReader(new FileReader(name));
        String line = lnr.readLine();
        while(line!=null) {
            content.append(line);
            line = lnr.readLine();
        }
        return content.toString();
    }
}
