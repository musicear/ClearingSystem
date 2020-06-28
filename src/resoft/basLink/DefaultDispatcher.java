package resoft.basLink;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.protocol.CfxProtocol;

/**
 * function:����IP��ͬ�趨��ͬ��Protocal
 * User: albert lee
 * Date: 2005-10-28
 * Time: 15:35:12
 */
public class DefaultDispatcher implements Dispatcher {
    private static Log logger = LogFactory.getLog(DefaultDispatcher.class);
    public void process() {

        Socket conn = (Socket) ThreadLocalContext.getInstance().getContext().getProperty("connection");
        String ip = conn.getInetAddress().getHostAddress();
        logger.info("����ͻ���IPΪ:" + ip);
        logger.info("����ͻ���Port:" + conn.getPort());
        String className = Configuration.getInstance().getProperty("dispatcher",ip);
        logger.info("dispatcher:" + className);
        Protocol protocol;
        if(className.equals("")) {
            protocol = new CfxProtocol();
        } else {
            try {
                protocol = (Protocol) Class.forName(className).newInstance();
            } catch (Exception e) {
                logger.error("����Э��:" + className + "ʧ��",e);
                return;
            }
        }
        logger.info(protocol);

        ThreadLocalContext.getInstance().getContext().setProtocol(protocol);
    }
}
