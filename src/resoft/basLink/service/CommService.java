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
 * function:����ʽͨѶ
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
            logger.error("����BasServerʧ��",e);
            return;
        }
        running = true;
        //�������ּ���
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
                logger.error("����TCP����ʧ��",e);
                continue;
            }
            try {
                pooledExecutor.execute(new CommReaderWorker(conn));
            } catch (InterruptedException e) {
                logger.error("��������ʧ��",e);
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
