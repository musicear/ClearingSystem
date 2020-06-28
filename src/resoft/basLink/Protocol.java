package resoft.basLink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * function: 协议。读取请求数据
 * User: albert lee
 * Date: 2005-10-18
 * Time: 14:14:35
 */
public interface Protocol {
    /**
     * 是否为长连接
     * */
    public boolean isKeepAlive();
    /**
     * 不同协议读取方式不同，有的用终结符，有的是定长的，有的是在报文前带一长度
     * */
    public byte[] read(InputStream inputStream) throws IOException;
    /**
     * 写响应回客户端
     * */
    public void write(OutputStream outputStream,byte[] buffer) throws IOException;
}
