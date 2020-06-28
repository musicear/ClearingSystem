package resoft.tips.chqsh;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.SaveTaxCheck;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检查是否完成全部日切对账</p>
 * Author: yangyuanxu
 * Date: 2007-8-8
 * Time: 15:55:07
 */
public class CheckIfAcctAll implements Action {
	private static final Log logger = LogFactory.getLog(SaveTaxCheck.class);
	private static Object locker = new Object();
	
	public int execute(Message msg) throws Exception {
        
         String chkAcctType=msg.getString("ChkAcctType");		//对账类型
		
		if(chkAcctType.equals("0")){							//0:日间;1:日切
			//日间不与行内进行核对
			return SUCCESS;
		}
		else
		  if (checkStatus()) {
			 return SUCCESS;
		   } else {
			 logger.error("分行对账仍未结束，。");
			 return FAIL;
		  }
}
	 /**
	 * 检查对帐包中明细笔数是否与总笔数一致
	 * 
	 * @param msg
	 * @return
	 * @throws SQLException
	 */
	private boolean checkStatus() throws SQLException {
		String sql="";
		sql = "select count(*) from bank_relation where status='0' ";
		String batchNum = DBUtil.queryForString(sql);
		batchNum=batchNum.trim();
		int Num = Integer.parseInt(batchNum);
		
		logger.info(" 明细总金额:" + Num);
		//return (allNum.equals(detailNum) && allAmt.equals(detailAmt));
		return ( Num == 0 );
	}
}
