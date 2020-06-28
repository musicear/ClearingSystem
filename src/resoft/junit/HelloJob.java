package resoft.junit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * function:
 * User: albert lee
 * Date: 2005-8-30
 * Time: 10:40:36
 */
public class HelloJob implements Job{
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(System.currentTimeMillis() + ":Hello,World");
    }
}
