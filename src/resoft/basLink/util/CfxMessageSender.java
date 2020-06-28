package resoft.basLink.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;

/**
 * function: Cfx格式的Socket发送
 * User: albert lee
 * Date: 2005-10-27
 * Time: 17:28:00
 */
public class CfxMessageSender implements MessageSender {
    private static Log logger = LogFactory.getLog(CfxMessageSender.class);
    /**
     * IP地址
     */
    public static final String Property_Host = "host";
    /**
     * 端口
     */
    public static final String Property_Port = "port";
    /**
     * 解析格式定义文件
     */
    public static final String Property_FormatFile = "formatFile";

    /**
     * 设置Packager
     * */
    public static final String Property_Packager = "packager";

    /**
     * 设置LengthAccessor
     * */
    public static final String Property_LengthAccessor = "lengthAccessor";

    public void setProperty(String name, String value) {
        if (name.equals(Property_Host)) {
            host = value;
        } else if (name.equals(Property_Port)) {
            port = Integer.parseInt(value);
        } else if (name.equals(Property_FormatFile)) {
            forwardFormatFile = value;
        } else if(name.equals(Property_Packager)) {
            try {
                packager = (Packager) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("初始化Packager失败",e);
            }
        } else if(name.equals(Property_LengthAccessor)) {
            try {
                lengthAccessor = (LengthAccessor) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("初始化LengthAccessor失败",e);
            }
        }
    }

    public Message send(Message msg) throws MessageSendException {
        if(packager == null) {
            packager = new IDPackager();
        }

        byte[] packet;
        if (forwardFormatFile == null) {
            packet = packager.pack(msg);
        } else {
            packet = packager.pack(msg);
        }
        logger.info("发送报文：" + new String(packet));
        byte[] returnPacket = null;
        //开始发送
        Socket sender = null;
        try {
            sender = new Socket(host, port);
            sender.setSoTimeout(60 * 1000);
            DataOutputStream output = new DataOutputStream(sender.getOutputStream());
            int len = packet.length;

            lengthAccessor.write(output,len);

            //output.writeInt(packet.length);
            output.write(packet);
            output.flush();
            //packet = null;
            //接收响应
            DataInputStream input = new DataInputStream(sender.getInputStream());

            int length = lengthAccessor.read(input);
            returnPacket = new byte[length];
            input.read(returnPacket, 0, length);
            logger.info("接收报文:" + new String(returnPacket));
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

    private String host = "";
    private int port = 0;
    private String forwardFormatFile = null;
    private Packager packager;
    private LengthAccessor lengthAccessor = new AsciiLengthAccessor();
}
