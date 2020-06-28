package junit.core.mock;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:11:59
 */
public class MockAction implements Action {
    public int execute(Message msg) throws RuntimeException {
        if(name.equals("Albert Li")) {
            msg.set("name","Albert Li");
            msg.set("age","28");
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
    public void setName(String name) {
        this.name = name;
    }
    private String name;
}
