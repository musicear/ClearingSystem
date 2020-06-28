package resoft.tips.action;

import resoft.tips.util.SaveRecvLogUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存接收报文日志</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 12:25:51
 */
public class SaveRecvLog implements Action {
    public int execute(Message msg) throws Exception {
        //是否有重复记录。有则先删除
        String msgId = msg.getString("//CFX/HEAD/MsgID");
        String workDate = msg.getString("//CFX/HEAD/WorkDate");
        String msgRef = msg.getString("//CFX/HEAD/MsgRef");
        String msgNo = msg.getString("//CFX/HEAD/MsgNo");  
        String filePath = msg.getString("tranCode"); 
        SaveRecvLogUtil.saveRecvLog(msgId,workDate,msgRef,msgNo,filePath,"0");
        return SUCCESS;
    }
}
