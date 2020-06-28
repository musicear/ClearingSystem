package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function: ����������߳�
 * User: albert lee
 * Date: 2005-9-22
 * Time: 11:25:51
 */
public class ServiceRunner {
    private static Log logger = LogFactory.getLog(ServiceRunner.class);
    public ServiceRunner(final String name,final Service service) {
        //�����߳�����������
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    service.start();
                } catch (Exception e) {
                    logger.error("��������" + name + "ʧ��",e);
                }
            }
        });
        thread.start();
    }
}
