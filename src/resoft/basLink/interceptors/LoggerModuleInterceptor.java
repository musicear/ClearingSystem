package resoft.basLink.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionInterceptor;
import resoft.xlink.core.Message;

/**
 * function: 记录执行日志日志
 * User: albert lee
 * Date: 2005-10-23
 * Time: 21:43:39
 */
public class LoggerModuleInterceptor implements ActionInterceptor {
    private static Log logger = LogFactory.getLog(LoggerModuleInterceptor.class);

    public boolean beforeAction(Action action, Message msg) {
        //logger.info(action.getClass().getName());
        return true;
    }

    public void afterAction(Action action, Message msg, int result) {
        logger.info(action.getClass().getName() + " returnValue=" + result);
    }

}
