package resoft.junit.testTransactionFlow;

import org.apache.commons.logging.LogFactory;

import resoft.basLink.Service;
import resoft.basLink.ServiceRunner;

/**
 * <p>function:</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-11-30</p>
 * <p>Time: 20:17:12</p>
 */
public class StartCommServiceTest {
    static {
        //log
        System.setProperty(LogFactory.FACTORY_PROPERTY,"resoft.basLink.log.BasLinkLogFactory");
    }
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = "resoft.basLink.service.CommService";
        String serviceName = "CommService";
        Service service = (Service) Class.forName(className).newInstance();
        new ServiceRunner(serviceName,service);
    }
}
