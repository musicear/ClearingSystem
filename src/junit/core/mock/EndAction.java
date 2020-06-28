package junit.core.mock;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 16:37:01
 */
public class EndAction implements Action {
    public int execute(Message msg) throws RuntimeException {
        msg.set("respCode","000");
        System.out.println("----------Yeah!----------");
        return SUCCESS;
    }
}
