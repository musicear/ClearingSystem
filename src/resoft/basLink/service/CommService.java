package resoft.basLink.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import resoft.basLink.AppWorker;
import resoft.basLink.Configuration;
import resoft.basLink.Service;
import resoft.basLink.service.tcp.CommReaderWorker;
import resoft.basLink.service.tcp.CommWriterWorker;

/**
 * function:阻塞式通讯
 * User: albert lee
 * Date: 2005-9-26
 * Time: 8:59:47
 */
public class CommService implements Service{
    private static Log logger = LogFactory.getLog(CommService.class);
    private static final int MaxThread = 500;
    private static final int MinThread = 200;
    private static final int DefaultPort = 1200;
    public void start() throws Exception {
        Configuration conf = Configuration.getInstance();
        String str = conf.getProperty("server","listenPort");
        int port = DefaultPort;
        if(!str.equals("")) {
            port = Integer.parseInt(str);
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            logger.info("BasLinkServer listening on port " + port);
        } catch (IOException e) {
            logger.error("启动BasServer失败",e);
            return;
        }
        running = true;
        //启动各种监听
        AppWorker.start();
        CommWriterWorker.start();
        PooledExecutor pooledExecutor = new PooledExecutor(MaxThread);
        pooledExecutor.setMinimumPoolSize(MinThread);
        pooledExecutor.createThreads(MinThread);
        pooledExecutor.waitWhenBlocked();
        while(true) {
            Socket conn = null;
            try {
                conn = server.accept();
                conn.setSoTimeout(10 * 1000);
            } catch (IOException e) {
                logger.error("接收TCP连接失败",e);
                continue;
            }
            try {
                pooledExecutor.execute(new CommReaderWorker(conn));
            } catch (InterruptedException e) {
                logger.error("处理连接失败",e);
            }
        }
    }

    public void stop() throws Exception {
        //todo how to stop it?
    }

    public boolean isRunning() {
        return running;
    }
    private boolean running = false;
}
