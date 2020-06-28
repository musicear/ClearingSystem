package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��������״̬</p>
 * Author: liguoyin
 * Date: 2007-6-15
 * Time: 15:44:33
 */
public class UpdateSysStatus implements Action {
    private static final Log logger = LogFactory.getLog(UpdateSysStatus.class);
    public int execute(Message msg) {
        String workDate = msg.getString("//CFX/MSG/ChangeState9101/WorkDate");
        String sysStatus = msg.getString("//CFX/MSG/ChangeState9101/SysStat");
        String sql = "";
        if(sysStatus.equals("1"))
        {
	        try
	        {
		        sql = "update SysStatus set preworkdate=workdate";
		        DBUtil.executeUpdate(sql);
		        sql = "update SysStatus set workDate='" + workDate + "',sysStatus='" + sysStatus + "'";
		        DBUtil.executeUpdate(sql);
	        }
	       catch(Exception e)
	       {
	    	   logger.info("����״̬���ʧ�ܣ���");
	    	   e.printStackTrace();
	    	   return FAIL;
	       }
        }
        else
        {
        	try
        	{
	        	sql = "update SysStatus set workDate='" + workDate + "',sysStatus='" + sysStatus + "'";
		        DBUtil.executeUpdate(sql);
        	}
        	catch(Exception e)
        	{
        		logger.info("����״̬���ʧ�ܣ���");
 	    	   e.printStackTrace();
 	    	   return FAIL;
        	}
        }
        logger.info("����״̬���������Ϊ��" + workDate + ";״̬Ϊ:" + sysStatus);
        return SUCCESS;
    }
}
