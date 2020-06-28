package resoft.tips.web.action;

import java.io.IOException;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * 强制登录TIPS:发送9007报文
 * </p>
 * Author: chenjianping Date: 2007-7-30 Time: 15:19:58
 */
public class ForceLogTips extends AbstractAction {


    public String execute() throws Exception {
        Message returnMsg;
        try {
            returnMsg = TransCommUtil.send("9006", new Message());
        } catch (IOException e) {
            setMessage("连接后台交易系统失败");
            return ERROR;
        }
        String loginResult = returnMsg.getValue("LoginResult");
        String addWord = returnMsg.getValue("AddWord");
        String sysStat = returnMsg.getValue("SysStat");
        if (loginResult.equals(""))
        	loginResult = "无";
        if(addWord.equals(""))
        	addWord = "无";
        if (sysStat.equals("0")) {
            sysStat = "日间";
        } else if (sysStat.equals("1")) {
            sysStat = "日切窗口";
        } else if (sysStat.equals("2")) {
            sysStat = "系统维护状态";
        } else 
        	sysStat = "无";
        setMessage("登录结果:" + loginResult + "  附言:" + addWord + "  当前系统状态:" + sysStat);
        return SUCCESS;
    }


}
