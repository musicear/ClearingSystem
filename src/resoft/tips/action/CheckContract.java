package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Message;

/**
 * <p>检验账户是否签约</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 12:43:11
 */
public class CheckContract extends AbstractSyncAction {
    
	private static final Log logger = LogFactory.getLog(CheckContract.class);
    
	public int execute(Message msg) throws Exception {
        
		String taxOrgCode = msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
        String payAcct = msg.getString("//CFX/MSG/Payment3001/PayAcct");
        String protocolNo = msg.getString("//CFX/MSG/Payment3001/ProtocolNo");
        
        String sql =  " select taxPayCode,AcctSeq from ProveInfo where verifyResult='0' and EnabledFlag='Y' and taxOrgCode='" + taxOrgCode +  "' and payAcct='" + payAcct + "' and protocolNo='" + protocolNo + "'";
        logger.info("扣税验证协议状态 SQL is:"+sql);        
        List rowSet = QueryUtil.queryRowSet(sql);
        
        if( rowSet.size()==0 ) { //协议未通过验证             
            msg.set(getResultNodePath(),"24009");
            msg.set(getAddWordNodePath(),"账户未签约");
            
            msg.set("AddWord", "账户未签约");
            msg.set("Result", "24009");
            
            return FAIL;
        } else {
            Map row = (Map) rowSet.get(0);
            msg.set("TaxPayCode",row.get("taxPayCode"));
            msg.set("AcctSeq",row.get("acctSeq"));
            return SUCCESS;
        }
       
    }

}
