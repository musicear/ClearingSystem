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
 * <p>定义任务管理器</p>
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
     * 得到唯一实例
     */
    public static JobManager getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    private void init() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            sched = schedFact.getScheduler();
            sched.start();
            inited = true;
        } catch (SchedulerException e) {
            logger.fatal("启动轮询服务失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加任务
     */
    public synchronized void addJob(String jobName, String cronExpr) {
        if (!inited) {
            throw new RuntimeException("初始化不成功");
        }
        JobDetail job = new JobDetail(jobName, "default", GenericJob.class);
        job.getJobDataMap().put("jobName", jobName);
        job.getJobDataMap().put("globalPropsReaderClassName", globalPropsReaderClassName);
        CronTrigger trigger = null;
        try {
            trigger = new CronTrigger(jobName, "default", cronExpr);
        } catch (ParseException e) {
            logger.error("轮询" + jobName + "时间定义表达式错误");
        }
        if (trigger != null) {
            try {
                sched.scheduleJob(job, trigger);
                logger.info("Job \"" + jobName + "\" started");
            } catch (SchedulerException e) {
                logger.error("启动任务" + jobName + "失败", e);
            }
        }
        jobThreads.put(jobName, new Integer(0));
        jobTriggers.put(jobName, trigger);
    }

    /**
     * 运行任务
     */
    public void run(final String jobName) {
        final Controller controller = new Controller();
        controller.setNameOfTransCode("交易码");
        controller.setGlobalPropertiesReader(globalPropsReaderClassName);

        controller.setInternalErrorAction(new Action() {
            public int execute(Message msg) throws Exception {
                logger.error("轮询任务:" + msg.get("交易码") + "未定义");
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
                msg.set("交易码", jobName);
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
     * 判断某一任务是否在运行
     */
    public boolean isRunning(String jobName) throws JobNotFoundException {
        if (!jobThreads.containsKey(jobName)) {
            throw new JobNotFoundException(jobName);
        }
        int threads = ((Integer) jobThreads.get(jobName)).intValue();
        return threads > 0;
    }

    /**
     * 任务的下次执行时间
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

    private Map jobThreads = new HashMap(); //每个任务当前执行数
    private Map jobTriggers = new HashMap();//每个任务对应的Trigger
}
