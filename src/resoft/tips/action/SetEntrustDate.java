package resoft.tips.action;

import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置委托日期。用于发起报文，若签约、银行端申报等</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 15:14:07
 */
public class SetEntrustDate implements Action {
    public int execute(Message msg) throws Exception {
        msg.set(entrustNodePath, DateTimeUtil.getDateString());
        return SUCCESS;
    }

    public void setEntrustNodePath(String entrustNodePath) {
        this.entrustNodePath = entrustNodePath;
    }

    private String entrustNodePath;
}
