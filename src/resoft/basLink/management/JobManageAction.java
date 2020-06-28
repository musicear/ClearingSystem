package resoft.basLink.management;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import resoft.xlink.comm.helper.JobManager;
import resoft.xlink.comm.helper.JobNotFoundException;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>定时任务管理</p>
 * Author: liguoyin
 * Date: 2007-12-19
 * Time: 22:28:38
 */
public class JobManageAction implements Action {
    public int execute(Message msg) {
        String jobName = msg.getString("JobName");
        String task = msg.getString("Task");
        JobManager jobManager = JobManager.getInstance();
        if (task.equalsIgnoreCase("query")) {
            String status = "stop";
            boolean running = false;
            try {
                running = jobManager.isRunning(jobName);
            } catch (JobNotFoundException e) {
                status = "notExists";
            }

            if (running) {
                status = "running";
            }
            msg.set("Status", status);

            Date nextFireTime;
            try {
                nextFireTime = jobManager.getNextFireTime(jobName);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                msg.set("NextFireTime", df.format(nextFireTime));
            } catch (JobNotFoundException e) {
                msg.set("NextFireTime", "1900-01-01");
            }

        } else if (task.equalsIgnoreCase("forceRun")) {
            jobManager.run(jobName);
            msg.set("Message", "Success");
        }
        return SUCCESS;
    }
}
