package resoft.tips.bankImpl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-4
 * Time: 12:21:46
 */
public class SyncSocketMessageSender implements MessageSender {
    private static Log logger = LogFactory.getLog(SyncSocketMessageSender.class);
    /**
     * IP地址
     */
    public static final String Property_Host = "host";
    /**
     * 端口
     */
    public static final String Property_Port = "port";
    /**
     * 解包处理类
     */
    private static final String Property_Packager = "packager";
    /**
     * 包长处理类
     */
    private static final String Property_LengthAccessor = "lengthAccessor";


    public void setProperty(String name, String value) {
        if (name.equals(Property_Host)) {
            host = value;
        } else if (name.equalsIgnoreCase(Property_Port)) {
            port = Integer.parseInt(value);
        } else if (name.equalsIgnoreCase(Property_Packager)) {
            try {
                packager = (Packager) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("加载类:" + value + "失败", e);
            }
        } else if (name.equalsIgnoreCase(Property_LengthAccessor)) {
            try {
                lengthAccessor = (LengthAccessor) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("加载类:" + value + "失败", e);
            }
        }
    }

    public Message send(Message msg) throws MessageSendException {
        byte[] packet;
        packet = packager.pack(msg);

        logger.info("发送报文：" + new String(packet));
        byte[] returnPacket = new byte[0];
        //开始发送
        Socket sender = null;
        try {
            sender = new Socket(host, port);
            sender.setSoTimeout(60 * 1000);
            DataOutputStream output = new DataOutputStream(sender.getOutputStream());
            lengthAccessor.write(output, packet.length);
            output.write(packet);
            output.flush();
            //接收响应
            InputStream input = sender.getInputStream();

//            returnPacket = new byte[10000];
//            input.read(returnPacket);


            //loop rad
            while (true) {
                int length = 0;
                try {
                    length = lengthAccessor.read(input);
                } catch (IOException lenEx) {
                }
                if (length <= 0) {
                    break;
                }
                byte[] tempPacket = new byte[length];
                input.read(tempPacket, 0, length);

                returnPacket = merge(returnPacket, tempPacket);

            }

            logger.info("接收报文:" + new String(returnPacket, "GBK"));
        } catch (IOException e) {
            throw new MessageSendException(e);
        } finally {
            //关闭连接
            if (sender != null) {
                try {
                    sender.close();
                } catch (IOException e) {
                }
            }
        }
        //转为Message
        return packager.unpack(returnPacket);
    }

    private byte[] merge(byte[] first, byte[] second) {
        byte[] swapBytes = new byte[first.length + second.length];
        System.arraycopy(first, 0, swapBytes, 0, first.length);
        System.arraycopy(second, 0, swapBytes, first.length, second.length);
        return swapBytes;
    }

    private String host = "";
    private int port = 0;
    private Packager packager = null;
    private LengthAccessor lengthAccessor;
}
