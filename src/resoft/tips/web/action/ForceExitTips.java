package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * 强制登录TIPS:发送9007报文
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
            setMessage("连接后台交易系统失败");
            logger.error("连接后台交易系统失败", e);
            return ERROR;
        }
        String logoutResult = returnData.getValue("LogoutResult");
        String addWord = returnData.getValue("AddWord");
        if(logoutResult.equals(""))
        	logoutResult = "无";
        if(addWord.equals(""));
            addWord = "无";
        setMessage("退出结果代码:" + logoutResult + " 附言:" + addWord);
        return SUCCESS;
    }

}
