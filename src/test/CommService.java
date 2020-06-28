package test;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import resoft.tips.web.action.util.AppWorker;
import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.Service;
import resoft.xlink.comm.services.tcp.CommReaderWorker;
import resoft.xlink.comm.services.tcp.CommWriterWorker;
//import resoft.xlink.comm.services.util.AppWorker;

public class CommService
    implements Service
{

    public CommService()
    {
        running = false;
        port = 16000;
       // dispatcher = new MockDispatcher();
    }

    public void start()
        throws Exception
    {
        ServerSocket server;
        try
        {
            server = new ServerSocket(port);
            logger.info("XLinkServer listening on port " + port);
        }
        catch(IOException e)
        {
            logger.error("\u542F\u52A8BasServer\u5931\u8D25", e);
            return;
        }
        running = true;
        PooledExecutor pooledExecutor = new PooledExecutor(500);
        pooledExecutor.setMinimumPoolSize(200);
        pooledExecutor.createThreads(200);
        pooledExecutor.waitWhenBlocked();
        AppWorker.start(nameOfTransCode, globalPropsReaderClassName);
        CommWriterWorker.start();
        running = true;
        do
        {
            if(!running)
                break;
            Socket conn;
            try
            {
                conn = server.accept();
                conn.setSoTimeout(10000);
            }
            catch(IOException e)
            {
                logger.error("\u63A5\u6536TCP\u8FDE\u63A5\u5931\u8D25", e);
                continue;
            }
            try
            {
                pooledExecutor.execute(new CommReaderWorker(conn, dispatcher));
            }
            catch(InterruptedException e)
            {
                logger.error("\u5904\u7406\u8FDE\u63A5\u5931\u8D25", e);
            }
        } while(true);
    }

    public void stop()
        throws Exception
    {
        running = false;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setProperty(String name, String value)
    {
        if(name.equalsIgnoreCase("listenPort") && !value.equals(""))
            port = Integer.parseInt(value);
        else
        if(name.equalsIgnoreCase("dispatcher") && !value.equals(""))
            try
            {
                dispatcher = (Dispatcher)Class.forName(value).newInstance();
            }
            catch(Exception e)
            {
                logger.error("\u52A0\u8F7DDispatcher:" + value + "\u5931\u8D25", e);
            }
        else
        if(name.equalsIgnoreCase("nameOfTransCode"))
        {
            nameOfTransCode = value;
            if(nameOfTransCode == null)
                nameOfTransCode = "\u4EA4\u6613\u7801";
        } else
        if(name.equalsIgnoreCase("globalPropertiesReader"))
            globalPropsReaderClassName = value;
    }
 

    private static Log logger;
    private static final int MaxThread = 500;
    private static final int MinThread = 200;
    private boolean running;
    private int port;
    private String nameOfTransCode="TransCode";
    private String globalPropsReaderClassName="resoft.basLink.util.GlobalPropertiesReaderImpl";
    private Dispatcher dispatcher;

    static 
    {
        logger = LogFactory.getLog(resoft.xlink.comm.services.CommService.class);
    }
}
