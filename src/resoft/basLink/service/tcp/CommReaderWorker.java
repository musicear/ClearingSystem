package resoft.basLink.service.tcp;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Context;
import resoft.basLink.DefaultDispatcher;
import resoft.basLink.Dispatcher;
import resoft.basLink.Pipeline;
import resoft.basLink.Protocol;
import resoft.basLink.ThreadLocalContext;

/**
 * function: 读取Socket请求
 * User: albert lee
 * Date: 2005-9-26
 * Time: 23:22:51
 */
public class CommReaderWorker implements Runnable{
    private static Log logger = LogFactory.getLog(CommReaderWorker.class);

    public CommReaderWorker(Socket conn) {
        this.conn = conn;
    }
    public void run() {
        try {
            //设置请求者IP至ThreadLocal
            Context ctx = ThreadLocalContext.getInstance().getContext();
            ctx.setProperty("ip",conn.getLocalAddress().getHostAddress());
            ctx.setProperty("connection",conn);
            //设置Connection ID
            ThreadLocalContext.getInstance().setRequestId(String.valueOf(conn.hashCode()));
            dispatcher.process();

            Protocol protocol = ctx.getProtocol();
            byte[] reqBytes = protocol.read(conn.getInputStream());

            Pipeline.getInstance().offerRequest(ctx,reqBytes);
        } catch (IOException e) {
            logger.info("读取失败",e);
            if(conn!=null) {
                try {
                    conn.close();
                } catch(Exception e1) {
                    logger.error("关闭错误",e1);
                }
            }
        }
    }

    private Socket conn = null;

    private static Dispatcher dispatcher = new DefaultDispatcher();

}
