package resoft.tips.action;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class SaveBatchStatus implements Action {
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String bankNo=msg.getString("bankNo")==null?"":msg.getString("bankNo");
        String result=msg.getString("Result");
        String AddWord=msg.getString("AddWord");
        String updateSql = "update BatchPackage set returnDate=#ReturnDate#,result=#Result#,addWord=#AddWord#,bankNo=#BankNo#,BatchFlag=#BatchFlag#" +
        " where taxOrgCode=#TaxOrgCode# and entrustDate=#EntrustDate# and packNo=#PackNo# and procFlag='2' " ;
        Map params = new HashMap();
//        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        params.put("ReturnDate",msg.getString("changeDate"));
        params.put("TaxOrgCode",taxOrgCode);
        params.put("EntrustDate",entrustDate);
        params.put("PackNo",packNo);
        params.put("BankNo",bankNo);
        params.put("Result",result);
        params.put("AddWord",AddWord);
        params.put("BatchFlag","1");
        DBUtil.executeUpdate(updateSql,params);
        return SUCCESS;
    }
}
