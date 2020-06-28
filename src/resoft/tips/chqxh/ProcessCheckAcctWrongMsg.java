package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>重庆信合处理错误信息</p>
 * Author: liwei
 * Date: 2007-10-11
 * Time: 09:36:06
 */
public class ProcessCheckAcctWrongMsg implements Action{	
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWrongMsg.class);
	public int execute(Message msg) throws Exception {
        //对账信息
		String tipsDate = msg.getString("ChkDate");        		//对账日期
		String chkAcctOrd=msg.getString("ChkAcctOrd");			//对账批次
        String chkAcctType=msg.getString("ChkAcctType");		//对账类型
        Map params=new HashMap();
        if(chkAcctType.equals("1")){		//0:日间;1:日切
        	//删除旧的数据
        	DBUtil.executeUpdate("delete from adjustAcct where chkdate='"+tipsDate+"' ");        	
        	//处理实时        	
    		String sql = "select * from RealtimePayment where checkStatus='0' and tipsWorkDate='"+tipsDate+"' order by traNo";
    		logger.info("前置处理实时差错信息："+sql);
            List realTimePayLists = QueryUtil.queryRowSet(sql);
            for (int i=0;i<realTimePayLists.size();i++){            	    	
	    		Map row=(Map)realTimePayLists.get(i);  
	    		params.put("chkDate", tipsDate);
	            params.put("payOpBkCode", (String) row.get("payOpBkCode"));
	            params.put("entrustDate", (String) row.get("entrustDate"));
	            params.put("bankTraDate", (String) row.get("bankTraDate"));
	            params.put("bankTraNo", (String) row.get("bankTraNo"));
	            params.put("taxOrgCode", (String) row.get("taxOrgCode"));
	            params.put("payAcct", (String) row.get("payAcct"));
	            params.put("traAmt", new Double((String) row.get("traAmt")));
	            params.put("packNo", "");
	            params.put("traNo", (String)row.get("traNo"));
	            //错误状态明确的信息不需要冲帐
	            if ((String)row.get("result")!=null && ((String)row.get("result")).equals("")) {
	            	params.put("adjustStatus", "1");
	            }else {
	            	params.put("adjustStatus", "0");
	            }
	            //已经冲帐成功的也无需冲帐
	            if (((String)row.get("revokeStatus")).equals("2") ) {
	            	params.put("adjustStatus", "1");
	            }	            
	            params.put("reason", (String)row.get("addWord"));
	            DBUtil.insert("AdjustAcct", params);
            }
        	//处理批量        	
    		sql = "select * from BatchPackDetail where checkStatus='0' and tipsWorkDate='"+tipsDate+"' order by traNo";
    		logger.info("前置处理批量差错信息："+sql);
            List batchLists = QueryUtil.queryRowSet(sql);
            for (int i=0;i<batchLists.size();i++){            	
	    		Map row=(Map)batchLists.get(i);	
	    		params.put("chkDate", tipsDate);
	            params.put("payOpBkCode", (String) row.get("payOpBkCode"));
	            params.put("entrustDate", (String) row.get("entrustDate"));
	            params.put("bankTraDate", (String) row.get("bankTraDate"));
	            params.put("bankTraNo", (String) row.get("bankTraNo"));
	            params.put("taxOrgCode", (String) row.get("taxOrgCode"));
	            params.put("payAcct", (String) row.get("payAcct"));
	            params.put("traAmt", new Double((String) row.get("traAmt")));
	            params.put("packNo", (String)row.get("packNo"));
	            params.put("traNo", (String)row.get("traNo"));
	            //错误状态明确的信息不需要冲帐
	            if ((String)row.get("result")!=null && ((String)row.get("result")).trim().equals("")) {
	            	params.put("adjustStatus", "1");
	            }else {
	            	params.put("adjustStatus", "0");
	            }
	            //已经冲帐成功的无需冲帐
	            if (((String)row.get("revokeStatus")).equals("2") ) {
	            	params.put("adjustStatus", "1");
	            }
	            params.put("reason", (String)row.get("addWord"));
	            DBUtil.insert("AdjustAcct", params);	    		
            }
        }
        return SUCCESS;
    }		
}
