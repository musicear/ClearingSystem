package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���ý�����</p>
 * Author: liguoyin
 * Date: 2007-8-7
 * Time: 20:27:29
 */
public class SetResponseTransCode implements Action {
    public int execute(Message msg) throws Exception {
        String transCode = msg.getString("������");
        //transCode += "1";
        msg.set("������",transCode);
        return SUCCESS;
    }
}
