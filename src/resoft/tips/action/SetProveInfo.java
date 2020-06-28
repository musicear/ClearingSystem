package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置发送TIPS报文“三方协议签订请求”报文</p>
 * Author: liwei
 * Date: 2007-07-09
 * Time: 15:06:06
 */
public class SetProveInfo implements Action {
    public int execute(Message msg) throws Exception {

        msg.set("//CFX/MSG/ProveInfo9114/VCSign", msg.get("VCSign"));
        //String orgCode = conf.getProperty("general","BankSrcNodeCode");
        msg.set("//CFX/MSG/ProveInfo9114/TaxOrgCode", msg.get("TaxOrgCode"));
        msg.set("//CFX/MSG/ProveInfo9114/TaxPayName", msg.get("TaxPayName"));
        msg.set("//CFX/MSG/ProveInfo9114/TaxPayCode", msg.get("TaxPayCode"));
        msg.set("//CFX/MSG/ProveInfo9114/PayOpBkCode", msg.get("PayOpBkCode")==null?"1111":msg.get("PayOpBkCode"));//TIPS不做控制,但是不能为空
        msg.set("//CFX/MSG/ProveInfo9114/PayBkCode", msg.get("PayBkCode"));    
        msg.set("//CFX/MSG/ProveInfo9114/PayAcct", msg.get("PayAcct"));
        msg.set("//CFX/MSG/ProveInfo9114/HandOrgName", msg.get("HandOrgName")==null?msg.get("PayAcctName"):msg.get("HandOrgName"));
        msg.set("//CFX/MSG/ProveInfo9114/ProtocolNo", msg.get("ProtocolNo"));

        //预先设定发送失败。等成功之后再修改状态
        msg.set("ReturnResult", "N");
        msg.set("AddWord","税务端无响应，请联系管理员或稍后重发");

        return SUCCESS;
    }
}


