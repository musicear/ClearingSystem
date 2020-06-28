package junit.core.mock;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionInterceptor;
import resoft.xlink.core.Message;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-25
 * Time: 8:34:24
 */
public class LogActionInterceptor implements ActionInterceptor {
    /**
     * 执行每个module之前
     */
    public boolean beforeAction(Action action, Message msg) {
        System.out.println(action.getClass().getName());
        return true;
    }

    /**
     * 执行每个module之后
     *
     * @param Action action 模块类
     * @param msg    Message
     * @param result String  执行结果
     */
    public void afterAction(Action action, Message msg, int result) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
