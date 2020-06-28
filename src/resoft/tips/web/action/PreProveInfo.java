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
 * Э��¼��,����T8002 ����
 * ����T8002����У��������˺��Ƿ���ȷ
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class PreProveInfo extends AbstractAction {
    private static final Log logger = LogFactory.getLog(PreProveInfo.class);

    public String execute() {
        //����T8002������У���˺�
        Message msg = new Message();
        msg.setValue("AcctNo", acctNo);
        Message returnData;
        try {
            returnData = TransCommUtil.send("T8002", msg);
        } catch (IOException e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            logger.error("���Ӻ�̨����ϵͳʧ��", e);
            return ERROR;
        }
        acctName = returnData.getValue("AcctName");
        String acctStatus = returnData.getValue("AcctStatus");
        if(!acctStatus.equals("0")) {
            Map descMap = new HashMap();
            descMap.put("1","�˺Ų�����");
            descMap.put("2","�Ѷ���");
            descMap.put("3","��ע��");
            descMap.put("4","��������");
            setMessage("������˺����󡣴���Ϊ��" + descMap.get(acctStatus));
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
