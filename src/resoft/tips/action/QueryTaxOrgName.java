package resoft.tips.action;

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
        String taxOrgCode = msg.getString("TaxOrgCode");
        String taxOrgName = "";
        try {
            taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + taxOrgCode + "'");
        } catch(Exception e) {
            logger.error("查询失败",e);
        }

        String returnResult = "N";
        if(taxOrgName!=null && !taxOrgName.equals("")) {
            returnResult = "Y";
        }
        msg.set("ReturnResult",returnResult);
        msg.set("TaxOrgName",taxOrgName);
        return SUCCESS;
    }
}
