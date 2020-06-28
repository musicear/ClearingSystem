package resoft.tips.web.action.util;
 
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

public class Maintest {

	private static String trano="2";

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		/*// TODO Auto-generated method stub
		CommService  server=new CommService();
		server.setProperty("dispatcher", "resoft.tips.bankImpl.sxbank.SXBankDispatcher");
		server.setProperty("nameOfTransCode", "TransCode");
		server.setProperty("globalPropertiesReader", "resoft.basLink.util.GlobalPropertiesReaderImpl");
		try
		{
			server.start();
		}
		catch(Exception e)
		{}
		
		String time="2011-01-22 33:44:32";
		time=time.substring(11, time.length());
		System.outrintln(time);*/
		String[] failDeatilList={"Iadac_dat"};
		List a=QueryUtil.queryRowSet("select trano from realtimepayment where trano='"+trano+"'");
		Map map=(Map)a.get(0);
		String tra=(String)map.get("trano");
		if(tra==null)
		{
			System.out.println("null");
		}
	}

}
