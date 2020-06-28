package resoft.junit;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-4-21
 * Time: 18:13:41
 */
public class TestServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(1200);
        Socket incoming = ss.accept();
        DataInputStream is = new DataInputStream(incoming.getInputStream());
        int length = is.readInt();
        System.out.println(length);
        incoming.close();
    }
}
