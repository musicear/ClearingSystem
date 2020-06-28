package resoft.tips.bankImpl.sxbank;

import java.sql.SQLException;

public class TuxedoPackager {
	
	
	public String getPackager(String protocolNo,String PayAcct,String TraAmt ,String payeeBankNo,String WorkDate,int flag) throws SQLException{
		
		TuxedoPackagerHead packagerHead = new TuxedoPackagerHead();
		TuxedoRealPackager realPackager = new TuxedoRealPackager();
		return packagerHead.getPackagerHead(flag) + realPackager.RealTimePackger(protocolNo,PayAcct,TraAmt ,payeeBankNo,WorkDate);
	}
	public String getRushApplystr(String payeeBankNo,String OriTraNo,String OriWorkDate,int flag)throws SQLException
	{
		TuxedoPackagerHead packagerHead = new TuxedoPackagerHead();
		TuxedoRealRushApply realPackager = new TuxedoRealRushApply();
		return packagerHead.getPackagerHead(flag) + realPackager.RealTimePackger(payeeBankNo,OriTraNo,OriWorkDate);
		
	}
	public String getQequestCheckInfoStr(String payeeBankNo,String CheckDate,int flag) throws SQLException
	{
		TuxedoPackagerHead packagerHead = new TuxedoPackagerHead();
		TuxedoQequestCheckInfo qequestCheckInfo = new TuxedoQequestCheckInfo();
		return packagerHead.getPackagerHead(flag)+qequestCheckInfo.QequestCheckInfo(payeeBankNo, CheckDate);
	}
}
