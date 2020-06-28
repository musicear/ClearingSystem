package resoft.junit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.basLink.Packer;
import resoft.basLink.packer.CfxPacker;

/**
 * 功能描述
 *
 * @author Albert Li
 *         Date: 2007-2-27
 *         Time: 15:53:45
 */
public class TestLFBasLink {
    private static final Log logger = LogFactory.getLog(TestLFBasLink.class);
    private static Packer packer = new CfxPacker();

    public static Message send(String host, int port, String transCode, Message data) throws IOException {
        data.setValue("交易码", transCode);
        byte[] xml = packer.unpack(data);

        Socket socketClient = new Socket(host, port);
        DataOutputStream os = new DataOutputStream(socketClient.getOutputStream());
        //发送长度
        int length = xml.length;
        //NumberFormat nf = new DecimalFormat("000000");
        os.write(new Integer(length).toString().getBytes());
        //os.write("    ".getBytes());
        //报文内容
        os.write(xml);
        //logger.info(Thread.currentThread().toString() + "发送内容:" + new String(xml));
        os.flush();
        //读取响应
        DataInputStream is = new DataInputStream(socketClient.getInputStream());
        byte[] returnLength = new byte[6];
        is.read(returnLength);
        int len = Integer.parseInt(new String(returnLength).trim());
        byte[] returnData = new byte[len];
        is.read(returnData, 0, len);
        is.close();
        socketClient.close();
        //logger.info(Thread.currentThread().toString() + "响应报文:" + new String(returnData));
        Message returnMsg = packer.pack(new String(returnData).getBytes());
        return returnMsg;
    }

    public static Map send(String host, int port, String transCode, Map map) throws IOException {
        Message msg = new Message();
        msg.putAll(map);
        Message returnMsg = send(host, port, transCode, msg);
        Map returnMap = new HashMap();
        returnMap.put("returnCode", returnMsg.getValue("returnCode"));
        returnMap.put("returnMesg", returnMsg.getValue("returnMesg"));
        returnMap.put("slRq", returnMsg.getValue("slRq"));
        return returnMap;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new SendThread());
            t.start();
        }

//        Thread t1 = new Thread(new SendThread());
//        t1.start();
//        Thread t2 = new Thread(new SendThread());
//        t2.start();
    }

    static class SendThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    TestLFBasLink.send("139.56.225.80", 1200, "1150", new HashMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
