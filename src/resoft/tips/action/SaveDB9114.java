package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
//import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存三方协议信息</p>
 * Author: liwei
 * Date: 2007-07-12
 * Time: 15:00:00
 */
public class SaveDB9114 implements Action {
    private static final Log logger = LogFactory.getLog(SaveDB9114.class);

    public int execute(Message msg) throws Exception {
    	String payAcct=msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"); 	   
        String taxPayCode=msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode");
        String protocolNo=msg.getString("//CFX/MSG/ProveInfo9114/ProtocolNo");
        String taxOrgCode=msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode");
        String taxPayName=msg.getString("//CFX/MSG/ProveInfo9114/TaxPayName");
        String payOpBkCode=msg.getString("//CFX/MSG/ProveInfo9114/PayOpBkCode");
        String payBkCode=msg.getString("//CFX/MSG/ProveInfo9114/PayBkCode");
        String handOrgName=msg.getString("//CFX/MSG/ProveInfo9114/HandOrgName");
        
        
        //保存三方协议信息        
        Map params=new HashMap();
        params.put("payAcct",payAcct);
        params.put("taxPayCode",taxPayCode);
        params.put("protocolNo",protocolNo);
        params.put("taxOrgCode",taxOrgCode);
        params.put("payOpBkCode",payOpBkCode);
        params.put("payBkCode",payBkCode);
        params.put("handOrgName",handOrgName);
        params.put("taxPayName", taxPayName);
        
        params.put("handOrgName",handOrgName);
        params.put("verifyResult","0");	//审核通过 0：通过 ；1：拒绝        
        params.put("EnabledFlag","N");	//无效        
        DateFormat dt=new SimpleDateFormat("yyyyMMddhhkkss");
        params.put("sendTime",dt.format(new Date()));
        params.put("inputTeller", "TIPS");
        
        logger.info(params);
        DBUtil.insert("ProveInfo", params);
        
        msg.set("//CFX/MSG/ProveReturn9115/VCResult", "0");
		msg.set("//CFX/MSG/ProveReturn9115/AddWord", "验证通过，协议已可以使用");
        
        return SUCCESS;
    }
}
