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
     * IP��ַ
     */
    public static final String Property_Host = "host";
    /**
     * �˿�
     */
    public static final String Property_Port = "port";
    /**
     * ���������
     */
    private static final String Property_Packager = "packager";
    /**
     * ����������
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
                logger.error("������:" + value + "ʧ��", e);
            }
        } else if (name.equalsIgnoreCase(Property_LengthAccessor)) {
            try {
                lengthAccessor = (LengthAccessor) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("������:" + value + "ʧ��", e);
            }
        }
    }

    public Message send(Message msg) throws MessageSendException {
        byte[] packet;
        packet = packager.pack(msg);

        logger.info("���ͱ��ģ�" + new String(packet));
        byte[] returnPacket = new byte[0];
        //��ʼ����
        Socket sender = null;
        try {
            sender = new Socket(host, port);
            sender.setSoTimeout(60 * 1000);
            DataOutputStream output = new DataOutputStream(sender.getOutputStream());
            lengthAccessor.write(output, packet.length);
            output.write(packet);
            output.flush();
            //������Ӧ
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

            logger.info("���ձ���:" + new String(returnPacket, "GBK"));
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
