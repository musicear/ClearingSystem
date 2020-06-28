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
 * <p>检查是否有单笔止付</p>
 * Author: zhuchangwu
 * Date: 2007-6-12
 * Time: 22:58:11
 */
public class CheckDetailDelApply implements Action {
	private static final Log logger = LogFactory.getLog(CheckDetailDelApply.class); 
	
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String TraNo = msg.getString("TraNo");
        String sql = "select tipsWorkDate,traAmt from BatchPackDetail " +
                " where STOPFLAG='N' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +
                "' and packNo='" + packNo + "' and traNo='"+TraNo+"'";
        List rowSet = QueryUtil.queryRowSet(sql);
        if(rowSet.size()>0){
        	logger.info("             has package           ");
        	Map row=(Map)rowSet.get(0);
        	msg.set("WorkDate", row.get("tipsWorkDate"));
        	msg.set("traAmt", row.get("traAmt"));
            return SUCCESS;
        }
        else{
        	logger.info("                no package           ");
            msg.set("Result", "24020");
            msg.set("AddWord", "已经止付");
        	return FAIL;
        }
    }
}
