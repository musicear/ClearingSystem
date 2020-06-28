package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * ǿ�Ƶ�¼TIPS:����9007����
 * </p>
 * Author: chenjianping Date: 2007-7-30 Time: 15:19:58
 */
public class ForceExitTips extends AbstractAction {

    private static final Log logger = LogFactory.getLog(ForceExitTips.class);

    public String execute() {
        Message msg = new Message();
        Message returnData;
        try {
            returnData = TransCommUtil.send("9008", msg);
        } catch (IOException e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            logger.error("���Ӻ�̨����ϵͳʧ��", e);
            return ERROR;
        }
        String logoutResult = returnData.getValue("LogoutResult");
        String addWord = returnData.getValue("AddWord");
        if(logoutResult.equals(""))
        	logoutResult = "��";
        if(addWord.equals(""));
            addWord = "��";
        setMessage("�˳��������:" + logoutResult + " ����:" + addWord);
        return SUCCESS;
    }

}
