package resoft.tips.web.action.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.helper.CommData;
import resoft.xlink.comm.helper.Pipeline;
import test.Controller;
//import resoft.xlink.comm.helper.*;

public class AppWorker
    implements Runnable
{

    public static void start(String nameOfTransCode, String globalPropsReaderClassName)
    {
        for(int i = 0; i < 100; i++)
        {
            Thread t = new Thread(new AppWorker(nameOfTransCode, globalPropsReaderClassName));
            t.setName("appWorker-" + i);
            t.start();
        }

    }

    private AppWorker(String nameOfTransCode, String globalPropsReaderClassName)
    {
        this.globalPropsReaderClassName = null;
        this.nameOfTransCode = nameOfTransCode;
        this.globalPropsReaderClassName = globalPropsReaderClassName;
    }

    public void run()
    {
        Controller controller = new Controller();
        controller.setNameOfTransCode(nameOfTransCode);
        if(globalPropsReaderClassName != null && !globalPropsReaderClassName.equals(""))
            controller.setGlobalPropertiesReader(globalPropsReaderClassName);
        do
        {
            CommData data;
            do
                data = Pipeline.getInstance().takeRequest();
            while(data == null);
            String returnData;
            try
            {
                returnData = new String(controller.execute(data.getData()));
            }
            catch(Exception e)
            {
                logger.error("AppWorker\u5904\u7406\u5931\u8D25", e);
                returnData = "Error";
            }
            Pipeline.getInstance().offerResponse(data.getContext(), returnData.getBytes());
        } while(true);
    } 

    private static Log logger;
    private static final int MaxThread = 100;
    private String nameOfTransCode;
    private String globalPropsReaderClassName;

    static 
    {
        logger = LogFactory.getLog(resoft.xlink.comm.services.util.AppWorker.class);
    }
}
