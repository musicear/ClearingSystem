package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���ʵʱת�˷��ؽ��</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 15:55:07
 */
public class CheckRealResult implements Action {
    public int execute(Message msg) throws Exception {
        String result = msg.getString("Result");
        if (result.equals("94999") || result.equals("99091")) {
            return FAIL;
        }
        return SUCCESS;
    }
}
