package resoft.tips.util;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import resoft.basLink.util.DBUtil;

/**
 * <p>保存接受日志</p>
 * Author: liguoyin
 * Date: 2007-6-11
 * Time: 2:08:11
 */
public class SaveRecvLogUtil {
    public static void saveRecvLog(String msgId,String workDate,String msgRef,String msgNo,String filePath,String logFlag) throws SQLException {
        String sql = "select count(*) from RecvLogs where workDate='" + workDate + "' and msgId='" + msgId + "'";
        int cnt = DBUtil.queryForInt(sql);
        if(cnt>0) {
            DBUtil.executeUpdate("delete from RecvLogs where workDate='" + workDate + "' and msgId='" + msgId + "'" );
        }
        Map params = new HashMap();
        params.put("msgId",msgId);
        params.put("workDate",workDate);
        params.put("msgRef",msgRef);
        params.put("msgNo",msgNo);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        params.put("recvTime",df.format(new Date()));
        params.put("funName",filePath);
        params.put("logFlag",logFlag);
        DBUtil.insert("recvLogs",params);
    }
}
