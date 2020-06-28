package resoft.tips.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.Message;
import resoft.basLink.util.BasLinkClient;
import resoft.tips.action2.CheckBillToPay;

/**
 * <p>CFXͨѶ��ʽ�����ࡣ����WEB�뽻����ϵͳͨѶ</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 11:10:58
 */
public class TransCommUtil {
    private static Configuration conf = Configuration.getInstance();

    private static Log logger = LogFactory.getLog(CheckBillToPay.class);
    public static Message send(String transCode,Message msg) throws IOException {
        String host = conf.getProperty("tcpTrans","host");
        logger.info("host is: "+host);
        logger.info("msg is: " + msg.getValue("ORIPACKMSGNO"));
        if(host==null || host.equals("")) {
            throw new IOException("��ȡ����ϵͳ��ַ����");
        }
        int port = Integer.parseInt(conf.getProperty("tcpTrans","port"));
        logger.info("port is: "+ port);
        Message msg_re = BasLinkClient.send(host,port,transCode,msg);
        logger.info("result is: "+ msg_re.getValue("Result"));
        return msg_re;
    }
    static 
	{
		logger = LogFactory.getLog(resoft.tips.util.TransCommUtil.class);
	}
}
