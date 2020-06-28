package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>帐号查询</p>
 * Author: liwei
 * Date: 2007-07-11
 * Time: 10:10:10
 */
public class CheckAcctStatus implements Action {
	 public int execute(Message msg) throws Exception {
		String payAcct=msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"); 	        
        if(!payAcct.equals("555")){
        	msg.set("//CFX/MSG/ProveReturn9115/VCResult","0");
        	msg.set("//CFX/MSG/ProveReturn9115/AddWord","帐号正常");
        	return SUCCESS;
        }else	{
        	msg.set("//CFX/MSG/ProveReturn9115/VCResult","1");
        	msg.set("//CFX/MSG/ProveReturn9115/AddWord","帐号不正常");
        	return FAIL;
        } 	        	        
    }
}


