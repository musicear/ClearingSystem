package resoft.basLink.jmx;

import resoft.xlink.comm.Service;

/**
 * <p>function:使Service可在JMX中进行管理</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-12-1</p>
 * <p>Time: 14:52:28</p>
 */
public interface JMXServiceMBean extends Service {
    public String getDescription();
}
