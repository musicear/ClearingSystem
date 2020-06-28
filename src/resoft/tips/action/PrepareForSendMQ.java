package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.tips.util.Signer;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>准备发送MQ</p>
 * User: liguoyin
 * Date: 2007-5-23
 * Time: 22:50:31
 */
public class PrepareForSendMQ implements Action {
    private static final Log logger = LogFactory.getLog(PrepareForSendMQ.class);
    public int execute(Message msg) throws Exception {
        Packager packager = new GenericXmlPackager();
        String packet = new String(packager.pack(msg)).replaceAll("UTF-8", "GBK");

        //添加签名
        String strEncData = Signer.getInstance().detachSign(packet);
        packet += "<!--" + strEncData + "-->";
        msg.set("strEncData", "<!--" + strEncData + "-->");
        logger.info("签名结果：" + strEncData);
        msg.set("报文内容",packet);
        return SUCCESS;
    }
}

