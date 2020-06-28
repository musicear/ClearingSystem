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
		String tmpPath = "C:\\Documents and Settings\\liwei\\桌面\\print";
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
	 * 信息中心只需要直到对账结果的总体信息即可
	 * @param WorkDate			：当前Tips工作日期
	 * @param BeforeWorkDate	：上一Tips工作日期
	 * @param BranchNo			：机构号
	 * @return					：对账信息
	 */
	public String initPayCheck(String WorkDate,String PreWorkDate,String BranchNo)
	{
		String forMat = "　　　　　　　　　　";
		String str = "";
		str = "\n\n\n\n";
		str += "                                       打印对账结果" + "\n\n\n";
		str += forMat.substring(0, 5) + "      Tips工作日期：" + WorkDate;
		str += forMat.substring(0, 5) + "\n\n";
		
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("16","收款行行号"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6","批次"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("6","类型"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8","总笔数"," ");
		payCheck += forMat.substring(0, 5) + ACEPackUtil.leftStrFormat("8","总金额"," ");
		payCheck += "\n";
		sql = "select * from paycheck where CHKDATE='"+ WorkDate +"'";
				
		//查询对账信息
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
			payCheck+= "操作数据库出错！！！";
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
				payCheck+= "操作数据库出错！！！";
				payCheck+= '\n';
				return payCheck;
			}
		}
	
		if(queryList.size() == 0)
		{
			payCheck+= '\n';
			payCheck+= "没有查询到相关记录！！！";
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
					chkAcctType = "日间";
				}
				else
				{
					chkAcctType = "日切";
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
		payCheck += forMat.substring(0, 5) + "      打印日期：" + date.get(Calendar.YEAR) + "年" + date.get(Calendar.MONTH) + "月" + date.get(Calendar.DAY_OF_MONTH) + "日";
		payCheck += "                                    柜员号： 0204";
		return str + payCheck;
	}
	
}
