package resoft.xlink.comm.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.Service;
import resoft.xlink.comm.helper.JobManager;

/**
 * <p>��ʱ����</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 0:34:49
 */
public class TimerService implements Service {
    private static Log logger = LogFactory.getLog(TimerService.class);

    public void start() throws Exception {
        JobManager jobManager = JobManager.getInstance();
        jobManager.setGlobalPropsReaderClassName(globalPropsReaderClassName);
        logger.info("Job Service is running");
        for (Iterator itrJobs = jobMap.keySet().iterator(); itrJobs.hasNext();) {
            String jobName = (String) itrJobs.next();
            String cronExpression = (String) jobMap.get(jobName);
            jobManager.addJob(jobName, cronExpression);
        }
    }


    public void stop() throws Exception {

    }

    public boolean isRunning() {
        return false;
    }

    /**
     * �������ԡ���������
     *
     * @param name  String ��������  ������Ϊprocess��ӦΪ job:process
     * @param value String �����cron���ʽ
     */
    public void setProperty(String name, String value) {
        if (name.startsWith("job:")) {
            String taskName = name.substring(4, name.length());
            if (taskName.length() > 0) {
                jobMap.put(taskName, value);
            }
        } else if (name.equalsIgnoreCase("globalPropertiesReader")) {
            globalPropsReaderClassName = value;
        }
    }

    private Map jobMap = new HashMap();

    private String globalPropsReaderClassName;
}
