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
 * <p>�ʺŲ�ѯ</p>
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
        sendMsg.set("TransCode", "T8002");//ԭ������Ϊ2100
        sendMsg.set("������","T8002");//ԭ������Ϊ2100
        sendMsg.set("AcctNo", acctNo);

        Message returnMsg;
        try {
           returnMsg = sender.send(sendMsg);
            //chenzifei 9��20�ո�,AcctName�ķ���ǰӦ����acctStatus���ж�֮��.
            //msg.set("AcctName", returnMsg.getString("AcctName"));
            msg.set("OpenBankCode", returnMsg.getString("OpenBankCode"));
            msg.set("AcctStatus", returnMsg.getString("AcctStatus"));
            msg.set("IdType", returnMsg.getString("IdType"));
            msg.set("IdNo", returnMsg.getString("IdNo"));
            String acctStatus =returnMsg.getString("AcctStatus");
            if(!"0".equals(acctStatus)){
            	msg.set("ReturnResult", "N");
              if (acctStatus.endsWith("1")) {
                 msg.set("AddWord", "�˻�������");
              } else if (acctStatus.equals("2")) {
                msg.set("AddWord", "�Ѷ���");
              } else if (acctStatus.equals("3")) {
                 msg.set("AddWord", "��ע��");
              } else if (acctStatus.equals("4")) {
                 msg.set("AddWord", "��������");
              }
              return FAIL;           	
            }else{
            	//�������λ��.
            	msg.set("AcctName", returnMsg.getString("AcctName"));
            	return SUCCESS;
            }
        } catch (MessageSendException e) {
            logger.error("�������к���ϵͳʧ��", e);
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "�������к���ϵͳʧ��");
            return FAIL;
        }

        
    }
}


