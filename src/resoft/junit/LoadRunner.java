package resoft.junit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * <p>function:</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-11-30</p>
 * <p>Time: 20:24:00</p>
 */
public class LoadRunner {
    public static void main(String[] args) {
        int threads = 10;
        for(int i=0;i<threads;i++) {
            Thread t = new Thread(new TestThread());
            t.start();
        }

    }
    static class TestThread implements Runnable {
        private static int times = 0;
        public void run() {
            while(true) {
                try {
                    String a = "<CFX><MSG></MSG></CFX>";
                    byte[] xml = a.getBytes();
                    Socket socketClient = new Socket("127.0.0.1",1200);
                    DataOutputStream os = new DataOutputStream(socketClient.getOutputStream());
                    //发送长度
                    os.writeInt(xml.length);
                    //报文内容
                    os.write(xml);
                    os.flush();
                    //读取响应
                    DataInputStream is = new DataInputStream(socketClient.getInputStream());
                    int len = is.readInt();
                    byte[] returnData = new byte[len];
                    is.read(returnData,0,len);
                    is.close();
                    socketClient.close();
                    System.out.println(new String(returnData));
                    printTimes();
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }


        }
        private void printTimes() {
            synchronized(lock) {
                times ++;
            }
            System.out.println("发送共计:" + times);
        }
        private Object lock = new Object();
    }
}
