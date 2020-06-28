package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.tips.chqxh.ACEPackUtil;

public class PayCheckDetailMessage {

	private String sql = "";
	public String paycheckdetailInfo = "";
	public Map packTags = new HashMap();
	
	public PayCheckDetailMessage()
	{
	}
	public String initPayCheckDetailInfo(String CheckDate) throws Exception
	{
		paycheckdetailInfo += "";
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("5","���"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("15","��������"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("17","��������"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","��������к�"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("5","ԭ���ջ��ش���"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("15","ԭί������"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("20","ԭ����ˮ��"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","ԭ������ˮ��"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","���׽��"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("5","�Ƿ���˳ɹ�"," ");
		paycheckdetailInfo += '\n';
		sql = "select * from paycheck where CHKDATE=''";
		
		List queryList=QueryUtil.queryRowSet(sql);	
		if(queryList.size() == 0)
		{
			paycheckdetailInfo += '\n';
			paycheckdetailInfo += "û�в�ѯ����ؼ�¼������";
			paycheckdetailInfo += '\n';
			return paycheckdetailInfo;
		}
		if (queryList.size()>0) 
		{			
			for (int i=0;i<queryList.size();i++) 
			{				
				Map row=(Map)queryList.get(i);
				
				paycheckdetailInfo += id(i);
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("15",(String)row.get("chkDate")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("17",(String)row.get("chkAcctOrd")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("10",(String)row.get("payAcct")," ");				
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("5",(String)row.get("oriTaxOrgCode")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("15",(String)row.get("oriEntrustDate")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("20",(String)row.get("oriPackNo")," ");	
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("10",(String)row.get("oriTraNo")," ");	
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("10",(String)row.get("traAmt")," ");	
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("5",(String)row.get("succCheck")," ");		
				paycheckdetailInfo += '\n';
			}
		}
		return paycheckdetailInfo;
	}
	public String id(int i)
	{
		String format = "     ";
		String buf = "";
		buf = "" + (i + 1);
		buf = buf + format.substring(0, format.length()-buf.length());
		return buf;
	}
}
