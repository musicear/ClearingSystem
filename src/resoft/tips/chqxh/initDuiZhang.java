package resoft.tips.chqxh;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>初始化对账信息</p>
 * Author: liwei
 * Date: 2007-10-18
 * Time: 18:36:08
 */
public class initDuiZhang implements Action {
	String chkDate="",chkAcctOrd="";
    public int execute(Message msg) throws Exception {    	
    	//初始化数据     	
    	String chkDate=msg.getString("ChkDate");
    	String chkAcctOrd=msg.getString("ChkAcctOrd");
    	String chkAcctType=msg.getString("ChkAcctType");
    	if (chkDate==null||chkDate.equals("")) {    		
    		return FAIL;
    	}else if (chkAcctOrd==null||chkAcctOrd.equals("")) {
    		return FAIL;
    	}else if (chkAcctType==null||chkAcctType.equals("")) {
    		return FAIL;
    	}
    	
    	return SUCCESS;
    }
}
