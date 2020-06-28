package junit.comm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import resoft.xlink.comm.ServiceDescription;
import resoft.xlink.comm.ServiceFinder;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 10:04:48
 */
public class DickServiceFinder implements ServiceFinder {
    public Collection findAllServices() {
        Collection services = new ArrayList();
        ServiceDescription serviceDesc = new ServiceDescription();
        serviceDesc.setName("TimerService");
        serviceDesc.setClassName("resoft.xlink.comm.services.TimerService");
        //º”‘ÿ»ŒŒÒ
        Map jobMap = new HashMap();
        jobMap.put("job:100","0 0/1 * * * ?");
        serviceDesc.setProperties(jobMap);
        services.add(serviceDesc);
        return services;
    }
}
