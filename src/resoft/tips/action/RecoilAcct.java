package resoft.tips.action;

//import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>账务反冲</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 19:04:12
 */
public class RecoilAcct extends AbstractSyncAction {

    public int execute(Message msg) throws Exception {
        msg.set(getResultNodePath(),"1");
        msg.set(getAddWordNodePath(),"冲正成功");
        return SUCCESS;
    }
}
