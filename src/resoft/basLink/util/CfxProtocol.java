package resoft.basLink.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Protocol;
import resoft.xlink.comm.helper.ThreadLocalContext;
import resoft.xlink.comm.impl.GenericXmlPackager;

/**
 * function: Cfx通讯协议。报文前加4位长度。报文为xml格式
 * User: albert lee
 * Date: 2005-10-28
 * Time: 15:48:27
 * changeLog:2007/5/20 长度改为用6位ascii码的整数来表示
 */
public class CfxProtocol implements Protocol {
    private static Log logger = LogFactory.getLog(CfxProtocol.class);
    private static final int MaxBufferSize = 10000;
    public boolean isKeepAlive() {
        return false;
    }

    public byte[] read(InputStream inputStream) throws IOException {
        DataInputStream input = new DataInputStream(inputStream);
        //读取长度
        byte[] lenBytes = new byte[6];
        input.read(lenBytes);

        int length = Integer.parseInt(new String(lenBytes));
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

        ThreadLocalContext.getInstance().getContext().setPacker(new GenericXmlPackager());//@todo 如何用多个Packager
        logger.info("接收客户端内容:" + reqStr);

        return reqBytes;
    }

    public void write(OutputStream output, byte[] bytes) throws IOException {
        //DataOutputStream output = new DataOutputStream(outputStream);
        //
//        byte[] a = {0x00,0x00,0x00,0x10};
//        output.write(a);

        //

        int len = bytes.length;
//        byte[] lenBytes = new byte[4];
//        lenBytes[0] = (byte) (len>>>24);
//        lenBytes[1] = (byte) (len>>>16);
//        lenBytes[2] = (byte) (len>>>8);
//        lenBytes[3] = (byte) len;
        NumberFormat nf = new DecimalFormat("000000");
        byte[] lenBytes = nf.format(len).getBytes();
        output.write(lenBytes);
//        output.write((len >>> 24) & 0xFF);
//        output.write((len >>> 16) & 0xFF);
//        output.write((len >>>  8) & 0xFF);
//        output.write((len >>>  0) & 0xFF);

        //output.write(bytes.length);
        output.flush();
        output.write(bytes);
        output.flush();
        output.close();
        logger.info("返回客户端报文:" + new String(bytes));
    }
}
