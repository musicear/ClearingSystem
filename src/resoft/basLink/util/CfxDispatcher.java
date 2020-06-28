package resoft.basLink.util;

import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p>∑÷≈‰∆˜</p>
 * User: liguoyin
 * Date: 2007-4-9
 * Time: 0:01:21
 */
public class CfxDispatcher implements Dispatcher {
    public void process() {
        ThreadLocalContext.getInstance().getContext().setProtocol(new CfxProtocol());
        ThreadLocalContext.getInstance().getContext().setPacker(new CfxPackager());
    }
}
