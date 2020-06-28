package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

//import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>查询银行端缴款中记录是否存在</p>
 * Author: zhuchangwu
 * Date: 2007-8-20
 * Time: 23:20:03
 */
public class CheckDeclareInfo implements Action {
	private static final Log logger = LogFactory.getLog(CheckDeclareInfo.class);
    public int execute(Message msg) throws Exception {
    	logger.info("IN here");
        String taxVouNo = msg.getString("TaxVouNo");
        String oriTaxOrgCode = msg.getString("OriTaxOrgCode");
        String oriEntrustDate = msg.getString("OriEntrustDate");
        String oriTraNo = msg.getString("OriTraNo");
        
	    String sql = "select tipsWorkDate,payOpBkCode,TraAmt,PayeeAcct,result,PayeeBankNo from DeclareInfo where";
	    sql += " taxVouNo='" + taxVouNo + "' and taxOrgCode='" + oriTaxOrgCode + "' and entrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";

	    List rowSet=QueryUtil.queryRowSet(sql);
	    if(rowSet.size()>0)
	    for(int i=0;i<rowSet.size();i++){
	    	Map row=(Map)rowSet.get(0);
	    	String result=(String)row.get("result");
	    	if("90000".equals(result)){
	        	//重复扣款
	    		msg.set("Result",result);
	            msg.set("ReturnResult","N");
	            msg.set("AddWord","已经缴款");
	            return SUCCESS;
	    	}else{
	    		msg.set("//CFX/HEAD/WorkDate", (String)row.get("tipsWorkDate"));
	    		msg.set("//CFX/MSG/TurnAccount3001/PayOpBkCode", (String)row.get("payOpBkCode"));
	    		msg.set("//CFX/MSG/Payment3001/TraAmt", (String)row.get("TraAmt"));
	    		msg.set("//CFX/MSG/TurnAccount3001/PayeeAcct", (String)row.get("PayeeAcct"));
	    		msg.set("//CFX/MSG/Payment3001/PayAcct", msg.getString("PayAcct"));
	    		msg.set("//CFX/MSG/TurnAccount3001/PayeeBankNo", (String)row.get("PayeeBankNo"));
	    		
	    		msg.set("//CFX/MSG/Payment3001/TaxTypeNum", "1");
	    		return SUCCESS;
	    	}
	    }else
        	//交易不存在
          msg.set("ReturnResult","N");
      	  msg.set("AddWord","交易不存在");
      	  return FAIL;
	    }
      
    }

