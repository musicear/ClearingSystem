package resoft.basLink.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionInterceptor;
import resoft.xlink.core.Message;

/**
 * function: 记录每个交易执行完毕后的响应码及响应描述
 * User: albert lee
 * Date: 2005-10-25
 * Time: 14:20:28
 */
public class LogReturnCodeModuleInterceptor implements ActionInterceptor {
    private static Log logger = LogFactory.getLog(LogReturnCodeModuleInterceptor.class);
    public boolean beforeAction(Action action, Message msg) {
        return true;
    }

    public void afterAction(Action action, Message msg, int result) {
        logger.info(action.getClass().getName() + "   响应码=\"" + msg.get("响应码") + "\";响应描述=\"" + msg.get("响应描述") + "\"");
    }
}
