package resoft.xlink.comm.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.Service;
import resoft.xlink.comm.impl.MockDispatcher;
import resoft.xlink.comm.services.tcp.CommReaderWorker;
import resoft.xlink.comm.services.tcp.CommWriterWorker;
import resoft.xlink.comm.services.util.AppWorker;

/**
 * <p>阻塞式通讯服务</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 16:50:55
 */
public class CommService implements Service {
    private static Log logger = LogFactory.getLog(CommService.class);
    private static final int MaxThread = 500;
    private static final int MinThread = 200;

    public void start() throws Exception {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
            logger.info("XLinkServer listening on port " + port);
        } catch (IOException e) {
            logger.error("启动BasServer失败", e);
            return;
        }
        running = true;
        PooledExecutor pooledExecutor = new PooledExecutor(MaxThread);
        pooledExecutor.setMinimumPoolSize(MinThread);
        pooledExecutor.createThreads(MinThread);
        pooledExecutor.waitWhenBlocked();
        //启动各种监听
        AppWorker.start(nameOfTransCode,globalPropsReaderClassName);
        CommWriterWorker.start();

        running = true;
        while (running) {
            Socket conn;
            try {
                conn = server.accept();
                conn.setSoTimeout(10 * 1000);
            } catch (IOException e) {
                logger.error("接收TCP连接失败", e);
                continue;
            }
            try {
                pooledExecutor.execute(new CommReaderWorker(conn,dispatcher));
            } catch (InterruptedException e) {
                logger.error("处理连接失败", e);
            }
        }
    }

    public void stop() throws Exception {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 设置属性
     */
    public void setProperty(String name, String value) {
        if (name.equalsIgnoreCase("listenPort") && !value.equals("")) {
            port = Integer.parseInt(value);
        } else if (name.equalsIgnoreCase("dispatcher") && !value.equals("")) {
            try {
                dispatcher = (Dispatcher) Class.forName(value).newInstance();
            } catch (Exception e) {
                logger.error("加载Dispatcher:" + value + "失败",e);
            }
        } else if(name.equalsIgnoreCase("nameOfTransCode")) {
            this.nameOfTransCode = value;
        } else if(name.equalsIgnoreCase("globalPropertiesReader")) {
            this.globalPropsReaderClassName = value;
        }
    }

    private boolean running = false;
    private int port = 1200;//默认端口
    private String nameOfTransCode;
    private String globalPropsReaderClassName;

    private Dispatcher dispatcher = new MockDispatcher();
}
