package resoft.tips.action;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>三方协议查询</p>
 * Author: liwei
 * Date: 2007-07-11
 * Time: 10:10:10
 */
public class QueryProveInfoByTips implements Action {
	 public int execute(Message msg) throws Exception {		  	          		    
		
		//msg.set("//CFX/MSG/ProveReturn9115/TaxPayCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode"));
		//msg.set("//CFX/MSG/ProveReturn9115/TaxOrgCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode"));
		//msg.set("//CFX/MSG/ProveReturn9115/TaxPayName",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayName"));
		//msg.set("//CFX/MSG/ProveReturn9115/PayBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayBkCode"));
		//msg.set("//CFX/MSG/ProveReturn9115/PayOpBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayOpBkCode"));    		
		//msg.set("//CFX/MSG/ProveReturn9115/PayAcct",msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"));
		//msg.set("//CFX/MSG/ProveReturn9115/HandOrgName",msg.getString("//CFX/MSG/ProveInfo9114/HandOrgName"));
		//msg.set("//CFX/MSG/ProveReturn9115/ProtocolNo",msg.getString("//CFX/MSG/ProveInfo9114/ProtocolNo"));
    	
        return SUCCESS;
    }
}
