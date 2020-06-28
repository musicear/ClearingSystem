package resoft.tips.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;


/**
 * <p/>
 * 协议录入,发送T8002 报文
 * 发送T8002报文校验输入的账号是否正确
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class PreProveInfo extends AbstractAction {
    private static final Log logger = LogFactory.getLog(PreProveInfo.class);

    public String execute() {
        //发送T8002报文以校验账号
        Message msg = new Message();
        msg.setValue("AcctNo", acctNo);
        Message returnData;
        try {
            returnData = TransCommUtil.send("T8002", msg);
        } catch (IOException e) {
            setMessage("连接后台交易系统失败");
            logger.error("连接后台交易系统失败", e);
            return ERROR;
        }
        acctName = returnData.getValue("AcctName");
        String acctStatus = returnData.getValue("AcctStatus");
        if(!acctStatus.equals("0")) {
            Map descMap = new HashMap();
            descMap.put("1","账号不存在");
            descMap.put("2","已冻结");
            descMap.put("3","已注销");
            descMap.put("4","其他错误");
            setMessage("输入的账号有误。错误为：" + descMap.get(acctStatus));
            return ERROR;
        }

        return SUCCESS;
    }



    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getTaxOrgCode() {
        return taxOrgCode;
    }

    public void setTaxOrgCode(String taxOrgCode) {
        this.taxOrgCode = taxOrgCode;
    }

    public String getTaxPayName() {
        return taxPayName;
    }

    public void setTaxPayName(String taxPayName) {
        this.taxPayName = taxPayName;
    }

    public String getTaxPayCode() {
        return taxPayCode;
    }

    public void setTaxPayCode(String taxPayCode) {
        this.taxPayCode = taxPayCode;
    }

    public String getAcctName() {
        return acctName;
    }

    
    private String taxPayCode;
    private String taxPayName;
    private String taxOrgCode;
    private String acctNo;
    private String protocolNo;
    private String acctName;

}
