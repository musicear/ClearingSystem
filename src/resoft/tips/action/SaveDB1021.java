package resoft.tips.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存冲正数据库</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 18:43:45
 */
public class SaveDB1021 implements Action {
    private static final Log logger = LogFactory.getLog(SaveDB1021.class);

    public int execute(Message msg) throws Exception {
        //是否已保存此冲正记录
        String cancelNo = msg.getString("//CFX/MSG/RushApply1021/CancelNo");
        String taxOrgCode = msg.getString("//CFX/MSG/RushApply1021/TaxOrgCode");
        String entrustDate = msg.getString("//CFX/MSG/RushApply1021/EntrustDate");
        try {
            String sql = "select count(*) from RushApply where cancelNo='" + cancelNo + "'" +
                    " and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + entrustDate + "'";
            System.out.println("the sql is:"+sql);
            int cnt = DBUtil.queryForInt(sql);
            if (cnt > 0) {
                sql = "delete from RushApply where  cancelNo='" + cancelNo + "'" +
                        " and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + entrustDate + "'";
                DBUtil.executeUpdate(sql);
            }
        } catch (SQLException e) {
            logger.error("query data error", e);
            return FAIL;
        }
        //保存冲正信息
        Map params = new HashMap();
        params.put("cancelNo", cancelNo);
        params.put("entrustDate", entrustDate);
        params.put("taxOrgCode", taxOrgCode);
        params.put("oriEntrustDate", msg.getString("//CFX/MSG/RushApply1021/OriEntrustDate"));
        params.put("oriTraNo", msg.getString("//CFX/MSG/RushApply1021/OriTraNo"));
        params.put("cancelReason", msg.getString("//CFX/MSG/RushApply1021/CancelReason"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        params.put("recvTime", df.format(new Date()));
        params.put("cancelAnswer", "1");
        params.put("addWord", "冲正成功");
        DBUtil.insert("RushApply", params);
        return SUCCESS;
    }
}
