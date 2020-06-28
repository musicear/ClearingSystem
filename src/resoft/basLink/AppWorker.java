package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function: 处理
 * User: albert lee
 * Date: 2005-9-27
 * Time: 16:25:41
 */
public class AppWorker implements Runnable{
    private static Log logger = LogFactory.getLog(AppWorker.class);
    private static final int MaxThread = 100;
    public static void start() {
        for(int i=0;i<MaxThread;i++) {
            Thread t = new Thread(new AppWorker());
            t.setName("appWorker-" + i);
            t.start();
        }
    }

    public void run() {
        while(true) {

            CommData data = Pipeline.getInstance().takeRequest();
            if(data!=null) {
                String returnData = null;
                try {
                    returnData = Controller.getInstance().execute(new String(data.getData()));
                    //logger.info(returnData);
                } catch(Exception e) {
                    logger.error("AppWorker处理失败",e);
                    returnData = "Error";
                }
                Pipeline.getInstance().offerResponse(data.getContext(),returnData.getBytes());
            }
        }
    }
}
