package resoft.xlink.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>通讯层协议。读取、写入请求数据</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:52:19
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
