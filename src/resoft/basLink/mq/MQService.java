package resoft.basLink.mq;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.xlink.comm.Service;

/**
 * <p>MQ监听服务启动</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 22:59:00
 */
public class MQService implements Service {
    private static final Log logger = LogFactory.getLog(MQService.class);
    public void start() throws Exception {
        Configuration conf = Configuration.getInstance("mq");
        Collection categories = conf.listAllCategories();
        for (Iterator itr = categories.iterator(); itr.hasNext();) {
            String name = (String) itr.next();
            Map properties = new HashMap();
            if(conf.listAllProperties(name).size()==0) {
                continue;
            }
            for (Iterator itrProperties = conf.listAllProperties(name).iterator(); itrProperties.hasNext();) {
                String propName = (String) itrProperties.next();
                String propValue = conf.getProperty(name, propName);
                properties.put(propName, propValue);
            }
            new Thread(new MQListener(properties)).start();
            logger.info("开始监听MQ队列：name=" + name + ";properties:" + properties);
        }

    }

    public void stop() throws Exception {
    }

    public boolean isRunning() {
        return true;
    }

    public void setProperty(String key, String value) {

    }

}
