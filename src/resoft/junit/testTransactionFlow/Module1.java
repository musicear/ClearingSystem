package resoft.junit.testTransactionFlow;

import resoft.basLink.Message;
import resoft.basLink.ModuleBase;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-8
 * Time: 15:36:37
 */
public class Module1 implements ModuleBase{
    public int execute(Message msg) throws Exception {

        if(msg.getValue("go").equals("true")) {
            msg.setValue("ÏìÓ¦Âë","123");
            return SUCCESS;
        } else {
            msg.setValue("ÏìÓ¦Âë","456");
            return FAIL;
        }
    }
}
