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
     * ִ��ÿ��module֮ǰ
     */
    public boolean beforeAction(Action action, Message msg) {
        System.out.println(action.getClass().getName());
        return true;
    }

    /**
     * ִ��ÿ��module֮��
     *
     * @param Action action ģ����
     * @param msg    Message
     * @param result String  ִ�н��
     */
    public void afterAction(Action action, Message msg, int result) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
