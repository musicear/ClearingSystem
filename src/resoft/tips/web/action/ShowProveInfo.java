package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p>
 * 协议删除:发送T1300报文
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class ShowProveInfo extends AbstractAction {

	private static final Log logger = LogFactory.getLog(ShowProveInfo.class);

	public String execute() {
		Message msg = new Message();
		msg.setValue("AcctNo", AcctNo);
		msg.setValue("TaxPayCode", TaxPayCode);
		msg.setValue("ProtocolNo", ProtocolNo);
		Message returnData;
		String result="";
		try {
			returnData = TransCommUtil.send("T1300", msg);
			result=returnData.getValue("ReturnResult");
			if("Y".equals(result)){
				TaxOrgName = returnData.getValue("TaxPayName");
				PayAcctName = returnData.getValue("HandOrgName");
				return SUCCESS;
			}else{
				setMessage("删除不成功,原因:"+returnData.getValue("AddWord"));
				return ERROR;
			}
		} catch (IOException e) {
			setMessage("连接后台交易系统失败");
			logger.error("连接后台交易系统失败", e);
			return ERROR;
		}

		//return SUCCESS;
	}

	public void setAcctNo(String acctNo) {
		AcctNo = acctNo;
	}

	public void setProtocolNo(String protocolNo) {
		ProtocolNo = protocolNo;
	}

	public void setTaxPayCode(String taxPayCode) {
		TaxPayCode = taxPayCode;
	}

	public String getAcctNo() {
		return AcctNo;
	}

	public String getProtocolNo() {
		return ProtocolNo;
	}

	public String getTaxPayCode() {
		return TaxPayCode;
	}

	public String getPayAcctName() {
		return PayAcctName;
	}

	public void setPayAcctName(String payAcctName) {
		PayAcctName = payAcctName;
	}

	public String getTaxOrgName() {
		return TaxOrgName;
	}

	public void setTaxOrgName(String taxOrgName) {
		TaxOrgName = taxOrgName;
	}

	private String TaxPayCode, AcctNo, ProtocolNo, PayAcctName, TaxOrgName;

}
