package resoft.tips.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>查到生效日期到期的记录，更新到运行表中</p>
 * Author: liwei
 * Date: 2007-11-25
 * Time: 19:56:24
 */
public class UpdateCommonInfo implements Action {
	
//	private static final Log logger = LogFactory.getLog(UpdateCommonInfo.class);
    
	public int execute(Message msg) throws Exception {
        Map dataTypeTableMap = new HashMap();
        dataTypeTableMap.put("101", "TaxOrgMng");
        dataTypeTableMap.put("102", "BankMng");
        dataTypeTableMap.put("103", "NodeMng");
        dataTypeTableMap.put("104", "TreMng");
        dataTypeTableMap.put("108", "TaxTypeMng");
        dataTypeTableMap.put("109", "TaxSubjectMng");

        String today = DateTimeUtil.getDateString();
        String sql = "select distinct updateBatch,dataType,rowNo from CommonInfo where infoKey='EffectDate' and infoValue<='" + today + "' ";
        //logger.info("query CommonInfo sql:"+sql);
        List rowSet = QueryUtil.queryRowSet(sql);
        for (Iterator itr = rowSet.iterator(); itr.hasNext();) {
            Map row = (Map) itr.next();
            String updateBatch = (String) row.get("updateBatch");
            String dataType = (String) row.get("dataType");
            String rowNo = (String) row.get("rowNo");
            String tableName = (String) dataTypeTableMap.get(dataType);
            if (tableName != null) {
                //更新对应表
                sql = "select infoKey,infoValue from CommonInfo where updateBatch='" + updateBatch + "' and dataType='" + dataType + "' and rowNo=" + rowNo;
                //logger.info("process sql:"+sql);
                String operSign = "";
                Map params = new HashMap();
                List keyValueList = QueryUtil.queryRowSet(sql);
                for (Iterator itrKeyValueItr = keyValueList.iterator(); itrKeyValueItr.hasNext();) {
                    Map keyValue = (Map) itrKeyValueItr.next();
                    String key = (String) keyValue.get("infoKey");
                    String value = (String) keyValue.get("infoValue");
                    if (key.equals("OperSign")) {
                        operSign = value;
                    } else {
                        params.put(key, value);
                    }
                }

                String keyCode = "",keyValue;//表主键的值
                if (tableName.equals("TaxOrgMng")) {
                    keyCode = "TaxOrgCode";
                } else if (tableName.equals("BankMng")) {
                    keyCode = "ReckBankNo";
                } else if (tableName.equals("NodeMng")) {
                    keyCode = "NodeCode";
                } else if (tableName.equals("TreMng")) {
                    keyCode = "TreCode";
                }else if (tableName.equals("TaxTypeMng")){
                	keyCode = "TaxTypeCode";
                }else if (tableName.equals("TaxSubjectMng")){
                	keyCode = "TaxSubjectCode";
                }
                keyValue = (String) params.get(keyCode);
                if (operSign.equals("1") || operSign.equals("2")) {
                    //首先删除原记录，再进行增加
                	String sqlSel="";
                	if(tableName.equals("TaxTypeMng")||tableName.equals("TaxSubjectMng")){
                		 sqlSel="select count("+keyCode+") from "+tableName+" where " + keyCode + "='" + keyValue + "' and TaxOrgType='"+(String)params.get("TaxOrgType")+"'";
                		 int count=DBUtil.queryForInt(sqlSel);
                		 if(count<1){
                			 DBUtil.insert(tableName, params);                		
                		 }
                	}else{
                		sqlSel="select count("+keyCode+") from "+tableName+" where " + keyCode + "='" + keyValue + "'";
                		int count=DBUtil.queryForInt(sqlSel);
               		 	if(count<1){
                			DBUtil.insert(tableName, params);
                		}
                	}
                } else if(operSign.equals("3")) {
                    //删除。将记录设置为无效
                	String updateSql ="";
                	if(tableName.equals("TaxTypeMng")||tableName.equals("TaxSubjectMng")){
                		updateSql = "update " + tableName + " set EnabledFlag='N' where " + keyCode + "='" + keyValue + "' and TaxOrgType='"+(String)params.get("TaxOrgType")+"'";
                	}else{                	
                       updateSql = "update " + tableName + " set EnabledFlag='N' where " + keyCode + "='" + keyValue + "'";
                	}                	
                    DBUtil.executeUpdate(updateSql);
                }                               
            }
            
            DBUtil.executeUpdate("delete from CommonInfo where updateBatch='"+updateBatch+"' and dataType='"+dataType+"' and rowNo='"+rowNo+"' ");            
        }             
        return SUCCESS;
    }
	
	
}
