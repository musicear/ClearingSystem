package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;
/**
 * User: zhuchangwu
 * Date: 2007-8-18
 * Time: 22:11:26
 */
public class DealRushApply extends AbstractSyncAction {
	private static final Log logger = LogFactory.getLog(DealRushApply.class);
	 public int execute(Message msg) throws RuntimeException {
	        String result = "";
	        String addWord;		 
        	try {
                Message newMsg = new DefaultMessage();
                newMsg.set("������","1021");
                newMsg.set("TaxOrgCode",msg.getString("//CFX/MSG/RushApply1021/TaxOrgCode"));
                newMsg.set("EntrustDate",msg.getString("//CFX/MSG/RushApply1021/EntrustDate"));
                newMsg.set("CancelNo",msg.getString("//CFX/MSG/RushApply1021/CancelNo"));
                newMsg.set("OriEntrustDate",msg.getString("//CFX/MSG/RushApply1021/OriEntrustDate"));
                newMsg.set("OriTraNo",msg.getString("//CFX/MSG/RushApply1021//OriTraNo"));
                newMsg.set("CancelReason",msg.getString("//CFX/MSG/RushApply1021/CancelReason"));        
            	MessageSender sender = MessageSenderUtil.getMessageSender();
            	Message returnMsg = sender.send(msg);
                addWord = returnMsg.getString("RespDesc");
                String respCode = returnMsg.getString("RespCode");
                if("0000".equals(respCode)){
                    result = "000";
                    addWord = "����ɹ�";           	
                }
                 if("0102".equals(respCode)){
                     result = "102";
                     addWord = "ԭ���ײ�����";              	 
                 }
                 if("0201".equals(respCode)){
                     result = "201";
                     addWord = "ԭ�����ѱ�����";              	 
                 }
                 if("0109".equals(respCode)){
                     result = "109";
                     addWord = "���д���ʧ��";              	 
                 }
                } catch (MessageSendException e) {
                    logger.error("�������к���ϵͳʧ��",e);
                    result = "109";
                    addWord = "���д���ʧ��";
                }  
                msg.set(getResultNodePath(),result);
                msg.set(getAddWordNodePath(),addWord);
                return SUCCESS;
	 }

}
