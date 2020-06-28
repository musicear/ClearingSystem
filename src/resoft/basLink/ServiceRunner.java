package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function: 启动服务的线程
 * User: albert lee
 * Date: 2005-9-22
 * Time: 11:25:51
 */
public class ServiceRunner {
    private static Log logger = LogFactory.getLog(ServiceRunner.class);
    public ServiceRunner(final String name,final Service service) {
        //用新线程来启动服务
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    service.start();
                } catch (Exception e) {
                    logger.error("启动服务" + name + "失败",e);
                }
            }
        });
        thread.start();
    }
}
