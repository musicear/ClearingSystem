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
 * function: Cfx��ʽ��Socket����
 * User: albert lee
 * Date: 2005-10-27
 * Time: 17:28:00
 */
public class CfxMessageSender implements MessageSender {
    private static Log logger = LogFactory.getLog(CfxMessageSender.class);
    /**
     * IP��ַ
     */
    public static final String Property_Host = "host";
    /**
     * �˿�
     */
    public static final String Property_Port = "port";
    /**
     * ������ʽ�����ļ�
     */
    public static final String Property_FormatFile = "formatFile";

    /**
     * ����Packager
     * */
    public static final String Property_Packager = "packager";

    /**
     * ����LengthAccessor
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
                logger.error("��ʼ��Packagerʧ��",e);
            }
        } else if(name.equals(Property_LengthAccessor)) {
            try {
                lengthAccessor = (LengthAccessor) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("��ʼ��LengthAccessorʧ��",e);
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
        logger.info("���ͱ��ģ�" + new String(packet));
        byte[] returnPacket = null;
        //��ʼ����
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
            //������Ӧ
            DataInputStream input = new DataInputStream(sender.getInputStream());

            int length = lengthAccessor.read(input);
            returnPacket = new byte[length];
            input.read(returnPacket, 0, length);
            logger.info("���ձ���:" + new String(returnPacket));
        } catch (IOException e) {
            throw new MessageSendException(e);
        } finally {
            //�ر�����
            if (sender != null) {
                try {
                    sender.close();
                } catch (IOException e) {
                }
            }
        }
        //תΪMessage
        return packager.unpack(returnPacket);
    }

    private String host = "";
    private int port = 0;
    private String forwardFormatFile = null;
    private Packager packager;
    private LengthAccessor lengthAccessor = new AsciiLengthAccessor();
}
