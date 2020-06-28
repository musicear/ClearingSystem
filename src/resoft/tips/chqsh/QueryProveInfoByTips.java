package resoft.tips.chqsh;

import java.util.List;
import java.util.Map;

//import resoft.basLink.util.DBUtil;
import org.zerone.jdbc.QueryUtil;

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
    	
	    List queryList = QueryUtil.queryRowSet("select * from ProveInfo a,BankMng b,taxOrgMng c where a.verifyResult='0' and a.payBkCode=b.ReckBankNo and a.taxOrgCode=c.taxOrgCode and a.taxOrgCode='"+msg.getString("taxOrgCode")+"' and a.taxPayCode='"+msg.getString("taxPayCode")+"' ");
    	if(queryList.size()>0){
    		Map row=(Map)queryList.get(0);
    		msg.set("//CFX/MSG/ProveReturn9115/TaxPayCode",row.get("taxPayCode"));
    		msg.set("//CFX/MSG/ProveReturn9115/TaxOrgCode",row.get("taxOrgCode"));
    		msg.set("//CFX/MSG/ProveReturn9115/TaxPayName",row.get("TaxPayCode"));
    		msg.set("//CFX/MSG/ProveReturn9115/PayBkCode",row.get("ReckBankNo"));
    		msg.set("//CFX/MSG/ProveReturn9115/PayOpBkCode",row.get("payBkCode"));    		
    		msg.set("//CFX/MSG/ProveReturn9115/PayAcct",row.get("payAcct"));
    		msg.set("//CFX/MSG/ProveReturn9115/HandOrgName",row.get("handOrgName"));
    		msg.set("//CFX/MSG/ProveReturn9115/ProtocolNo",row.get("protocolNo"));
    	}   
        return SUCCESS;
    }
}
