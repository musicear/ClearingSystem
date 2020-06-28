package resoft.tips.action;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import resoft.xlink.impl.DefaultMessage;
import resoft.basLink.util.DBUtil;
//import resoft.basLink.util.MessageSendException;
//import resoft.basLink.util.MessageSender;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

//import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>三方协议删除</p>
 * Author: zhuchangwu
 * Date: 2007-08-23
 * Time: 10:10:10
 */

public class RemoveProveInfo implements Action {
//    private static final Log logger = LogFactory.getLog(RemoveProveInfo.class);

    public int execute(Message msg) throws Exception {
        String TaxPayCode = msg.getString("TaxPayCode");
        String ProtocolNo = msg.getString("ProtocolNo");
        String AddWord = msg.getString("AddWord");
        String verifyResult=msg.getString("verifyResult");
        String EnabledFlag=msg.getString("EnabledFlag");
        try{
            Map params = new HashMap();
//            params.put("RemoveTeller", msg.get("RemoveTeller"));//泉州固定为999999
//            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
//            params.put("RemoveTime", dtFormat.format(new Date()));
            params.put("EnabledFlag", EnabledFlag);
            params.put("verifyResult", verifyResult);
            params.put("AddWord", AddWord);
            DBUtil.executeUpdate("update ProveInfo set EnabledFlag=#EnabledFlag#,verifyResult=#verifyResult#,AddWord=#AddWord# where  TaxPayCode='" + TaxPayCode + "' and protocolNo='" + ProtocolNo + "' ", params);
//            msg.set("ReturnResult", "Y");
//            msg.set("AddWord", "协议删除成功");
            return SUCCESS;
        }catch(Exception e){
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "撤销失败");
            return FAIL;
        }

    }


}
