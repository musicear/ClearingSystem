package resoft.tips.action;


import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�ʺŲ�ѯ</p>
 * Author: liwei
 * Date: 2007-07-11
 * Time: 10:10:10
 */
public class CheckAcctOrd implements Action {
	private static final Log logger = LogFactory.getLog(CheckAcctOrd.class); 
	public int execute(Message msg) throws Exception {
		String AllNum=msg.getString("AllNum");
		String chkDate = msg.getString("ChkDate");
        String chkAcctOrd=msg.getString("ChkAcctOrd");
        logger.info("�������ڣ�"+chkDate);
        logger.info("���Σ�"+chkAcctOrd);
        logger.info("�ܱ�����"+AllNum);
        if(Integer.parseInt(AllNum) == getAllNum(chkDate,chkAcctOrd)){
        	return SUCCESS;
        }else	{
        	return FAIL;
        } 	        	        
    }
	 
	 public int getAllNum(String chkDate,String chkAcctOrd) throws SQLException{
	    	String sql="";
	    	sql = "select count(*) from paycheckdetail where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "'";
			int AllNum = DBUtil.queryForInt(sql);
			return AllNum;
	    }
}