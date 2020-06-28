package resoft.junit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * function:
 * User: albert lee
 * Date: 2005-8-23
 * Time: 17:03:47
 */
public class SocketClientTest {
    public static void main(String[] args) throws Exception{
        System.out.println(Math.random());

        for(int j = 0;j<10;j++) {
            for(int i=0;i<100;i++) {
                Thread t = new Thread(new T());
                t.start();
            }
            Thread.sleep(60 * 1000);
        }








    }
}

class T implements Runnable {

    public void run() {
        Socket client = null;
        try {
            client = new Socket("166.168.1.16",1200);
            //59.83.176.89

            DataOutputStream output = new DataOutputStream(client.getOutputStream());

            //String str = "<CFX><MSG><������ val=\"1000\"/><Ԥ�㼶�� val=\"3\"/></MSG></CFX>";
            String str = "<CFX><MSG><nsrsbh val=\"370206614304893\"/><nsrmc val=\"С����\"/><yhMc val=\"��������\"/><yhzh val=\"3803026019200000808\"/><pzzlDm val=\"411\"/><zsjgDm val=\"23701020000\"/><xtspHm val=\"";
            String xtSphm = Math.random() + "00";
            str += xtSphm;
            str += "\"/><sssqQ val=\"2004-12-1\"/><sssqZ val=\"2004-12-31\"/><tfRq val=\"2005-1-17\"/><skgkDm val=\"37020601\"/><hyDm val=\"0909\"/><hyMc val=\"�ɾ�ҵ\"/><tkgkDm val=\"23701020\"/><yhzlDm val=\"0011\"/><ttlxDm1 val=\"10\"/><yskmDm val=\"010120\"/><zsxmDm val=\"101\"/><djzclxDm val=\"12\"/><djzclxMc val=\"12\"/><kpJe val=\"6.66\"/><xjRq val=\"2005-1-17\"/><bz val=\"��ע\"/><sbfsDm val=\"01\"/><zsxmDm1 val=\"101\"/><yskmDm1 val=\"010120\"/><ysfpblDm val=\"001090\"/><zspmDm1 val=\"9900\"/><kssl1 val=\"0\"/><xssr1 val=\"\"/><sl1 val=\"0.000500\"/><yjkce1 val=\"0\"/><sjSe1 val=\"6.66\"/><zsxmDm2 val=\"\"/><yskmDm2 val=\"\"/><ysfpblDm2 val=\"7809\"/><zspmDm2 val=\"\"/><kssl2 val=\"\"/><xssr2 val=\"\"/><sl2 val=\"\"/><yjkce2 val=\"\"/><sjSe2 val=\"\"/><������ val=\"1300\"/></MSG></CFX>";

            //String str = "��";
            output.writeInt(str.getBytes().length);
            output.write(str.getBytes());

            //output.write("\"GB18030\"?><CFX><MSG><������ val=\"1000\"/><Ԥ�㼶�� val=\"3\"/></MSG></CFX>".getBytes());
            //output.write("1000@-@/*��˰��ʶ���*/@-@/*��˰������*/@-@/*�����������*/@-@/*�����ʺ�*/@-@/*��������*/@-@/*Ʊ֤�������*/@-@ /*���ջ���*/@-@/*���ջ��ش���*/@-@ /*˰Ʊ����*/@-@/*����ʱ����*/@-@/*����ʱ��ֹ*/@-@/*�����*/@-@/*�տ�������*/@-@/*�Ǽ�ע�����ʹ���*/@-@/*������ϵ*/@-@/*��Ʊ���*/@-@/*�޽�����*/@-@/*��ע*/@-@/*������Ŀ1����*/@-@/*Ԥ���Ŀ1����*/@-@/*Ԥ�㼶��1*/@-@/*Ԥ�㼶��1����*/@-@/*����ƷĿ1����*/@-@ /*��˰����1*/@-@/*��������1*/@-@/*˰��1*/@-@/*�ѽɿ۳���1*/@-@/*ʵ��˰��1*/@-@/*������Ŀ2����*/@-@/*����ƷĿ2����*/@-@/*��˰����2*/@-@/*��������2*/@-@/*˰��2*/@-@/*�ѽɿ۳���2*/@-@/*ʵ��˰��2*/@-@/*������Ŀ3����*/@-@/*����ƷĿ3����*/@-@/*��˰����3*/@-@/*��������3*/@-@/*˰��3*/@-@/*�ѽɿ۳���3*/@-@/*ʵ��˰��3*/@-@".getBytes());
            //output.flush();
            client.shutdownOutput();
            //Thread.sleep(5000);
            //byte[] res = new byte[1024];
            DataInputStream is = new DataInputStream(client.getInputStream());
            int len = is.readInt();
            byte[] res = new byte[len];
            is.read(res,0,len);
            is.close();
            int port = client.getLocalPort();
            System.out.println(port + new String(res));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(client!=null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
