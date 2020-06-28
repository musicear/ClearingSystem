package resoft.basLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import resoft.xlink.comm.ServiceDescription;
import resoft.xlink.comm.ServiceFinder;

/**
 * <p>服务查找类。通过配置文件来查找</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 22:06:56
 */
public class BasServiceFinder implements ServiceFinder {
    public Collection findAllServices() {
        Collection services = new ArrayList();
        Configuration conf = Configuration.getInstance();
        for (Iterator itr = conf.listAllProperties("services").iterator(); itr.hasNext();) {
            String serviceName = (String) itr.next();
            String className = conf.getProperty("services",serviceName);
            ServiceDescription serviceDesc = new ServiceDescription();
            serviceDesc.setName(serviceName);
            serviceDesc.setClassName(className);
            //属性
            Map properties = new HashMap();
            for(Iterator itrProps = conf.listAllProperties(serviceName).iterator();itrProps.hasNext();) {
                String key = (String) itrProps.next();
                String value = conf.getProperty(serviceName,key);
                properties.put(key,value);
            }
            serviceDesc.setProperties(properties);
            services.add(serviceDesc);
        }
        return services;
    }
}
