package resoft.basLink.service.tcp;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.CommData;
import resoft.basLink.Pipeline;
import resoft.basLink.Protocol;
import resoft.basLink.ThreadLocalContext;

/**
 * function: 发送响应
 * User: albert lee
 * Date: 2005-9-27
 * Time: 16:25:41
 */
public class CommWriterWorker implements Runnable{
    private static Log logger = LogFactory.getLog(CommWriterWorker.class);

    private static final int MaxThread = 10;
    public static void start() {
        for(int i=0;i<MaxThread;i++) {
            Thread t = new Thread(new CommWriterWorker());
            t.setName("writer-" + i);
            t.start();
        }
    }

    public void run() {
        while(true) {
            CommData data = Pipeline.getInstance().takeResponse();
            if(data!=null) {
                //发送响应
                Protocol protocol = null;
                Socket conn = (Socket) data.getContext().getProperty("connection");
                try {
                    protocol = ThreadLocalContext.getInstance().getContext().getProtocol();
                    protocol.write(conn.getOutputStream(),data.getData());
                } catch(IOException e) {
                    logger.info("响应失败",e);
                } finally {
                    if(!protocol.isKeepAlive()) {
                        try {
                            conn.close();
                        } catch (IOException e) {
                            logger.error("关闭错误",e);
                        }
                    }
                }

            }
        }
    }
}
