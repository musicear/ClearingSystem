package resoft.tips.chqsh;

public class SQLInit {
	private static final String NAME = "selectmessagefrombrt";
	private static int count = 0;

	/**
	 * 查询报文初始化交易
	 * @param SQL		：查询语句
	 * @param TL_TD		：操作柜员
	 * @return			：查询报文
	 */
	public static String getSQL(String SQL,String TL_TD)
	{
		//规定传来的文件名称必须为20位
		count = SQL.length();
		for(int i=0;i<(481-count);i++)
		{
			SQL += " ";
		}
		return TuxedoPackHeadInit.getSQLHead(TL_TD) + NAME + SQL;
	}
	
	
}
