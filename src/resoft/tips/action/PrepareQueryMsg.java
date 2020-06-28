package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>Æ´´Õ½»Ò××´Ì¬²éÑ¯</p>
 * Author: chenzifei
 * Date: 2007-9-18
 * Time: 10:40:06
 */
public class PrepareQueryMsg implements Action{

	public int execute(Message msg) throws Exception {
		msg.set("//CFX/MSG/TraStatusCheck9003/SendOrgCode", msg.getString("SendOrgCode"));
		msg.set("//CFX/MSG/TraStatusCheck9003/SearchType", msg.getString("SearchType"));
		msg.set("//CFX/MSG/TraStatusCheck9003/OriMsgNo", msg.getString("OriMsgNo"));
		msg.set("//CFX/MSG/TraStatusCheck9003/OriEntrustDate", msg.getString("OriEntrustDate"));
		msg.set("//CFX/MSG/TraStatusCheck9003/OriPackNo", msg.getString("OriPackNo"));
		msg.set("//CFX/MSG/TraStatusCheck9003/OriTraNo", msg.getString("OriTraNo"));
		return SUCCESS;
	}

}
