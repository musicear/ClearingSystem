package resoft.tips.chqsh;

import resoft.basLink.Configuration;

public class TIPSDesOrgCode {

	private static Configuration conf = Configuration.getInstance();
	public static String getTIPSDesOrgCode = conf.getProperty("general", "TIPSDesOrgCode");
	public String getgetTIPSDesOrgCode()
	{
		return getTIPSDesOrgCode;
	}
	public static void main(String args[])
	{
		System.out.println(getTIPSDesOrgCode);
	}
}
