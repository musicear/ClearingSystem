package resoft.xlink.comm.services.tcp;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.Protocol;
import resoft.xlink.comm.helper.Context;
import resoft.xlink.comm.helper.Pipeline;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p>读取Socket请求</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 23:55:32
 */
public class CommReaderWorker implements Runnable{
    private static Log logger = LogFactory.getLog(CommReaderWorker.class);

    public CommReaderWorker(Socket conn,Dispatcher dispatcher) {
        this.conn = conn;
        this.dispatcher = dispatcher;
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

    private Dispatcher dispatcher;

}
