package resoft.basLink.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>二进制长度</p>
 * Author: liguoyin
 * Date: 2007-8-4
 * Time: 12:47:54
 */
public class RawLengthAccessor implements LengthAccessor {

    public void write(OutputStream outputStream, int length) throws IOException {
        new DataOutputStream(outputStream).writeInt(length);
    }

    public int read(InputStream inputStream) throws IOException {
        return new DataInputStream(inputStream).readInt();
    }
}
