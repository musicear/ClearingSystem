package resoft.tips.bankImpl.sxbank;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;

public class TuxedoRealRushApply {
	private static final Log logger = LogFactory.getLog(TuxedoRealRushApply.class);
	public String RealTimePackger(String payeeBankNo,String OriTraNo,String OriWorkDate) throws SQLException
	{
		String PackName = "TIPS001";//要查询bank_relation表 通过payeebankno字段关联 ywbsh
		logger.info("付款银行号为："+PackName);
		String returnstring =ToChangeDate(PackName.getBytes().length)+PackName+ToChangeDate(OriTraNo.getBytes().length)+OriTraNo+ToChangeDate(OriWorkDate.getBytes().length)+OriWorkDate;
		return ToChangeDate(PackName.getBytes().length)+PackName+ToChangeDate(OriTraNo.length())+OriTraNo+ToChangeDate(OriWorkDate.length())+OriWorkDate;
	}
	public char ToChangeDate(int ori)
	{
		char s=(char)ori;		
		return s;
	
	}
	public String getYwbsh(String payeeBankNo) throws SQLException{
		String ywbsh = "";
		ywbsh=DBUtil.queryForString("select ywbsh from bank_relation where PayeeBankNo='"+payeeBankNo+"'");	
		ywbsh=ywbsh.trim();
		return ywbsh;
	}
	
	

}
