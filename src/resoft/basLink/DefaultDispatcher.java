package resoft.basLink;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.protocol.CfxProtocol;

/**
 * function:根据IP不同设定不同的Protocal
 * User: albert lee
 * Date: 2005-10-28
 * Time: 15:35:12
 */
public class DefaultDispatcher implements Dispatcher {
    private static Log logger = LogFactory.getLog(DefaultDispatcher.class);
    public void process() {

        Socket conn = (Socket) ThreadLocalContext.getInstance().getContext().getProperty("connection");
        String ip = conn.getInetAddress().getHostAddress();
        logger.info("请求客户端IP为:" + ip);
        logger.info("请求客户端Port:" + conn.getPort());
        String className = Configuration.getInstance().getProperty("dispatcher",ip);
        logger.info("dispatcher:" + className);
        Protocol protocol;
        if(className.equals("")) {
            protocol = new CfxProtocol();
        } else {
            try {
                protocol = (Protocol) Class.forName(className).newInstance();
            } catch (Exception e) {
                logger.error("加载协议:" + className + "失败",e);
                return;
            }
        }
        logger.info(protocol);

        ThreadLocalContext.getInstance().getContext().setProtocol(protocol);
    }
}
