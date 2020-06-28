package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新批量包明细扣款库状态</p>
 * Author: liguoyin
 * Date: 2007-6-12
 * Time: 23:02:58
 * upateTime 2007-09-07 by zhuchangwu
 */
public class UpdatePackDetail implements Action {
    
	public int execute(Message msg) throws Exception {
        
    	String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String traNo = msg.getString("TraNo");
        //yangyuanxu add 
        String payeeBankNo = msg.getString("payeeBankNo");
        System.out.println("payeeBankNo is:"+payeeBankNo);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String result = msg.getString("Result");
        String addWord = msg.getString("AddWord");
        //yangyuanxu update
        String sql = " update BatchPackDetail set TR_CODE=#TR_CODE#,MSG_DATA=#MSG_DATA#,VCH_NO=#VCH_NO#,JRN_NO=#JRN_NO#,FLAG=#FLAG#,taxDate=#TaxDate#,result=#Result#,IADAC_DAT=#IADAC_DAT#,addWord=#AddWord# ," +
                     " bankTraNo=#BankTraNo#,bankTraDate=#BankTraDate#,bankTraTime=#BankTraTime#,teller=#Teller#,taxPayCode=#taxPayCode# "+
                     " where taxOrgCode=#TaxOrgCode# and entrustDate=#EntrustDate# and packNo=#PackNo# and traNo=#TraNo# and payeeBankNo=#payeeBankNo#";
        Map params = new HashMap();
        
        params.put("JRN_NO", msg.get("JRN_NO")==null?"":msg.get("JRN_NO"));
        params.put("VCH_NO", msg.get("VCH_NO")==null?"":msg.get("VCH_NO"));
        params.put("MSG_DATA", msg.get("MSG_DATA")==null?"":msg.get("MSG_DATA"));
        params.put("TR_CODE", msg.get("TR_CODE")==null?"":msg.get("TR_CODE"));
        params.put("FLAG", msg.get("FLAG")==null?"":msg.get("FLAG"));
        params.put("IADAC_DAT", msg.get("IADAC_DAT")==null?"":msg.get("IADAC_DAT"));
        
        params.put("TaxDate",df.format(new Date()));
        params.put("Result",result);
        params.put("AddWord",addWord);
        params.put("TaxOrgCode",taxOrgCode);
        params.put("EntrustDate",entrustDate);
        params.put("PackNo",packNo);
        params.put("TraNo",traNo);
        params.put("Teller","");
        params.put("BankTraNo",msg.get("bankTraNo")==null?"":msg.get("bankTraNo"));
        params.put("BankTraDate",msg.get("bankTraDate")==null?"":msg.get("bankTraDate"));
        params.put("BankTraTime",msg.get("bankTraTime")==null?"":msg.get("bankTraTime"));
        params.put("taxPayCode", msg.get("TaxPayCode")==null?"":msg.get("TaxPayCode"));
        //yangyuanxu add
        params.put("payeeBankNo",payeeBankNo);
        
        DBUtil.executeUpdate(sql,params);
        
        return SUCCESS;
    }
}
