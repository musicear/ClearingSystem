package resoft.tips.chqsh;

public class SQLInit {
	private static final String NAME = "selectmessagefrombrt";
	private static int count = 0;

	/**
	 * ��ѯ���ĳ�ʼ������
	 * @param SQL		����ѯ���
	 * @param TL_TD		��������Ա
	 * @return			����ѯ����
	 */
	public static String getSQL(String SQL,String TL_TD)
	{
		//�涨�������ļ����Ʊ���Ϊ20λ
		count = SQL.length();
		for(int i=0;i<(481-count);i++)
		{
			SQL += " ";
		}
		return TuxedoPackHeadInit.getSQLHead(TL_TD) + NAME + SQL;
	}
	
	
}
