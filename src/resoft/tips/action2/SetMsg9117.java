package resoft.tips.action2;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 构造9117报文
 * @author chenlujia
 *
 */
public class SetMsg9117 implements Action {
	public int execute(Message msg) throws Exception {
		String oriPackNo = (String)(msg.get("oriPackNo"));
		String oriPackMsgNo = (String)(msg.get("oriPackMsgNo"));
		String oriChkDate = (String)(msg.get("oriChkDate"));
		
		msg.set("//CFX/MSG/GetMsg9117/OriPackNo", oriPackNo);
		msg.set("//CFX/MSG/GetMsg9117/OriPackMsgNo", oriPackMsgNo);
		msg.set("//CFX/MSG/GetMsg9117/OriChkDate", oriChkDate);
		msg.set("//CFX/MSG/GetMsg9117/OrgType", "3");
		 return SUCCESS;
	}
}
