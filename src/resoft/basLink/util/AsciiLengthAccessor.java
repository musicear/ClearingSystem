package resoft.basLink.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <p>6Î»ASCIIÂë³¤¶È</p>
 * Author: liguoyin
 * Date: 2007-8-4
 * Time: 12:47:54
 */
public class AsciiLengthAccessor implements LengthAccessor {

    public void write(OutputStream outputStream, int length) throws IOException {
        //outputStream.writeInt(length);
        NumberFormat nf = new DecimalFormat("000000");
        byte[] lenBytes = nf.format(length).getBytes();
        outputStream.write(lenBytes);
    }

    public int read(InputStream inputStream) throws IOException {
        byte[] lenBytes = new byte[6];
        int i = inputStream.read(lenBytes);

        return Integer.parseInt(new String(lenBytes));
    }
}