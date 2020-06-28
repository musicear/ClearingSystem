package resoft.basLink.service;

import javax.management.MBeanServerFactory;

import resoft.basLink.Service;

/**
 * <p>title</p>
 * User: Albert Li
 * Date: 2006-4-21
 * Time: 15:12:16
 */
public class MBeanService implements Service {
    public void start() throws Exception {
        MBeanServerFactory.createMBeanServer();
    }

    public void stop() throws Exception {
        throw new UnsupportedOperationException();
    }

    public boolean isRunning() {
        throw new UnsupportedOperationException();
    }
}
