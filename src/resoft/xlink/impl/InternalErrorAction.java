package resoft.xlink.impl;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�ڲ��������Action</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:57:41
 */
public class InternalErrorAction implements Action {
    public int execute(Message msg) {

        return SUCCESS;
    }
}
