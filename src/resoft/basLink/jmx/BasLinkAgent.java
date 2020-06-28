package resoft.basLink.jmx;

import java.util.Iterator;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jdmk.comm.HtmlAdaptorServer;

import resoft.basLink.Configuration;
import resoft.xlink.comm.Service;

/**
 * <p>function:JMX Agent</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-12-1</p>
 * <p>Time: 15:56:22</p>
 */
public class BasLinkAgent {
    private static Log logger = null;
    static {
        //log
        System.setProperty(LogFactory.FACTORY_PROPERTY,"resoft.basLink.log.BasLinkLogFactory");
        logger = LogFactory.getLog(BasLinkAgent.class);
    }
    private MBeanServer mbs = null;
    private BasLinkAgent(int port) throws MalformedObjectNameException, NotCompliantMBeanException, MBeanRegistrationException, InstanceAlreadyExistsException {
        mbs = MBeanServerFactory.createMBeanServer();
        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
        ObjectName adaptorName = new ObjectName("BLAgent:name=htmlAdaptor,port=" + port);
        adaptor.setPort(port);
        mbs.registerMBean(adaptor,adaptorName);
        //注册所有Service
        for(Iterator itr = Configuration.getInstance().listAllProperties("services").iterator();itr.hasNext();) {
            String serviceName = (String) itr.next();
            startup(serviceName);
        }

        adaptor.start();
    }
    private void startup(String serviceName) {
        String className = Configuration.getInstance().getProperty("services",serviceName);
        try {
            Service service = (Service) Class.forName(className).newInstance();
            register(serviceName,service);
        } catch (Exception e) {
            logger.error("service \"" + serviceName + "\"启动失败",e);
        }
    }
    /**
     * 注册Service
     * */
    private void register(String serviceName,Service service) throws MalformedObjectNameException, NotCompliantMBeanException, MBeanRegistrationException, InstanceAlreadyExistsException {
        JMXServiceMBean serviceMBean = new JMXService(serviceName,service);
        ObjectName beanName = new ObjectName("BLAgent:name=" + serviceName);
        mbs.registerMBean(serviceMBean,beanName);
        try {
            serviceMBean.start();
        } catch (Exception e) {
            logger.error("启动服务" + serviceName + "失败");
        }
    }
    /**
     * 主启动程序
     * 第一个参数为JMX端口号，第二个参数若有则是服务名
     * */
    public static void main(String[] args) throws MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, InstanceAlreadyExistsException {
        if(args.length==0) {
            System.out.println("启动参数错误.");
            System.exit(-1);
        } else {
            int port = Integer.parseInt(args[0]);
            new BasLinkAgent(port);
        }
    }
}
