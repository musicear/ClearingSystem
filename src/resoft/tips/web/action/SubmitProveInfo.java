package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * 协议录入:发送T1100报文
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class SubmitProveInfo extends AbstractAction {

    private static final Log logger = LogFactory.getLog(SubmitProveInfo.class);

    public String execute() {
        Message msg = new Message();
        msg.setValue("TaxPayCode", taxPayCode);
        msg.setValue("TaxPayName",taxPayName);
        msg.setValue("TaxOrgCode", taxOrgCode);
        msg.setValue("AcctNo", acctNo);
        msg.setValue("HandOrgName",acctName);

        msg.setValue("ContectCode", protocolNo);
        Message returnData;
        try {
            returnData = TransCommUtil.send("T1100", msg);
        } catch (IOException e) {
            setMessage("连接后台交易系统失败");
            logger.error("连接后台交易系统失败", e);
            return ERROR;
        }
       
        String addWord = returnData.getValue("AddWord");
        String returnResult = returnData.getValue("ReturnResult");
        if(returnResult.equals("Y")) {
            setMessage("签约成功");
        } else {
            setMessage("签约失败。" + addWord);
        }

        return SUCCESS;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public void setTaxOrgCode(String taxOrgCode) {
        this.taxOrgCode = taxOrgCode;
    }

    public void setTaxPayName(String taxPayName) {
        this.taxPayName = taxPayName;
    }

    public void setTaxPayCode(String taxPayCode) {
        this.taxPayCode = taxPayCode;
    }


    private String taxPayCode;
    private String taxPayName;
    private String taxOrgCode;
    private String acctNo;
    private String acctName;
    private String protocolNo;


}
