package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;


/**
 * <p>
 * 协议删除:发送T1200报文
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class DelProveInfo extends AbstractAction {


	private static final Log logger = LogFactory.getLog(DelProveInfo.class);

	public String execute() {
		Message msg = new Message();
        
        msg.setValue("AcctNo", AcctNo);
		msg.setValue("TaxPayCode", TaxPayCode);
		msg.setValue("ProtocolNo", ProtocolNo);
		Message returnData;
		try {
			returnData = TransCommUtil.send("T1200", msg);
			String returnResult = returnData.getValue("ReturnResult");
	        String addWord = returnData.getValue("AddWord");

	        String desc;
	        if (returnResult.equals("Y")) {
	            desc = "协议删除成功";
	        } else {
	            desc = "协议删除失败。失败原因：" + addWord;
	        }
	        setMessage(desc);
		} catch (IOException e) {
			setMessage("连接后台交易系统失败");
			logger.error("连接后台交易系统失败", e);
			return ERROR;
		}


		
		return SUCCESS;
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

	private String TaxPayCode, AcctNo, ProtocolNo;
}
