package resoft.junit.testTransactionFlow;

import resoft.basLink.Message;
import resoft.basLink.ModuleBase;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-8
 * Time: 15:37:20
 */
public class Module2 implements ModuleBase{
    public int execute(Message msg) throws Exception {
        msg.setValue("œÏ”¶√Ë ˆ","≤‚ ‘Flow");
        return 0;
    }
}
