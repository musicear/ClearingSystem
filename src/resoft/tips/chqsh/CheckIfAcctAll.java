package resoft.tips.chqsh;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.SaveTaxCheck;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Ƿ����ȫ�����ж���</p>
 * Author: yangyuanxu
 * Date: 2007-8-8
 * Time: 15:55:07
 */
public class CheckIfAcctAll implements Action {
	private static final Log logger = LogFactory.getLog(SaveTaxCheck.class);
	private static Object locker = new Object();
	
	public int execute(Message msg) throws Exception {
        
         String chkAcctType=msg.getString("ChkAcctType");		//��������
		
		if(chkAcctType.equals("0")){							//0:�ռ�;1:����
			//�ռ䲻�����ڽ��к˶�
			return SUCCESS;
		}
		else
		  if (checkStatus()) {
			 return SUCCESS;
		   } else {
			 logger.error("���ж�����δ��������");
			 return FAIL;
		  }
}
	 /**
	 * �����ʰ�����ϸ�����Ƿ����ܱ���һ��
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
		
		logger.info(" ��ϸ�ܽ��:" + Num);
		//return (allNum.equals(detailNum) && allAmt.equals(detailAmt));
		return ( Num == 0 );
	}
}
