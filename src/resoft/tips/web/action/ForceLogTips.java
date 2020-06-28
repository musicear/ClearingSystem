package resoft.tips.web.action;

import java.io.IOException;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * ǿ�Ƶ�¼TIPS:����9007����
 * </p>
 * Author: chenjianping Date: 2007-7-30 Time: 15:19:58
 */
public class ForceLogTips extends AbstractAction {


    public String execute() throws Exception {
        Message returnMsg;
        try {
            returnMsg = TransCommUtil.send("9006", new Message());
        } catch (IOException e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            return ERROR;
        }
        String loginResult = returnMsg.getValue("LoginResult");
        String addWord = returnMsg.getValue("AddWord");
        String sysStat = returnMsg.getValue("SysStat");
        if (loginResult.equals(""))
        	loginResult = "��";
        if(addWord.equals(""))
        	addWord = "��";
        if (sysStat.equals("0")) {
            sysStat = "�ռ�";
        } else if (sysStat.equals("1")) {
            sysStat = "���д���";
        } else if (sysStat.equals("2")) {
            sysStat = "ϵͳά��״̬";
        } else 
        	sysStat = "��";
        setMessage("��¼���:" + loginResult + "  ����:" + addWord + "  ��ǰϵͳ״̬:" + sysStat);
        return SUCCESS;
    }


}
