package junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import resoft.tips.action.SendToMQ;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>与仿真测试</p>
 * Author: liwei
 * Date: 2008-5-23
 * Time: 4:07:17
 */ 
public class TestSendMQFangZhen { 
    public static void main(String[] args) throws IOException {
    	
        SendToMQ action = new SendToMQ();
        action.setQueueManagerName("QM_TIPSEMLU");        
        action.setQueueName("PBC.402100000010.BATCH.OUT");
        
        //action.setQueueManagerName("QM_TIPS_613220000026_01");
        //action.setQueueName("PBC.613220000026.BATCH.OUT");
        
        Message msg = new DefaultMessage(); 
        
        //File f=new File("D:\\3102.xml");        
        //File f=new File("c:\\temp\\test\\5103.xml");
        //File f=new File("c:\\temp\\test\\5102.xml");
        File f=new File("c:\\temp\\test\\3143.xml");
        
        byte[] bytes = new byte[(int) f.length()];
        InputStream is = new FileInputStream(f);
        is.read(bytes);
        msg.set("报文内容",new String(bytes));
        msg.set("strEncData",new String(bytes));
        msg.set("correlationId","20070710064910634739");
        msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
        msg.set("//CFX/HEAD/MsgID","123");
        msg.set("//CFX/HEAD/MsgNo","123");
        action.execute(msg);

    }
}
