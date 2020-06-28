package resoft.junit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <p>function:</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-11-13</p>
 * <p>Time: 11:23:31</p>
 */
public class DESTest {
    public static void main(String[] args) throws Exception{
        byte[] desKey = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        DESKeySpec desKS = new DESKeySpec(desKey);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(desKS);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE,sk);
        byte[] b = cipher.doFinal("Hello,00abc".getBytes());
        byte a = 'T';
        System.out.println();
        System.out.println(new String(decrypt(b,desKey)));

        //得到密钥存在的端口
        Socket s = new Socket("192.168.110.118",8000);
        byte[] sendBuffer = {0x01,(byte) 0xFF};
        DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
        outputStream.write(sendBuffer);
        outputStream.flush();
        DataInputStream inputStream = new DataInputStream(s.getInputStream());
        byte[] buffer = new byte[8];
        inputStream.read(buffer);
        System.out.println();
        s.close();
        //
        //Get local key
//        InputStream is = new FileInputStream("c:\\sysdevice.C");
//        byte[] locakKey_time = new byte[16];
//        is.read(locakKey_time,0,8);
//        byte[] time = decrypt("ABCDEFGD".getBytes(),"NewGroup".getBytes());
        System.out.println(buffer[3] * 256 + buffer[4]);
        s = new Socket("192.168.110.118",buffer[3] * 256 + buffer[4]);
        outputStream = new DataOutputStream(s.getOutputStream());
        byte[] cc = {0x00,0x00,0x00,0x0C,(byte) 0xe7,(byte) 0xf1,(byte) 0xf0,(byte) 0xf2,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01};
        outputStream.write(cc);
        outputStream.flush();
        inputStream = new DataInputStream(s.getInputStream());
        byte[] dd= new byte[256];
        System.out.println(inputStream.read(dd));
        s.close();



    }

    public static byte[] decrypt(byte[] buffer,byte[] key) throws Exception{
        DESKeySpec desKS = new DESKeySpec(key);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(desKS);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE,sk);

        return cipher.doFinal(buffer);
    }
}
