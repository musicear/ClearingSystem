package resoft.tips.chqsh;

public class MoneyFormat {

	public MoneyFormat()
	{
		
	}
	public static String getMoneyFormat(String traAmt)
	{
		int count = 0;
		String buf = "";
		for(int x=traAmt.length();x>0;x--)
		{
			buf = traAmt.substring(x-1, x);
			count++;
			if(buf.equals("."))
			{
				count = 0;
			}
			if(count == 3 && x != 1)
			{
				count = 0;
				traAmt = traAmt.substring(0, x-1) + "," + traAmt.substring(x-1);
			}
		}
		return traAmt;
	}
}
