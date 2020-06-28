package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>查询征收机关名称</p>
 * Author: liguoyin
 * Date: 2007-7-5
 * Time: 10:38:10
 */
public class QueryTaxOrgName implements Action {
    private static final Log logger = LogFactory.getLog(QueryTaxOrgName.class);
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode").trim();
        String taxOrgName = "";
        try {
        	String showmsg="select taxOrgName from taxOrgMng where taxOrgCode='" + taxOrgCode + "'";
            taxOrgName = DBUtil.queryForString(showmsg);
            logger.info("查询语句"+showmsg);
        } catch(Exception e) {
            logger.error("查询失败",e);
            msg.set("AddWord",e);
        }

        String returnResult = "N";
        if(taxOrgName!=null && !taxOrgName.equals("")) {
            returnResult = "Y";
            msg.set("AddWord","查询成功");
        }
        else
        {
        	
        	msg.set("AddWord","无此机关代码");
        }
        msg.set("ReturnResult",returnResult);
        msg.set("TaxOrgName",taxOrgName);
        return SUCCESS;
    }
}
