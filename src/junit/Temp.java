package junit;

import java.io.UnsupportedEncodingException;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-9-1
 * Time: 3:45:40
 */
public class Temp {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "–≈œ¢L";
        byte[] bytes = str.getBytes("UTF-16");
        for(int i = 0;i<bytes.length;i++) {
            System.out.println(bytes[i] & 0xFF);
        }
    }
}
