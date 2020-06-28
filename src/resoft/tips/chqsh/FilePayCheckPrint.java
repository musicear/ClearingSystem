package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.CurrencyUtil;

public class FilePayCheckPrint {
	private String payCheck = "";
	private String sql = "";
	private String beforesql = "";
	private static final Log logger = LogFactory.getLog(QueryAcct.class);
	private Calendar date = Calendar.getInstance();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FilePayCheckPrint f = new FilePayCheckPrint();
//		System.out.println(f.initPayCheck("2001116", "20071115", "9998"));
		String tmpPath = "C:\\Documents and Settings\\liwei\\����\\print";
		String ReturnFileName = "Tips12345";
		File file = new File(tmpPath, ReturnFileName);
		Writer writer = null;
		try
		{
			writer = new FileWriter(file);
			writer.write(f.initPayCheck("2001116", "20071115", "0001"));			
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��Ϣ����ֻ��Ҫֱ�����˽����������Ϣ����
	 * @param WorkDate			����ǰTips��������
	 * @param BeforeWorkDate	����һTips��������
	 * @param BranchNo			��������
	 * @return					��������Ϣ
	 */
	public String initPayCheck(String WorkDate,String PreWorkDate,String BranchNo)
	{
		String forMat = "��������������������";
		String str = "";
		str = "\n\n\n\n";
		str += "                                       ��ӡ���˽��" + "\n\n\n";
		str += forMat.substring(0, 5) + "      Tips�������ڣ�" + WorkDate;
		str += forMat.substring(0, 5) + "\n\n";
		
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("16","�տ����к�"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6","����"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6","����"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8","�ܱ���"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8","�ܽ��"," ");
		payCheck += "\n";
		sql = "select * from paycheck where CHKDATE='"+ WorkDate +"'";
				
		//��ѯ������Ϣ
		List queryList = null;
		logger.info("sql = " + sql);
		try
		{
			queryList = QueryUtil.queryRowSet(sql);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			payCheck+= '\n';
			payCheck+= "�������ݿ��������";
			payCheck+= '\n';
			return payCheck;
		}
		if(queryList.size() == 0)
		{
			logger.info("sql = " + beforesql);
			try
			{
				beforesql = "select * from paycheck where CHKDATE='"+ PreWorkDate +"'";
				queryList = QueryUtil.queryRowSet(beforesql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				payCheck+= '\n';
				payCheck+= "�������ݿ��������";
				payCheck+= '\n';
				return payCheck;
			}
		}
	
		if(queryList.size() == 0)
		{
			payCheck+= '\n';
			payCheck+= "û�в�ѯ����ؼ�¼������";
			payCheck+= '\n';
			return payCheck;
		}
		if (queryList.size()>0) 
		{			
			for (int i=0;i<queryList.size();i++) 
			{				
				Map row=(Map)queryList.get(i);			
				payCheck+="";
				payCheck+= forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("16",(String)row.get("payeeBankNo")," ");
				payCheck+= forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6",(String)row.get("chkAcctOrd")," ");
				String chkAcctType = (String)row.get("chkAcctType");
				if("0".equals(chkAcctType))
				{
					chkAcctType = "�ռ�";
				}
				else
				{
					chkAcctType = "����";
				}
				payCheck+= forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6",chkAcctType," ");
				payCheck+= forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8",(String)row.get("allNum")," ");	
				String allAmt = (String) row.get("allAmt");
		        if(allAmt==null||"".equals(allAmt))
		        	allAmt="0.00";  
		        allAmt = CurrencyUtil.getCurrencyFormat(allAmt);
		        payCheck+= forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8",allAmt," ");		
				payCheck+= "\n";
			}
			payCheck+= "\n";
		}
		payCheck += forMat.substring(0, 5) + "      ��ӡ���ڣ�" + date.get(Calendar.YEAR) + "��" + date.get(Calendar.MONTH) + "��" + date.get(Calendar.DAY_OF_MONTH) + "��";
		payCheck += "                                    ��Ա�ţ� 0204";
		return str + payCheck;
	}
	
}
