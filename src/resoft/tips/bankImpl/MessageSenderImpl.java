package resoft.tips.bankImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.CfxPackager;
import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;

/**
 * <p>Ȫ����ҵ���������ϵͳͨѶ�ӿ�</p>
 * Author: liguoyin
 * Date: 2007-7-19
 * Time: 16:38:37
 */
public class MessageSenderImpl implements MessageSender {
    private static Log logger = LogFactory.getLog(MessageSenderImpl.class);
    /**
     * IP��ַ
     * */
    public static final String Property_Host = "host";
    /**
     * �˿�
     * */
    public static final String Property_Port = "port";
    /**
     * ������ʽ�����ļ�
     * */
    public static final String Property_FormatFile = "formatFile";

    public void setProperty(String name, String value) {
        if(name.equals(Property_Host)) {
            host = value;
        } else if(name.equals(Property_Port)) {
            port = Integer.parseInt(value);
        } else if(name.equals(Property_FormatFile)) {
            forwardFormatFile = value;
        }
    }

    public Message send(Message msg) throws MessageSendException {
        Packager packer = new CfxPackager();
        byte[] packet = null;
        if(forwardFormatFile==null) {
            packet = packer.pack(msg);
        } else {
            //packet = packer.unpackByFile(msg,forwardFormatFile);
        }
        logger.info("���ͱ��ģ�" + new String(packet));
        byte[] returnPacket = null;
        //��ʼ����
        Socket sender = null;
        try {
            sender = new Socket(host,port);
            sender.setSoTimeout(60 * 1000);
            DataOutputStream output = new DataOutputStream(sender.getOutputStream());
            output.writeInt(packet.length);
            output.write(packet);
            output.flush();
            packet = null;
            //������Ӧ
            DataInputStream input = new DataInputStream(sender.getInputStream());
            int length = input.readInt();
            returnPacket = new byte[length];
            input.read(returnPacket,0,length);
            logger.info("���ձ���:" + new String(returnPacket));
        } catch(IOException e) {
            throw new MessageSendException(e);
        } finally {
            //�ر�����
            if(sender!=null) {
                try {
                    sender.close();
                } catch (IOException e) {
                }
            }
        }
        //תΪMessage
        Message returnMsg = packer.unpack(returnPacket);
        return returnMsg;
    }

    private String host = "";
    private int port = 0;
    private String forwardFormatFile = null;
}
