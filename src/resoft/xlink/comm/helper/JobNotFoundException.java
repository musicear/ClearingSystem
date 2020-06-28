package resoft.xlink.comm.helper;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-12-20
 * Time: 15:29:09
 */
public class JobNotFoundException extends Exception{
    public JobNotFoundException(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    private String jobName;
}
