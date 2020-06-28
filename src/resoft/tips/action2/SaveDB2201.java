package resoft.tips.action2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Message;

public class SaveDB2201 {
	private static final Log logger = LogFactory.getLog(SaveDB2201.class);
	private String AgentBnkCode = null;
	private String FinOrgCode = null;
	private String TreCode = null;
	private String EntrustDate = null;
	private String PackNo = null;
	private String AllNum = null;
	private String AllAmt = null;
	private String PayoutVouType = null;
	private String PayMode = null;
	
	
	private String TraNo = null;
	private String VouNo = null;
	private String VouDate = null;
	private String PayerAcct = null;
	private String PayerName = null;
	private String PayerAddr = null;
	private String PayeeAcct = null;
	private String PayeeName = null;
	private String PayeeAddr = null;
	private String PayeeOpnBnkNo = null;
	private String AddWord = null;
	private String BudgetType = null;
	private String TrimSign = null;
	private String OfYear = null;
	private String Amt = null;
	private String StatInfNum = null;
	
	private String SeqNo = null;
	private String BdgOrgCode = null;
	private String FuncSbtCode = null;
	private String EcnomicSubjectCode = null;
	private String Amt_detail = null;
	private String AcctProp = null;
	
	
	public void testMessage(Message msg){
		
		
		String AgentBnkCode = msg.getString("AgentBnkCode");
		logger.info("AgentBnkCode is: "+AgentBnkCode);
		String FinOrgCode = msg.getString("FinOrgCode");
		logger.info("FinOrgCode is: "+FinOrgCode);
	}
}
