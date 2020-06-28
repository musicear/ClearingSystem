package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
//import resoft.basLink.util.DBUtil;

/**
 * <p>三方协议验证和删除标志</p>
 * Author: liwei
 * Date: 2007-07-29
 * Time: 17:10:10
 */
public class OpFlag implements Action {
	 public int execute(Message msg) throws Exception {
		 //0:三方协议验证;1:三方协议删除
		String opFlag=msg.getString("//CFX/MSG/ProveInfo9114/VCSign");
		
		String taxPayCode=msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode");
        String taxOrgCode=msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode");
        msg.set("taxOrgCode", taxOrgCode);
        msg.set("taxPayCode", taxPayCode);
        
        msg.set("//CFX/MSG/ProveReturn9115/OriSendOrgCode",msg.getString("//CFX/MSG/ProveInfo9114/SendOrgCode"));        
        msg.set("//CFX/MSG/ProveReturn9115/OriEntrustDate",msg.getString("//CFX/MSG/ProveInfo9114/EntrustDate"));
        msg.set("//CFX/MSG/ProveReturn9115/OriVCNo",msg.getString("//CFX/MSG/ProveInfo9114/VCNo"));
        msg.set("//CFX/MSG/ProveReturn9115/VCSign",msg.getString("//CFX/MSG/ProveInfo9114/VCSign"));
        
		
		return Integer.parseInt(opFlag);	        	        
    }
}


