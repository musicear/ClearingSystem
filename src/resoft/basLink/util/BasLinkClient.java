package resoft.basLink.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import resoft.basLink.Message;
import resoft.basLink.Packer;
import resoft.basLink.packer.CfxPacker;

/**
 * function: BasLink�ͻ��ˡ����ڷ�������
 * User: albert lee
 * Date: 2005-9-21
 * Time: 16:02:06
 */
public class BasLinkClient {
    private static Packer packer = new CfxPacker();
    public static Message send(String transCode,Message data) throws IOException {
        return send("127.0.0.1",1200,transCode,data);
    }
    public static Message send(String host,int port,String transCode,Message data) throws IOException {
        data.setValue("������",transCode);
        byte[] xml = packer.unpack(data);

        Socket socketClient = new Socket(host,port);
        DataOutputStream os = new DataOutputStream(socketClient.getOutputStream());
        //���ͳ���
        os.writeInt(xml.length);
        //��������
        os.write(xml);
        os.flush();
        //��ȡ��Ӧ
        DataInputStream is = new DataInputStream(socketClient.getInputStream());
        int len = is.readInt();
        byte[] returnData = new byte[len];
        is.read(returnData,0,len);
        is.close();
        socketClient.close();        
        Message returnMsg = packer.pack(new String(returnData).getBytes());
        return returnMsg;
    }

    public static Map send(String host,int port,String transCode,Map map) throws IOException {
        Message msg = new Message();
        msg.putAll(map);
        Message returnMsg = send(host,port,transCode,msg);
        Map returnMap = new HashMap();
        returnMap.put("returnCode",returnMsg.getValue("returnCode"));
        returnMap.put("returnMesg",returnMsg.getValue("returnMesg"));
        returnMap.put("slRq",returnMsg.getValue("slRq"));
        return returnMap;
    }
}
