package resoft.tips.bankImpl.sxbank;

import resoft.tips.bankImpl.RawLengthAccessor;
import resoft.tips.util.TipsTellerProtocol;
import resoft.xlink.comm.Dispatcher;
import resoft.xlink.comm.helper.ThreadLocalContext;




/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-7-5
 * Time: 11:37:08
 */
public class SXBankDispatcher implements Dispatcher {
    public void process() {
        //ThreadLocalContext.getInstance().getContext().setProperty("LengthAccessorInstance",);
        ThreadLocalContext.getInstance().getContext().setPacker(new SXBankPackager());
        ThreadLocalContext.getInstance().getContext().setProtocol(new TipsTellerProtocol(new RawLengthAccessor()));
    }
}