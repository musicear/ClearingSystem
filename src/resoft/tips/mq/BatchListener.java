package resoft.tips.mq;

import java.io.IOException;
import java.util.Map;

import resoft.basLink.Configuration;
import resoft.tips.util.Signer;
import resoft.xlink.comm.AsyncListener;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.comm.impl.GenericXmlMessage;
import resoft.xlink.core.Message;

/**
 * <p>批量类交易处理</p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 23:06:15
 */
public class BatchListener implements AsyncListener {

    private static Configuration conf = Configuration.getInstance();

    public int onMessage(byte[] bytes, Map properties) {
        //首先校验签名
        String data = new String(bytes);
        int big=data.indexOf("<MsgNo>");
        int end=data.indexOf("</MsgNo>");
        String tranCode="";
        tranCode=data.substring(big+7, end);
        boolean checkSignSuccess = Signer.getInstance().verifySign(data);

        Message msg = new GenericXmlMessage();
        msg.set("correlationId", new String((byte[]) properties.get("correlationId")));
        msg.set("messageId", new String((byte[]) properties.get("messageId")));
        msg.set("核押结果", Boolean.valueOf(checkSignSuccess));
        //将内容存放于文件中
        try {
        	msg.set("tranCode", tranCode);
            String filePath = PacketWriter.writePacket(data,"_0_"+tranCode+"_");
            msg.set("tranCode", filePath);
            msg.set("批量文件", filePath);
        } catch (IOException e) {
            return -1;
        }

        msg.set("交易码", "batch");
        //进行调度
        Controller controller = new Controller();
        controller.setNameOfTransCode("交易码");
        controller.setGlobalPropertiesReader(conf.getProperty("general", "globalPropertiesReader"));
        controller.execute(msg);
        return 0;
    }
}
