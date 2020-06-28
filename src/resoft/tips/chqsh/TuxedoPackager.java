package resoft.tips.chqsh;

import java.sql.SQLException;

import resoft.basLink.util.DBUtil;

/**
 * 报文组织方式：报文头+Data1+Data2
 * 报文组成：TuxedoPackHeadInit + XXXInit + TuxedoData2Init
 * 实现：通过类XXXInit，实现报文头、Data1、Data2三部分的整合
 */

public class TuxedoPackager {

	private static String packager;
	
	private static final TuxedoPackager onlyone = new TuxedoPackager();

	public static TuxedoPackager getTuxedoPackager()
	{
		return onlyone;
	}
	private TuxedoPackager()
	{}
	public String getPswSqlPackager(String SQL)
	{
		int count = 0;
		count = SQL.length();
		for(int i=0;i<(501-count);i++)
		{
			SQL += " ";
		}
		return TuxedoPackHeadInit.checkPassWordHead() + SQL;
	}
	public String getPswSqlPackager(String SUP1_ID,String SUP1_PSWD,String SUP2_ID)
	{
		int count = 0;
		String buf = "";
		for(int i=0;i<(501-count);i++)
		{
			buf += " ";
		}
		return TuxedoPackHeadInit.checkPassWordHead(SUP1_ID, SUP1_PSWD,SUP2_ID) + buf ;
	}
	/**
	 * 查询报文
	 * @param SQL		：查询语句
	 * @param TL_TD		：操作柜员
	 * @return			：操作结果
	 */
	public String getSqlPackager(String SQL,String TL_TD)
	{
		return SQLInit.getSQL(SQL,TL_TD);
	}	
	
	/**
	 * 记账报文：卡账户、活期储蓄、支票账户
	 * @param PayAcct	：纳税人帐号
	 * @param TraAmt	：交易金额
	 * @param TL_TD		：操作柜员
	 * @return			：记账报文
	 * @throws SQLException 
	 */
	//yangyuanxu update
	public String getInPackager(String PayAcct,String TraAmt,String PayeeBankNo,String TaxTypeName,String TL_TD) throws SQLException
	{
		//packager = TuxedoPackHeadInit.InitHead1 + PayAcct + TuxedoPackHeadInit.InitHead2;
		String bankPayAcct = getPayAcct(PayeeBankNo);
		if(16 == PayAcct.length())
		{
			//卡账户IN_DATA yangyuanxu update
			packager = IN_DATAInit.getIN_DATA(PayAcct, TraAmt, TaxTypeName,TL_TD, bankPayAcct,PayeeBankNo);
		}
		else
		{
			if(PayAcct.substring(6, 8).equals("10") && PayAcct.length() == 15)
			{
				//活期储蓄账户I yangyuanxu update
				packager = IInit.getI(PayAcct, TraAmt, TaxTypeName, TL_TD, bankPayAcct,PayeeBankNo);
			}
			if(PayAcct.substring(6, 8).equals("04") && PayAcct.length() == 15)
			{
				//支票账户INPT  yangyuanxu update
				packager = INPTInit.getINPT(PayAcct, TraAmt, TaxTypeName, TL_TD, bankPayAcct,PayeeBankNo);
			}
		}
		//System.out.println(packager);
		return packager;
	}
	/**
	 * 抹账报文
	 * @param JRN_NO	：日志号
	 * @param VCH_NO	：传票号
	 * @param TR_CODE	：交易码
	 * @param TL_TD		：操作柜员
	 * @return			：摸帐报文
	 */
	public String getOutPackager(String JRN_NO,String VCH_NO,String TR_CODE,String TL_TD)
	{
		return TuxedoPackHeadInit.getUNDOHead(TL_TD) + JRN_NO + VCH_NO + TR_CODE;
	}
	
	/**
	 * 获取贷方帐号
	 * @param JRN_NO	：日志号
	 * @return			：贷方帐号
	 */
	//yangyuanxu add
	public String getPayAcct(String payeeBaNo) throws SQLException{
		String payAcct = "";
		payAcct=DBUtil.queryForString("select bankpayacct from bank_relation where payeeBankNo='"+payeeBaNo+"'");	
		payAcct=payAcct.trim();
		return payAcct;
	}
}

