package resoft.xlink;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Service;
import resoft.xlink.comm.ServiceDescription;
import resoft.xlink.comm.ServiceFinder;

/**
 * <p>启动中心</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 1:15:47
 */
public class XLinkServer {
    private static final Log logger = LogFactory.getLog(XLinkServer.class);

    public XLinkServer(String serviceFinderClassName) throws Exception {
        try {
             serviceFinder = (ServiceFinder) Class.forName(serviceFinderClassName).newInstance();
        } catch (Exception e) {
            logger.error("服务查找类\"" + serviceFinderClassName + "\"加载失败", e);
            throw e;
        }
    }

    public void startup() {
        //every service start a new thread
        for (Iterator itr = serviceFinder.findAllServices().iterator(); itr.hasNext();) {
            ServiceDescription serviceDesc = (ServiceDescription) itr.next();
            startup(serviceDesc);
        }
        logger.info("XLinkServer is running...");
    }

    public void startup(ServiceDescription serviceDesc) {
        String className = serviceDesc.getClassName();
        Service service;
        try {
             service = (Service) Class.forName(className).newInstance();
        } catch (Exception e) {
            logger.error("service \"" + serviceDesc.getName() + "\"加载失败", e);
            return;
        }
        Map properties = serviceDesc.getProperties();
        for(Iterator itr = properties.keySet().iterator();itr.hasNext();) {
            String key = (String) itr.next();
            String value = (String) properties.get(key);
            service.setProperty(key,value);
        }
        new ServiceRunner(serviceDesc.getName(), service);
    }

    private ServiceFinder serviceFinder;
}
class ServiceRunner {
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
