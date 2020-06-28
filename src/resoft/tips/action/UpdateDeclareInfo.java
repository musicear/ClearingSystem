package resoft.tips.action;

import java.util.HashMap;
import java.util.Map;

import resoft.basLink.util.DBUtil;
//import resoft.xlink.comm.Packager;
//import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新银行端缴款信息</p>
 * Author: tengyang
 * Date: 2007-8-20
 * Time: 23:30:39
 */
public class UpdateDeclareInfo implements Action {
    public int execute(Message msg) throws Exception {
        Map params = new HashMap();        

        params.put("result",msg.get("Result"));//msg.get(getResultNodePath())
        params.put("addWord",msg.get("AddWord"));//msg.get(getAddWordNodePath())
        params.put("bankTraNo",msg.get("bankTraNo")==null?"":msg.get("bankTraNo"));
        params.put("bankTraDate",msg.get("bankTraDate")==null?"":msg.get("bankTraDate"));
        params.put("bankTraTime",msg.get("bankTraTime")==null?"":msg.get("bankTraTime"));
        params.put("taxDate", msg.get("bankTraDate")==null?"":msg.get("bankTraDate"));
        params.put("traNo", msg.getString("OriTraNo"));
        params.put("taxOrgCode", msg.getString("OriTaxOrgCode"));
        params.put("entrustDate", msg.getString("OriEntrustDate"));
        params.put("teller",msg.getString("teller"));
        String result=msg.getString("Result");
        String addWord=msg.getString("AddWord");
        String bankTraNo=msg.getString("bankTraNo")==null?"":msg.getString("bankTraNo");
        String bankTraDate=msg.getString("bankTraDate")==null?"":msg.getString("bankTraDate");
        String bankTraTime=msg.getString("bankTraTime")==null?"":msg.getString("bankTraTime");
        String taxDate=msg.getString("taxDate")==null?"":msg.getString("taxDate");
          

        String sql = "update DeclareInfo set TaxDate=#taxDate#,teller=#teller#,"
                + " Result=#result#,AddWord=#addWord#,bankTraNo=#bankTraNo#, bankTraDate=#bankTraDate#,bankTraTime=#bankTraTime#"
                + " where traNo=#traNo# and taxOrgCode=#taxOrgCode# "
                + " and entrustDate=#entrustDate#";
        DBUtil.executeUpdate(sql, params);

//        }
        return SUCCESS;
    }
}
