package resoft.tips.chqsh;

import java.text.ParseException;

//import resoft.tips.util.CurrencyUtil;

//import resoft.basLink.util.DBUtil;

public class testCard {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		//String SQL = "select MCAACTSTS,SDMACTSTS,SDMACNAM from MCA m,SDM s where m.MCAACTNO=s.SDMACTNO and m.MCACRDNO='6221340003980299' and m.MCAEPTCOD='90'";
		
		String SQL = "select p.bkdbnkcod,p.bkdsuprlist,s.SDMACNAM from MCA m,PS_BKD p,SDM s where m.MCAEPTCOD='99' and p.bkdbnknpno=s.SDMGACBK and m.MCAACTNO=s.SDMACTNO and m.MCACRDNO='6221340003980299'";
		//String SQL = "select * from iad where iadvchno='88020001' and iadac_dat='20071102'";
		SendMsgToBankSystem send = new SendMsgToBankSystem();
		String TL_TD = "880";
		String rcvMsg = "";
		int i = 0;
		int j = 0;
		rcvMsg = send.sendMsg(SQL, TL_TD + (System.currentTimeMillis()%4 + 1));
		System.out.println(rcvMsg.length());
		System.out.println(rcvMsg);
		i = rcvMsg.indexOf(10);
		j = rcvMsg.lastIndexOf(10);
		rcvMsg = rcvMsg.substring(i+1,j-1);
		System.out.println(rcvMsg.substring(0, 12));
		System.out.println(rcvMsg.substring(12, 24));
		System.out.println(rcvMsg.substring(24));
	/**
		i = rcvMsg.indexOf(10);
		j = rcvMsg.lastIndexOf(10);
		System.out.println(i + "   " + j);
		String buf = rcvMsg.substring(i+1, j);
		System.out.println(buf);
		System.out.println(buf.substring(0, 1));
		System.out.println(buf.substring(1, 10));
		System.out.println(buf.substring(11, buf.length()));
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat simp = new SimpleDateFormat();
		Calendar date = Calendar.getInstance();
		System.out.println(date.get(Calendar.YEAR));
		System.out.println(Calendar.YEAR);
		TuxedoPackager onlyone = TuxedoPackager.getTuxedoPackager();
		String sendMsg = onlyone.getInPackager("010101041111111", "0.98", "½ÌÓýË°", "8801");
		System.out.println(sendMsg);
		DBUtil.
		String str = "11.1";
		str = CurrencyUtil.getCurrencyFormat(str);
		 */
		B b = new B();
		System.out.println(b.i);
	}
}
class A{
	int i;
	public A()
	{
		this.i=5;
	}
}
class B extends A{
	//int i;
	public B()
	{
		super();
	}
}

