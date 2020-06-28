package resoft.tips.chqsh;

public class SQLPayCheck {

	/**
	 * @param args
	 */
	private SendMsgToBankSystem send = new SendMsgToBankSystem();
	private static String TL_TD = "880";
	
	public String CheckCount()
	{
//		String count = "";
		String SQL = "select count(*) from IAD where IADAC_DAT='20071115'";
		SQL = "select count(*) from IAD where IADCTAIND='N'";
		String rcvMsg = "";
		
		rcvMsg = send.sendMsg(SQL, TL_TD + (System.currentTimeMillis()%4 + 1));
//		count = rcvMsg.substring(0, 6);
		return rcvMsg;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SQLPayCheck sql = new SQLPayCheck();
		System.out.println(sql.CheckCount());
	}

}
