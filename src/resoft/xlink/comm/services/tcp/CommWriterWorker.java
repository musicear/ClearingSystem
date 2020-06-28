package resoft.xlink.comm.services.tcp;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Protocol;
import resoft.xlink.comm.helper.CommData;
import resoft.xlink.comm.helper.Pipeline;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p>发送响应</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 0:23:12
 */
public class CommWriterWorker implements Runnable {
    private static Log logger = LogFactory.getLog(CommWriterWorker.class);

    private static final int MaxThread = 10;

    public static void start() {
        for (int i = 0; i < MaxThread; i++) {
            Thread t = new Thread(new CommWriterWorker());
            t.setName("writer-" + i);
            t.start();
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            CommData data = Pipeline.getInstance().takeResponse();
            if (data != null) {
                //发送响应
                Protocol protocol = null;
                Socket conn = (Socket) data.getContext().getProperty("connection");
                try {
                    protocol = ThreadLocalContext.getInstance().getContext().getProtocol();
                    protocol.write(conn.getOutputStream(), data.getData());
                } catch (IOException e) {
                    logger.info("响应失败", e);
                } finally {
                    if (protocol != null && !protocol.isKeepAlive()) {
                        try {
                            conn.close();
                        } catch (IOException e) {
                            logger.error("关闭错误", e);
                        }
                    }
                }

            }
        }
    }
}
