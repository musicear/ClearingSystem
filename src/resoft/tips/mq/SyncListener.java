package resoft.tips.mq;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.util.Signer;
import resoft.xlink.comm.AsyncListener;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>实时业务监听器</p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 21:46:00
 */
public class SyncListener implements AsyncListener {
    private static final Log logger = LogFactory.getLog(SyncListener.class);

    private static Configuration conf = Configuration.getInstance();
    public int onMessage(byte[] bytes, Map properties) {
        //首先校验签名
        String data = new String(bytes);
        boolean checkSignSuccess = Signer.getInstance().verifySign(data);

        //进行交易处理
        Packager packager = new GenericXmlPackager();
        Message msg = packager.unpack(data.getBytes());
        String tranCode="_0_"+msg.getString("//CFX/HEAD/MsgNo");       
        msg.set("correlationId",new String((byte[]) properties.get("correlationId")));
        msg.set("messageId",new String((byte[]) properties.get("messageId")));
        msg.set("核押结果", Boolean.valueOf(checkSignSuccess));
        //保存内容到临时文件中
        try {
        	
            String filePath = PacketWriter.writePacket(data,tranCode);
            msg.set("tranCode", filePath);
            msg.set("批量文件", filePath);
        } catch (IOException e) {
            logger.error("保存为临时文件失败",e);
        }
        //交易调度
        Controller controller = new Controller();
        controller.setGlobalPropertiesReader(conf.getProperty("general","globalPropertiesReader"));
        controller.setNameOfTransCode("//CFX/HEAD/MsgNo");
        controller.setInternalErrorAction(new Action() {
            public int execute(Message msg) throws RuntimeException {
                logger.error("交易码不存在");
                return 0;
            }
        });

        controller.execute(msg);
        
        return 0;
    }
}
