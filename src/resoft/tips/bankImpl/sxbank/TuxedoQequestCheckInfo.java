package resoft.tips.bankImpl.sxbank;

import java.sql.SQLException;

import resoft.basLink.util.DBUtil;

public class TuxedoQequestCheckInfo 
{
	public String QequestCheckInfo(String payeeBankNo,String CheckDate) throws SQLException
	{
		String PackName = getYwbsh(payeeBankNo);//Ҫ��ѯbank_relation�� ͨ��payeebankno�ֶι��� ywbsh
		return ToChangeDate(PackName.getBytes().length)+PackName+ToChangeDate(CheckDate.getBytes().length)+CheckDate;
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

