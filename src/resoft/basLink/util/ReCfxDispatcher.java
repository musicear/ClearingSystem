package resoft.basLink.util;

import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-7-5
 * Time: 11:37:08
 */
public class ReCfxDispatcher implements Dispatcher {
    public void process() {
        ThreadLocalContext.getInstance().getContext().setPacker(new CfxPackager());
        ThreadLocalContext.getInstance().getContext().setProtocol(new ReCfxProtocol(new RawLengthAccessor()));
    }
}
