package resoft.tips.action;

import resoft.basLink.Configuration;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置支付系统行号</p>
 * @Author: liwei
 * @Date: 2008-06-01
 * @Time: 10:10:10
 */

public class SetBankNo implements Action {
    private static Configuration conf = Configuration.getInstance();
    public int execute(Message msg) throws Exception {
        String bankNo = conf.getProperty("general", "BankSrcOrgCode");
        msg.set(bankNoNodePath,bankNo);
        return SUCCESS;
    }

    public void setBankNoNodePath(String bankNoNodePath) {
        this.bankNoNodePath = bankNoNodePath;
    }

    private String bankNoNodePath;
}
