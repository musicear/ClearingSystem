package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import resoft.tips.util.DateTimeUtil;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存三方协议签约信息到ProveInfo表</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 12:08:48
 */
public class SaveProveInfo implements Action {
    private static final Log logger = LogFactory.getLog(SaveProveInfo.class);

    public int execute(Message msg) throws Exception {
        Map params = new HashMap();
//        params.put("taxOrgCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode"));
        params.put("taxPayCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode"));
//        params.put("payOpBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayOpBkCode"));
//        params.put("payBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayBkCode"));
        params.put("payAcct",msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"));
//        params.put("TaxPayName",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayName"));
//        String acctSeq = msg.getString("AcctSeq");
//        if(acctSeq!=null && !acctSeq.equals("")) {
//            params.put("AcctSeq",msg.getString("AcctSeq"));
//        }
//
//        params.put("handOrgName",msg.getString("//CFX/MSG/ProveInfo9114/HandOrgName"));
        params.put("protocolNo",msg.getString("//CFX/MSG/ProveInfo9114/ProtocolNo"));
//        params.put("sendTime", DateTimeUtil.getDateTimeString());
        params.put("verifyResult",msg.get("verifyResult"));
        params.put("addWord",msg.getString("AddWord"));
//        params.put("inputTeller",msg.getString("InputTeller"));//泉州固定为999999
        String sql = "update ProveInfo set verifyResult=#verifyResult#,addWord=#AddWord# " +
   " where taxPayCode=#taxPayCode# and payAcct=#payAcct# and protocolNo=#protocolNo# ";
        DBUtil.executeUpdate(sql, params);
        logger.info("保存数据库!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return SUCCESS;
    }
}
