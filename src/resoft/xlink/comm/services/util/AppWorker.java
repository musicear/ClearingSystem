package resoft.xlink.comm.services.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.helper.CommData;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.comm.helper.Pipeline;

/**
 * <p>处理层Workder。通过消息队列与通讯层接触</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 17:00:31
 */
public class AppWorker implements Runnable{
    private static Log logger = LogFactory.getLog(AppWorker.class);
    private static final int MaxThread = 100;
    public static void start(String nameOfTransCode,String globalPropsReaderClassName) {
        for(int i=0;i<MaxThread;i++) {
            Thread t = new Thread(new AppWorker(nameOfTransCode,globalPropsReaderClassName));
            t.setName("appWorker-" + i);
            t.start();
        }
    }

    private AppWorker(String nameOfTransCode,String globalPropsReaderClassName) {
        this.nameOfTransCode = nameOfTransCode;
        this.globalPropsReaderClassName = globalPropsReaderClassName;
    }


    public void run() {
        Controller controller = new Controller();
        controller.setNameOfTransCode(nameOfTransCode);
        if(globalPropsReaderClassName !=null && !globalPropsReaderClassName.equals("")) {
            controller.setGlobalPropertiesReader(globalPropsReaderClassName);
        }
        while(true) {
            CommData data = Pipeline.getInstance().takeRequest();
            if(data!=null) {
                String returnData;
                try {
                    returnData = new String(controller.execute(data.getData()));
                } catch(Exception e) {
                    logger.error("AppWorker处理失败",e);
                    returnData = "Error";
                }
                Pipeline.getInstance().offerResponse(data.getContext(),returnData.getBytes());
            }
        }
    }
    private String nameOfTransCode;
    private String globalPropsReaderClassName = null;
}
