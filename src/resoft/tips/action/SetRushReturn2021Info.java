package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置实时冲正回执信息</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 19:25:29
 */
public class SetRushReturn2021Info implements Action {

    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/RushReturn2021/TaxOrgCode",msg.get("//CFX/MSG/RushApply1021/TaxOrgCode"));
        msg.set("//CFX/MSG/RushReturn2021/OriCancelNo",msg.get("//CFX/MSG/RushApply1021/CancelNo"));
        msg.set("//CFX/MSG/RushReturn2021/OriCancelDate",msg.get("//CFX/MSG/RushApply1021/EntrustDate"));
        msg.set("//CFX/MSG/RushReturn2021/OriTraNo",msg.get("//CFX/MSG/RushApply1021/OriTraNo"));
        msg.set("//CFX/MSG/RushReturn2021/OriEntrustDate",msg.get("//CFX/MSG/RushApply1021/OriEntrustDate"));        
        return SUCCESS;
    }
}
