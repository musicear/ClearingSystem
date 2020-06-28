package resoft.junit;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-10-12
 * Time: 15:07:45
 * To change this template use File | Settings | File Templates.
 */
public class TestFileAppend {
    public static void main(String[] args) throws Exception {

        Runnable r = new Runnable() {

            /**
             * When an object implementing interface <code>Runnable</code> is used
             * to create a thread, starting the thread causes the object's
             * <code>run</code> method to be called in that separately executing
             * thread.
             * <p/>
             * The general contract of the method <code>run</code> is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            public void run() {
                write();
            }
        };
        for (int i = 0; i < 10000; i++) {
            Thread t = new Thread(r);
            t.start();
        }
        //Thread.sleep(10*1000);

    }

    private static synchronized void write() {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\a.txt", true);
            fos.write("Hello,world\r\n".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
