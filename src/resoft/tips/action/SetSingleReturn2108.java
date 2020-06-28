package resoft.tips.action;

import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置银行申报扣税回执信息</p>
 * Author: liguoyin
 * Date: 2007-8-16
 * Time: 18:37:44
 */
public class SetSingleReturn2108 implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/SingleReturn2108/TaxVouNo",msg.get("TaxVouNo"));
        msg.set("//CFX/MSG/SingleReturn2108/OriTaxOrgCode",msg.get("OriTaxOrgCode"));
        msg.set("//CFX/MSG/SingleReturn2108/OriEntrustDate",msg.get("OriEntrustDate"));
        msg.set("//CFX/MSG/SingleReturn2108/OriTraNo",msg.get("OriTraNo"));
        msg.set("//CFX/MSG/SingleReturn2108/TaxDate", DateTimeUtil.getDateString());
        msg.set("//CFX/MSG/SingleReturn2108/Result", msg.get("Result"));
        msg.set("//CFX/MSG/SingleReturn2108/AddWord", msg.get("AddWord"));
        return SUCCESS;
    }
}
