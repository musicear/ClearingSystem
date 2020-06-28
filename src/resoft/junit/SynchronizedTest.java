package resoft.junit;

import java.util.HashMap;
import java.util.Map;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-31
 * Time: 8:53:21
 */
public class SynchronizedTest {
    private Map map = new HashMap();
    public SynchronizedTest() {
        map.put("key","456");
    }
    private void foo() {
        synchronized(map) {
            System.out.println(Thread.currentThread() + "is running");

            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            map.put("key","123");
            System.out.println(Thread.currentThread() + "is running");
        }

    }
    private void foo1() {

            System.out.println(map.get("key"));


    }

    public static void main(String[] args) {
        final SynchronizedTest t = new SynchronizedTest();
        new Thread(new Runnable() {

            public void run() {
                t.foo();
            }
        }).start();
        new Thread(new Runnable() {

            public void run() {
                t.foo1();
            }
        }).start();
    }
}
