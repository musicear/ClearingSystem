package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置"银行对账信息重发请求"信息</p>
 * Author: liguoyin
 * Date: 2007-8-13
 * Time: 22:37:30
 */
public class SetGetMsg9112Info implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/GetMsg9112/OriPayeeBankNo",msg.getString("OriPayeeBankNo"));
        msg.set("//CFX/MSG/GetMsg9112/OriChkDate",msg.getString("OriChkDate"));
        msg.set("//CFX/MSG/GetMsg9112/OriChkAcctOrd",msg.getString("OriChkAcctOrd"));
        msg.set("//CFX/MSG/GetMsg9112/SendOrgCode","102100009980");
        return SUCCESS;
    }
}
