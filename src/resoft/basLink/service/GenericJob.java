package resoft.basLink.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import resoft.basLink.Message;
import resoft.basLink.TransactionFlow;
import resoft.basLink.TransactionFlowManager;

/**
 * <p>function:实现轮询服务</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-10</p>
 * <p>Time: 9:16:32</p>
 */
public class GenericJob implements Job{
    private static Log logger = LogFactory.getLog(GenericJob.class);
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String transName = (String) map.get("jobName");
        TransactionFlow flow = TransactionFlowManager.getInstance().getTransactionFlow(transName);
        if(flow!=null) {
            flow.execute(new Message());
        } else {
            logger.error("轮询任务:" + transName + "未定义");
        }

    }
}
