package resoft.basLink.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>��ȡ��д�뱨��ͷ����</p>
 * Author: liguoyin
 * Date: 2007-8-4
 * Time: 12:45:14
 */
public interface LengthAccessor {
    public void write(OutputStream outputStream,int length) throws IOException;
    public int read(InputStream inputStream) throws IOException;
}

