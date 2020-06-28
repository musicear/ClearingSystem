package resoft.junit.testTransactionFlow;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-9-29
 * Time: 11:43:38
 * To change this template use File | Settings | File Templates.
 */
public class TestCharset {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("บบ".getBytes().length);
        byte[] array = "บบ".getBytes();
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        String s = "บบ";
        array = s.getBytes("UTF-8");
        System.out.println(array.length);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        String u = new String(array,"UTF-8");

        System.out.println(u);
    }
}
