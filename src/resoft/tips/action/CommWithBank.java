package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�����к���ϵͳͨѶ</p>
 * Author: liguoyin
 * Date: 2007-8-18
 * Time: 17:29:59
 */
public class CommWithBank implements Action {
	private static final Log logger = LogFactory.getLog(CommWithBank.class);
    public int execute(Message msg) throws Exception {
    	logger.info("������Ϊ"+className);
        Action action = (Action) Class.forName(className).newInstance();
        return action.execute(msg);
        //return SUCCESS;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;
}
