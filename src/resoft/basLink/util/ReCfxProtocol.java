package resoft.basLink.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Protocol;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-7-5
 * Time: 11:32:44
 */
public class ReCfxProtocol implements Protocol {
    private static Log logger = LogFactory.getLog(ReCfxProtocol.class);
    private static final int MaxBufferSize = 10000;
    public ReCfxProtocol(LengthAccessor lengthAccessor) {
        this.lengthAccessor = lengthAccessor;
    }

    public boolean isKeepAlive() {
        return false;
    }

    public byte[] read(InputStream inputStream) throws IOException {
        DataInputStream input = new DataInputStream(inputStream);
        //读取长度
        //int length = input.readInt();
        int length = lengthAccessor.read(input);
        logger.info("读取长度为" + length);
        length = length>=MaxBufferSize? 0 : length;
        //读取报文
        byte[] reqBytes = new byte[length];

        int position = 0;
        while(position<length) {
            int readLen = input.read(reqBytes,position,length - position);
            if(readLen==-1) {
                break;
            }
            position += readLen;
        }

        //打印日志
        String reqStr = new String(reqBytes);

        logger.info("接收客户端内容:" + reqStr);

        return reqBytes;
    }

    public void write(OutputStream output, byte[] bytes) throws IOException {

        int len = bytes.length;
//        byte[] lenBytes = new byte[4];
//        lenBytes[0] = (byte) (len>>>24);
//        lenBytes[1] = (byte) (len>>>16);
//        lenBytes[2] = (byte) (len>>>8);
//        lenBytes[3] = (byte) len;
//        output.write(lenBytes);
        lengthAccessor.write(new DataOutputStream(output),len);

        //output.write(bytes.length);
        output.flush();
        output.write(bytes);
        output.flush();
        output.close();
        logger.info("返回客户端报文:" + new String(bytes));
    }

    private LengthAccessor lengthAccessor;
}
