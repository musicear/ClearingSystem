package resoft.junit.testTransactionFlow;

import resoft.basLink.Message;
import resoft.basLink.ModuleBase;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-9
 * Time: 23:17:17
 */
public class Module3 implements ModuleBase{
    public int execute(Message msg) throws Exception {
        msg.setValue("flag","true");
        return 0;
    }
}
