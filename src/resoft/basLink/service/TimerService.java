package resoft.basLink.service;

import java.text.ParseException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import resoft.basLink.Configuration;
import resoft.basLink.Service;

/**
 * function: 轮询服务
 * User: albert lee
 * Date: 2005-9-26
 * Time: 9:05:15
 */
public class TimerService implements Service {
    private static Log logger = LogFactory.getLog(TimerService.class);

    public void start() throws Exception {
        Configuration conf = Configuration.getInstance();
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        Scheduler sched;
        try {
            sched = schedFact.getScheduler();
            sched.start();
        }
        catch (SchedulerException e) {
            logger.fatal("启动轮询服务失败", e);
            return;
        }
        logger.info("Job Service is running");
        for (Iterator itrJobs = conf.listAllProperties("jobs").iterator(); itrJobs.hasNext();) {
            String jobName = (String) itrJobs.next();
            String cronExpression = conf.getProperty("jobs", jobName);
            JobDetail job = new JobDetail(jobName, "default", GenericJob.class);
            job.getJobDataMap().put("jobName", jobName);
            CronTrigger trigger = null;            
            try {
                trigger = new CronTrigger(jobName, "default", cronExpression);
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
        }
    }

    public void stop() throws Exception {

    }

    public boolean isRunning() {
        return false;
    }
}
