package resoft.tips.action;

import resoft.basLink.Configuration;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置发起机构代码.用于发起业务，如签约、银行申报等</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 15:20:30
 */
public class SetSendOrgCode implements Action {
    private static Configuration conf = Configuration.getInstance();
    public int execute(Message msg) throws Exception {
        String orgCode = conf.getProperty("general","BankSrcOrgCode");
        msg.set(sendOrgCodeNodePath,orgCode);
        return SUCCESS;
    }

    public void setSendOrgCodeNodePath(String sendOrgCodeNodePath) {
        this.sendOrgCodeNodePath = sendOrgCodeNodePath;
    }

    private String sendOrgCodeNodePath;
}
