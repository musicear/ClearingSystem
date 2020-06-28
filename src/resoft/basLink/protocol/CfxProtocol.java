package resoft.basLink.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Protocol;
import resoft.basLink.ThreadLocalContext;
import resoft.basLink.packer.CfxPacker;

/**
 * function: CfxͨѶЭ�顣����ǰ��4λ���ȡ�����Ϊxml��ʽ
 * User: albert lee
 * Date: 2005-10-28
 * Time: 15:48:27
 */
public class CfxProtocol implements Protocol{
    private static Log logger = LogFactory.getLog(CfxProtocol.class);
    private static final int MaxBufferSize = 10000;
    public boolean isKeepAlive() {
        return false;
    }

    public byte[] read(InputStream inputStream) throws IOException {
        DataInputStream input = new DataInputStream(inputStream);
        //��ȡ����
        int length = input.readInt();
        logger.info("��ȡ����Ϊ" + length);
        length = length>=MaxBufferSize? 0 : length;
        //��ȡ����
        byte[] reqBytes = new byte[length];

        int position = 0;
        while(position<length) {
            int readLen = input.read(reqBytes,position,length - position);
            if(readLen==-1) {
                break;
            }
            position += readLen;
        }

        //��ӡ��־
        String reqStr = new String(reqBytes);

        ThreadLocalContext.getInstance().getContext().setPacker(new CfxPacker());
        logger.info("���տͻ�������:" + reqStr);

        return reqBytes;
    }

    public void write(OutputStream output, byte[] bytes) throws IOException {
        //DataOutputStream output = new DataOutputStream(outputStream);
        //
//        byte[] a = {0x00,0x00,0x00,0x10};
//        output.write(a);

        //

        int len = bytes.length;
        byte[] lenBytes = new byte[4];
        lenBytes[0] = (byte) (len>>>24);
        lenBytes[1] = (byte) (len>>>16);
        lenBytes[2] = (byte) (len>>>8);
        lenBytes[3] = (byte) len;
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
        logger.info("���ؿͻ��˱���:" + new String(bytes));
    }
}
