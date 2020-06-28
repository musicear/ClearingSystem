package resoft.tips.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Message;

/**
 * <p> ¸üÐÂ¿Û¿î×´Ì¬ </p>
 * @author liwei
 * @date 2008-06-04
 */
public class UpdateDB3001 extends AbstractSyncAction {
	private static final Log logger = LogFactory.getLog(UpdateDB3001.class);
	public int execute(Message msg) throws RuntimeException {
        
		Map params = new HashMap();        
        params.put("result",msg.get("Result")==null?"":msg.get("Result"));
        params.put("addWord",msg.get("AddWord")==null?"":msg.get("AddWord"));
        params.put("bankTraNo",msg.get("bankTraNo")==null?"":msg.get("bankTraNo")); 
        params.put("bankTraDate",msg.get("bankTraDate")==null?"":msg.get("bankTraDate"));
        params.put("bankTraTime",msg.get("bankTraTime")==null?"":msg.get("bankTraTime"));
        params.put("taxDate", msg.get("//CFX/MSG/SingleReturn2001/TaxDate"));
        params.put("traNo", msg.get("//CFX/MSG/RealHead3001/TraNo"));
        params.put("taxOrgCode", msg.get("//CFX/MSG/RealHead3001/TaxOrgCode"));
        params.put("entrustDate", msg.get("//CFX/MSG/RealHead3001/EntrustDate"));
        //yangyuanxu add
        params.put("payeeBankNo", msg.get("//CFX/MSG/TurnAccount3001/PayeeBankNo"));
        params.put("taxPayCode", msg.get("TaxPayCode")==null?"":msg.get("TaxPayCode"));
        params.put("teller",msg.get("teller")==null?"":msg.get("teller"));
        params.put("JRN_NO", msg.get("JRN_NO")==null?"":msg.get("JRN_NO"));
        params.put("VCH_NO", msg.get("VCH_NO")==null?"":msg.get("VCH_NO"));
        params.put("MSG_DATA", msg.get("MSG_DATA")==null?"":msg.get("MSG_DATA"));
        params.put("TR_CODE", msg.get("TR_CODE")==null?"":msg.get("TR_CODE"));
        params.put("FLAG", msg.get("FLAG")==null?"":msg.get("FLAG"));
        params.put("IADAC_DAT", msg.get("IADAC_DAT")==null?"":msg.get("IADAC_DAT")); 
        String sql = "update RealtimePayment set TaxDate=#taxDate#,teller=#teller#,taxPayCode=#taxPayCode#, "
                + " Result=#result#,AddWord=#addWord#,bankTraNo=#bankTraNo#, bankTraDate=#bankTraDate#,bankTraTime=#bankTraTime#, " 
                + " JRN_NO=#JRN_NO#,VCH_NO=#VCH_NO#,MSG_DATA=#MSG_DATA#,TR_CODE=#TR_CODE#,FLAG=#FLAG#,IADAC_DAT=#IADAC_DAT# "
                + " where traNo=#traNo# and taxOrgCode=#taxOrgCode# "
                + " and entrustDate=#entrustDate# and payeeBankNo=#payeeBankNo#";
        DBUtil.executeUpdate(sql, params); 
        
        return SUCCESS;
        
    }
}
