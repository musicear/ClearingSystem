package resoft.xlink.comm.helper;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import resoft.xlink.comm.services.util.GenericJob;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>�������������</p>
 * Author: liguoyin
 * Date: 2007-12-17
 * Time: 14:43:40
 */
public class JobManager {
    private static final Log logger = LogFactory.getLog(JobManager.class);

    private static final JobManager instance = new JobManager();

    private final Object lock = new Object();

    private JobManager() {
        init();
    }

    /**
     * �õ�Ψһʵ��
     */
    public static JobManager getInstance() {
        return instance;
    }

    /**
     * ��ʼ��
     */
    private void init() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            sched = schedFact.getScheduler();
            sched.start();
            inited = true;
        } catch (SchedulerException e) {
            logger.fatal("������ѯ����ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * �������
     */
    public synchronized void addJob(String jobName, String cronExpr) {
        if (!inited) {
            throw new RuntimeException("��ʼ�����ɹ�");
        }
        JobDetail job = new JobDetail(jobName, "default", GenericJob.class);
        job.getJobDataMap().put("jobName", jobName);
        job.getJobDataMap().put("globalPropsReaderClassName", globalPropsReaderClassName);
        CronTrigger trigger = null;
        try {
            trigger = new CronTrigger(jobName, "default", cronExpr);
        } catch (ParseException e) {
            logger.error("��ѯ" + jobName + "ʱ�䶨����ʽ����");
        }
        if (trigger != null) {
            try {
                sched.scheduleJob(job, trigger);
                logger.info("Job \"" + jobName + "\" started");
            } catch (SchedulerException e) {
                logger.error("��������" + jobName + "ʧ��", e);
            }
        }
        jobThreads.put(jobName, new Integer(0));
        jobTriggers.put(jobName, trigger);
    }

    /**
     * ��������
     */
    public void run(final String jobName) {
        final Controller controller = new Controller();
        controller.setNameOfTransCode("������");
        controller.setGlobalPropertiesReader(globalPropsReaderClassName);

        controller.setInternalErrorAction(new Action() {
            public int execute(Message msg) throws Exception {
                logger.error("��ѯ����:" + msg.get("������") + "δ����");
                return 0;
            }
        });
        synchronized (lock) {
            int threads = ((Integer) jobThreads.get(jobName)).intValue();
            threads ++;
            jobThreads.put(jobName, new Integer(threads));
        }
        new Thread(new Runnable() {

            public void run() {
                Message msg = new DefaultMessage();
                msg.set("������", jobName);
                controller.execute(msg);
                synchronized (lock) {
                    int threads = ((Integer) jobThreads.get(jobName)).intValue();
                    threads --;
                    jobThreads.put(jobName, new Integer(threads));
                }
            }
        }).start();

    }

    /**
     * �ж�ĳһ�����Ƿ�������
     */
    public boolean isRunning(String jobName) throws JobNotFoundException {
        if (!jobThreads.containsKey(jobName)) {
            throw new JobNotFoundException(jobName);
        }
        int threads = ((Integer) jobThreads.get(jobName)).intValue();
        return threads > 0;
    }

    /**
     * ������´�ִ��ʱ��
     */
    public Date getNextFireTime(String jobName) throws JobNotFoundException {
        if (!jobTriggers.containsKey(jobName)) {
            throw new JobNotFoundException(jobName);
        }
        Trigger trigger = (Trigger) jobTriggers.get(jobName);
        return trigger.getNextFireTime();
    }


    public void setGlobalPropsReaderClassName(String globalPropsReaderClassName) {
        this.globalPropsReaderClassName = globalPropsReaderClassName;
    }


    private Scheduler sched = null;
    private boolean inited = false;
    private String globalPropsReaderClassName;

    private Map jobThreads = new HashMap(); //ÿ������ǰִ����
    private Map jobTriggers = new HashMap();//ÿ�������Ӧ��Trigger
}
