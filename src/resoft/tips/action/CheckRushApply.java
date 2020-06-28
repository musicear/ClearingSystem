package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Message;

/**
 * <p>检查是否已冲正</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 13:04:54
 */
public class CheckRushApply extends AbstractSyncAction {
    public int execute(Message msg) throws Exception {
        String traNo = msg.getString("//CFX/MSG/RealHead3001/TraNo");
        String taxOrgCode = msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
        String entrustDate = msg.getString("//CFX/MSG/RealHead3001/EntrustDate");
        String sql = "select count(*) from RushApply where ";
        sql += " taxOrgCode='" + taxOrgCode + "' and oriEntrustDate='" + entrustDate + "' and oriTraNo='" + traNo + "'";
        System.out.println("检查是否已冲正 is........."+sql);
        int cnt = DBUtil.queryForInt(sql);
        if(cnt>0) {
            //已冲正
            msg.set(getResultNodePath(),"24020");
            msg.set(getAddWordNodePath(),"业务已撤销");
            return FAIL;
        } else {
            return SUCCESS;
        }
    }
}
