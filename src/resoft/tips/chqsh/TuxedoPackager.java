package resoft.tips.chqsh;

import java.sql.SQLException;

import resoft.basLink.util.DBUtil;

/**
 * ������֯��ʽ������ͷ+Data1+Data2
 * ������ɣ�TuxedoPackHeadInit + XXXInit + TuxedoData2Init
 * ʵ�֣�ͨ����XXXInit��ʵ�ֱ���ͷ��Data1��Data2�����ֵ�����
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
	 * ��ѯ����
	 * @param SQL		����ѯ���
	 * @param TL_TD		��������Ա
	 * @return			���������
	 */
	public String getSqlPackager(String SQL,String TL_TD)
	{
		return SQLInit.getSQL(SQL,TL_TD);
	}	
	
	/**
	 * ���˱��ģ����˻������ڴ��֧Ʊ�˻�
	 * @param PayAcct	����˰���ʺ�
	 * @param TraAmt	�����׽��
	 * @param TL_TD		��������Ա
	 * @return			�����˱���
	 * @throws SQLException 
	 */
	//yangyuanxu update
	public String getInPackager(String PayAcct,String TraAmt,String PayeeBankNo,String TaxTypeName,String TL_TD) throws SQLException
	{
		//packager = TuxedoPackHeadInit.InitHead1 + PayAcct + TuxedoPackHeadInit.InitHead2;
		String bankPayAcct = getPayAcct(PayeeBankNo);
		if(16 == PayAcct.length())
		{
			//���˻�IN_DATA yangyuanxu update
			packager = IN_DATAInit.getIN_DATA(PayAcct, TraAmt, TaxTypeName,TL_TD, bankPayAcct,PayeeBankNo);
		}
		else
		{
			if(PayAcct.substring(6, 8).equals("10") && PayAcct.length() == 15)
			{
				//���ڴ����˻�I yangyuanxu update
				packager = IInit.getI(PayAcct, TraAmt, TaxTypeName, TL_TD, bankPayAcct,PayeeBankNo);
			}
			if(PayAcct.substring(6, 8).equals("04") && PayAcct.length() == 15)
			{
				//֧Ʊ�˻�INPT  yangyuanxu update
				packager = INPTInit.getINPT(PayAcct, TraAmt, TaxTypeName, TL_TD, bankPayAcct,PayeeBankNo);
			}
		}
		//System.out.println(packager);
		return packager;
	}
	/**
	 * Ĩ�˱���
	 * @param JRN_NO	����־��
	 * @param VCH_NO	����Ʊ��
	 * @param TR_CODE	��������
	 * @param TL_TD		��������Ա
	 * @return			�����ʱ���
	 */
	public String getOutPackager(String JRN_NO,String VCH_NO,String TR_CODE,String TL_TD)
	{
		return TuxedoPackHeadInit.getUNDOHead(TL_TD) + JRN_NO + VCH_NO + TR_CODE;
	}
	
	/**
	 * ��ȡ�����ʺ�
	 * @param JRN_NO	����־��
	 * @return			�������ʺ�
	 */
	//yangyuanxu add
	public String getPayAcct(String payeeBaNo) throws SQLException{
		String payAcct = "";
		payAcct=DBUtil.queryForString("select bankpayacct from bank_relation where payeeBankNo='"+payeeBaNo+"'");	
		payAcct=payAcct.trim();
		return payAcct;
	}
}

