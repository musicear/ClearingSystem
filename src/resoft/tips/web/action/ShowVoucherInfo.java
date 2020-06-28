package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;


/**
 * <p/>
 * 税票回单打印,发送T3100 报文
 * </p>
 * Author: wangchenglong Date: 2007-7-30 Time: 16:29:58
 */

public class ShowVoucherInfo extends AbstractAction {
    private static final Log logger = LogFactory.getLog(ShowVoucherInfo.class);

    public String execute() throws Exception {

        Message msg = new Message();
        msg.setValue("交易码", "T3100");
        msg.setValue("PayAcct", payAcct);
        msg.setValue("TaxVouNo", TaxVouNo);
        msg.setValue("TaxPayCode", TaxPayCode);
        Message returnData;
        try {
            returnData = TransCommUtil.send("T3100", msg);
            if(returnData.getValue("ReturnResult").equals("N")) {
                setMessage("要查询的税票不存在，请重新输入");
                return ERROR;
            }
            payAcct = returnData.getValue("PayAcct");
            TaxVouNo = returnData.getValue("TaxVouNo");
            TaxPayCode = returnData.getValue("TaxPayCode");
            TraNo = returnData.getValue("TraNo");
            TaxPayName = returnData.getValue("TaxPayName");
            HandOrgName = returnData.getValue("HandOrgName");
            TaxOrgName = returnData.getValue("TaxOrgName");
            PayOpBkName = returnData.getValue("PayOpBkName");
            PayeeName = returnData.getValue("PayeeName");
            PrintTimes = Integer.parseInt(returnData.getValue("PrintTimes"));
            TraAmt = Double.parseDouble(returnData.getValue("TraAmt"));
            TraAmtChinese = returnData.getValue("TraAmtChinese");


            TaxTypeNum = Integer.parseInt(returnData.getValue("TaxTypeNum"));
            for (int i = 0; i < TaxTypeNum; i++) {
                TaxTypeName = returnData.getValue("TaxTypeName" + (i + 1));
                TaxStartDate = returnData.getValue("TaxStartDate" + (i + 1));
                TaxEndDate = returnData.getValue("TaxEndDate" + (i + 1));
                TaxTypeAmt = Double.parseDouble(returnData.getValue("TaxTypeAmt" + (i + 1)));
            }

        } catch (IOException e) {
            setMessage("连接后台交易系统失败");
            logger.error("连接后台交易系统失败", e);
            return ERROR;
        }

        return SUCCESS;

    }

    public String getHandOrgName() {
        return HandOrgName;
    }

    public void setHandOrgName(String handOrgName) {
        HandOrgName = handOrgName;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getPayAcct() {
        return payAcct;
    }

    public void setPayAcct(String payAcct) {
        this.payAcct = payAcct;
    }

    public String getPayeeName() {
        return PayeeName;
    }

    public void setPayeeName(String payeeName) {
        PayeeName = payeeName;
    }

    public String getPayOpBkName() {
        return PayOpBkName;
    }

    public void setPayOpBkName(String payOpBkName) {
        PayOpBkName = payOpBkName;
    }

    public String getTaxEndDate() {
        return TaxEndDate;
    }

    public void setTaxEndDate(String taxEndDate) {
        TaxEndDate = taxEndDate;
    }

    public String getTaxOrgName() {
        return TaxOrgName;
    }

    public void setTaxOrgName(String taxOrgName) {
        TaxOrgName = taxOrgName;
    }

    public String getTaxPayCode() {
        return TaxPayCode;
    }

    public void setTaxPayCode(String taxPayCode) {
        TaxPayCode = taxPayCode;
    }

    public String getTaxPayName() {
        return TaxPayName;
    }

    public void setTaxPayName(String taxPayName) {
        TaxPayName = taxPayName;
    }

    public String getTaxStartDate() {
        return TaxStartDate;
    }

    public void setTaxStartDate(String taxStartDate) {
        TaxStartDate = taxStartDate;
    }

    public double getTaxTypeAmt() {
        return TaxTypeAmt;
    }

    public void setTaxTypeAmt(double taxTypeAmt) {
        TaxTypeAmt = taxTypeAmt;
    }

    public String getTaxTypeName() {
        return TaxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        TaxTypeName = taxTypeName;
    }

    public String getTaxVouNo() {
        return TaxVouNo;
    }

    public void setTaxVouNo(String taxVouNo) {
        TaxVouNo = taxVouNo;
    }

    public double getTraAmt() {
        return TraAmt;
    }

    public void setTraAmt(double traAmt) {
        TraAmt = traAmt;
    }

    public String getTraAmtChinese() {
        return TraAmtChinese;
    }

    public void setTraAmtChinese(String traAmtChinese) {
        TraAmtChinese = traAmtChinese;
    }

    public String getTraNo() {
        return TraNo;
    }

    public void setTraNo(String traNo) {
        TraNo = traNo;
    }

    public int getTaxTypeNum() {
        return TaxTypeNum;
    }


    public void setTaxTypeNum(int taxTypeNum) {
        TaxTypeNum = taxTypeNum;
    }

    public int getPrintTimes() {
        return PrintTimes;
    }


    public void setPrintTimes(int printTimes) {
        PrintTimes = printTimes;
    }

    private String MsgId;

    private String payAcct;

    private String TaxVouNo;

    private String TaxPayCode;
    private String TraNo;

    private String TaxPayName;

    private String HandOrgName;

    private String TaxOrgName;

    private String PayOpBkName;

    private String PayeeName;

    private String TaxTypeName;

    private String TraAmtChinese;
    private double TraAmt;
    private double TaxTypeAmt;

    private String TaxStartDate;
    private String TaxEndDate;

    private int TaxTypeNum;
    private int PrintTimes;


}
