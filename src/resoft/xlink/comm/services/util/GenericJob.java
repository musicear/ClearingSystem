package resoft.xlink.comm.services.util;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import resoft.xlink.comm.helper.JobManager;

/**
 * <p>定时通用任务，用于调用交易</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 0:51:07
 */
public class GenericJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String transName = (String) map.get("jobName");

        JobManager.getInstance().run(transName);


    }
}
