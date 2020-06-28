package resoft.tips.action;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>帐号查询</p>
 * Author: zhuchangwu
 * Date: 2007-7-5
 * Time: 16:40:06
 */
public class QueryAcct implements Action {
    private static final Log logger = LogFactory.getLog(QueryAcct.class);

    public int execute(Message msg) throws Exception {
        String acctNo = msg.getString("AcctNo");
        MessageSender sender = MessageSenderUtil.getMessageSender();
        Message sendMsg = new DefaultMessage();
        sendMsg.set("TransCode", "T8002");//原交易码为2100
        sendMsg.set("交易码","T8002");//原交易码为2100
        sendMsg.set("AcctNo", acctNo);

        Message returnMsg;
        try {
           returnMsg = sender.send(sendMsg);
            //chenzifei 9月20日改,AcctName的发送前应该在acctStatus的判断之后.
            //msg.set("AcctName", returnMsg.getString("AcctName"));
            msg.set("OpenBankCode", returnMsg.getString("OpenBankCode"));
            msg.set("AcctStatus", returnMsg.getString("AcctStatus"));
            msg.set("IdType", returnMsg.getString("IdType"));
            msg.set("IdNo", returnMsg.getString("IdNo"));
            String acctStatus =returnMsg.getString("AcctStatus");
            if(!"0".equals(acctStatus)){
            	msg.set("ReturnResult", "N");
              if (acctStatus.endsWith("1")) {
                 msg.set("AddWord", "账户不存在");
              } else if (acctStatus.equals("2")) {
                msg.set("AddWord", "已冻结");
              } else if (acctStatus.equals("3")) {
                 msg.set("AddWord", "已注销");
              } else if (acctStatus.equals("4")) {
                 msg.set("AddWord", "其他错误");
              }
              return FAIL;           	
            }else{
            	//放在这个位置.
            	msg.set("AcctName", returnMsg.getString("AcctName"));
            	return SUCCESS;
            }
        } catch (MessageSendException e) {
            logger.error("发送银行核心系统失败", e);
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "发送银行核心系统失败");
            return FAIL;
        }

        
    }
}


