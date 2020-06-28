package resoft.tips.chqsh;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检索是否有连接测试请求</p>
 * Author:fanchengwei
 */
public class CheckSend9005 implements Action {
    public int execute(Message msg) {
    	try
    	{
	        String connflag = DBUtil.queryForString("select connflag from conntest");
	        if(connflag.equals("N"))
	        {
	        	return FAIL;
	        }
	        else
	        {
	        	DBUtil.executeUpdate("update conntest set connflag='N'");
	        }
    	}
    	catch(Exception e)
    	{
    		return FAIL;
    	}
        return SUCCESS;
    }
}


