package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新处理标志为“扣税完毕”</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 18:51:55
 */
public class UpdateProcFlagAsPayFinish implements Action {
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String sql = "update BatchPackage set procFlag='3',procEndTime='" + DateTimeUtil.getDateTimeString() + "'" +
                " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "'";
        DBUtil.executeUpdate(sql);
        return SUCCESS;
    }
}
