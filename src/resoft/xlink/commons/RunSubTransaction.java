package resoft.xlink.commons;

import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�����ӽ���</p>
 * User: liguoyin
 * Date: 2008-6-4
 * Time: 21:48:30
 */
public class RunSubTransaction implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("������",transCode);
        Controller controller = new Controller();
        controller.setNameOfTransCode("������");
        controller.execute(msg);

        return SUCCESS;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    private String transCode;
}
