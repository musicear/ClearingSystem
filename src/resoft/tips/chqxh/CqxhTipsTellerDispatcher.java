package resoft.tips.chqxh;

import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p>�����źϹ���������</p>
 * Author: liwei
 * Date: 2007-07-29
 * Time: 14:50:06
 */
public class CqxhTipsTellerDispatcher implements Dispatcher {
    public void process() {
        ThreadLocalContext.getInstance().getContext().setProtocol(new CqxhTipsTellerProtocol());
        //ThreadLocalContext.getInstance().getContext().setPacker(new CfxPackager());
    }
}