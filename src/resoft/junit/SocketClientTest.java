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

            //String str = "<CFX><MSG><交易码 val=\"1000\"/><预算级次 val=\"3\"/></MSG></CFX>";
            String str = "<CFX><MSG><nsrsbh val=\"370206614304893\"/><nsrmc val=\"小二黑\"/><yhMc val=\"银行名称\"/><yhzh val=\"3803026019200000808\"/><pzzlDm val=\"411\"/><zsjgDm val=\"23701020000\"/><xtspHm val=\"";
            String xtSphm = Math.random() + "00";
            str += xtSphm;
            str += "\"/><sssqQ val=\"2004-12-1\"/><sssqZ val=\"2004-12-31\"/><tfRq val=\"2005-1-17\"/><skgkDm val=\"37020601\"/><hyDm val=\"0909\"/><hyMc val=\"采掘业\"/><tkgkDm val=\"23701020\"/><yhzlDm val=\"0011\"/><ttlxDm1 val=\"10\"/><yskmDm val=\"010120\"/><zsxmDm val=\"101\"/><djzclxDm val=\"12\"/><djzclxMc val=\"12\"/><kpJe val=\"6.66\"/><xjRq val=\"2005-1-17\"/><bz val=\"备注\"/><sbfsDm val=\"01\"/><zsxmDm1 val=\"101\"/><yskmDm1 val=\"010120\"/><ysfpblDm val=\"001090\"/><zspmDm1 val=\"9900\"/><kssl1 val=\"0\"/><xssr1 val=\"\"/><sl1 val=\"0.000500\"/><yjkce1 val=\"0\"/><sjSe1 val=\"6.66\"/><zsxmDm2 val=\"\"/><yskmDm2 val=\"\"/><ysfpblDm2 val=\"7809\"/><zspmDm2 val=\"\"/><kssl2 val=\"\"/><xssr2 val=\"\"/><sl2 val=\"\"/><yjkce2 val=\"\"/><sjSe2 val=\"\"/><交易码 val=\"1300\"/></MSG></CFX>";

            //String str = "大";
            output.writeInt(str.getBytes().length);
            output.write(str.getBytes());

            //output.write("\"GB18030\"?><CFX><MSG><交易码 val=\"1000\"/><预算级次 val=\"3\"/></MSG></CFX>".getBytes());
            //output.write("1000@-@/*纳税人识别号*/@-@/*纳税人名称*/@-@/*银行种类代码*/@-@/*银行帐号*/@-@/*银行名称*/@-@/*票证种类代码*/@-@ /*征收机关*/@-@/*征收机关代码*/@-@ /*税票号码*/@-@/*所属时期起*/@-@/*所属时期止*/@-@/*填发日期*/@-@/*收款国库代码*/@-@/*登记注册类型代码*/@-@/*隶属关系*/@-@/*开票金额*/@-@/*限缴日期*/@-@/*备注*/@-@/*征收项目1代码*/@-@/*预算科目1代码*/@-@/*预算级次1*/@-@/*预算级次1代码*/@-@/*征收品目1代码*/@-@ /*课税数量1*/@-@/*销售收入1*/@-@/*税率1*/@-@/*已缴扣除额1*/@-@/*实缴税额1*/@-@/*征收项目2代码*/@-@/*征收品目2代码*/@-@/*课税数量2*/@-@/*销售收入2*/@-@/*税率2*/@-@/*已缴扣除额2*/@-@/*实缴税额2*/@-@/*征收项目3代码*/@-@/*征收品目3代码*/@-@/*课税数量3*/@-@/*销售收入3*/@-@/*税率3*/@-@/*已缴扣除额3*/@-@/*实缴税额3*/@-@".getBytes());
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
