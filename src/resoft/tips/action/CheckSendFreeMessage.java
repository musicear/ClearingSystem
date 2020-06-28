package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检索是否有发送自由报文的数据</p>
 * Author:liwei
 * Date: 2007-07-17
 * Time: 11:09:00
 */
public class CheckSendFreeMessage implements Action {
    public int execute(Message msg) throws Exception {
        int count = DBUtil.queryForInt("select count(*) from SentFreeMessage where status='0' ");

    	if (count == 0) {//没有发送数据
            return FAIL;
        } else {
        	
            List freeMsgInfo = QueryUtil.queryRowSet("select * from SentFreeMessage where status='0' ");
            if (freeMsgInfo.size() > 0) {
                Map row = (Map) freeMsgInfo.get(0);                
                msg.set("//CFX/MSG/FreeFormat9105/DesNodeCode", row.get("desNodeCode"));
                msg.set("//CFX/MSG/FreeFormat9105/Content", row.get("content"));

                msg.set("freeMsgId", row.get("ID"));
            }

            return SUCCESS;
        }
    }
}


