package resoft.basLink.jmx;

import resoft.xlink.comm.Service;

/**
 * <p>function:ServiceMBean之实现类</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-12-1</p>
 * <p>Time: 15:22:07</p>
 */
public class JMXService implements JMXServiceMBean {
    public JMXService(String serviceName, Service service) {
        this.serviceName = serviceName;
        this.service = service;
    }
    public String getDescription() {
        return serviceName;
    }

    public void start() throws Exception {
        //new ServiceRunner(serviceName,service);
    }

    public void stop() throws Exception {
        service.stop();
    }

    public boolean isRunning() {
        return service.isRunning();
    }

    public void setProperty(String string, String string1) {

    }

    private String serviceName;
    private Service service;
}
