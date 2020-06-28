package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检查税种</p>
 * Author: liguoyin
 * Date: 2007-10-13
 * Time: 0:22:46
 */
public class QueryTaxTypeName implements Action {
    public int execute(Message msg) throws Exception {
     String taxTypeCode=msg.getString("TaxTypeCode").trim();    
     String taxOrgType=msg.getString("TaxOrgType").trim();
     String sql="select TaxTypeName from TaxTypeMng where TaxTypeCode='"+taxTypeCode+"' and TaxOrgType='"+taxOrgType+"'";
     List rowSets=QueryUtil.queryRowSet(sql);
     if(rowSets.size()>0){
    	 Map row=(Map)rowSets.get(0);
    	 msg.set("ReturnResult", "Y");
    	 msg.set("AddWord", "查询成功");
    	 msg.set("TaxTypeName", (String)row.get("TaxTypeName"));
     }else{
    	 msg.set("ReturnResult", "N");
    	 msg.set("AddWord", "查询不到");
     }
     return SUCCESS;
    }
}
