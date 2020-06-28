package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.CurrencyUtil;
import resoft.xlink.core.Message;

/**
 * 根据纳税人编码从三方协议表中取出纳税人的协议书号
 * 根据纳税人的协议书号从实时扣税表、银行端直缴、批量扣税明细表中取出交易流水号、金额、税票号码
 * 根据交易流水号从税票税种信息表中取出税种信息
 * @author fanchengwei
 *
 */
public class FileTaxMessage {

	private static final Log logger = LogFactory.getLog(SendMsgToBankSystem.class);
	private String rtsql = "";
	private String bpsql = "";
	private String paycheckdetailInfo = "";
	private String taxTypeName = "";
	private String format = "     ";
	private int count = 0;
	private float allTraAmt = 0;
	public Map packTags = new HashMap();
	
	public FileTaxMessage()
	{
	}
	/**
	 * 初始化缴税明细信息
	 * @param TaxPayCode	：纳税人编码
	 * @param BegineDate	：起始Tips工作日
	 * @param EndDate		：终止Tips工作日
	 * @param TaxAmt		：金额
	 * @return				：返回缴税明细信息
	 * @throws Exception	：抛出SQLException
	 */
	public String initTaxFileInfo(String PayAcct,String BegineDate,String EndDate,Message msg)
	{
		paycheckdetailInfo += format;
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("5","序号"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","记账日期"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","流水号"," ");	
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("15","实缴金额"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("20","纳税人编码"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("60","纳税人名称"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("13","征收机关代码"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("20","税票号码"," ");	
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("30","税种名称"," ");
		paycheckdetailInfo += '\n'; 
		/**
		 * 银行端直缴，未处理
		 */
		List rtqueryList = null;
		List bpqueryList = null;
		try
		{
			//rtsql = "select taxorgcode,traamt,taxpayname,trano,taxvouno,payacct,BANKTRADATE,PROTOCOLNO from realtimepayment " +
			//"where PayAcct='"+ PayAcct +"' and replace(BANKTRADATE,'-','')>='"+ BegineDate +"' and replace(BANKTRADATE,'-','')<='"+ EndDate +"' and checkstatus='1'";
			rtsql = "select * from realtimepayment where PayAcct='"+ PayAcct +"' and BANKTRADATE>='"+ BegineDate +"' and BANKTRADATE<='"+ EndDate +"' and checkstatus='1'";
			logger.info("实时缴税SQL is:"+rtsql);
			rtqueryList=QueryUtil.queryRowSet(rtsql); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("查询实时缴税结果出错！！");
			msg.set("AddWord", "查询实时缴税结果出错！！");
			paycheckdetailInfo = "查询实时缴税结果出错！！";
//			e.printStackTrace();
			paycheckdetailInfo ="账号："+ PayAcct+" 缴款总金额为：                  " + "       缴款总次数：" + "\n" + paycheckdetailInfo;
			return paycheckdetailInfo;
		}
		
		if (rtqueryList.size()>0) 
		{			
			for (int i=0;i<rtqueryList.size();i++) 
			{
				logger.info("进入此处，说明查询成功！");
				Map row=(Map)rtqueryList.get(i);
				paycheckdetailInfo += format + id(count++);
				paycheckdetailInfo += ACEPackUtil.leftStrFormat("10",(String)row.get("BANKTRADATE")," ");;
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("10",(String)row.get("trano")," ");
				String traAmt = (String)row.get("traamt");
				allTraAmt = allTraAmt + Float.parseFloat(traAmt);
				if(traAmt==null||"".equals(traAmt))
		        	   traAmt="0.00";
		        traAmt = CurrencyUtil.getCurrencyFormat(traAmt);
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("15",traAmt," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("20",(String)row.get("taxpaycode")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("60",(String)row.get("taxpayname")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("13",(String)row.get("taxorgcode")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("20",(String)row.get("taxvouno")," ");
				logger.info("进入2处，说明查询成功！");
				try
				{
					taxTypeName = DBUtil.queryForString("select taxTypeName from voucherTaxType where trano='"+ (String)row.get("trano") +"'");
				}
				catch(Exception e)
				{
//					e.printStackTrace();
					taxTypeName = "未能取到税种信息";
					msg.set("AddWord", "未能取到税种信息！！");
					paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
					paycheckdetailInfo += '\n';
					paycheckdetailInfo = "账号:"+PayAcct+" 缴款总金额为：      " + "缴款总次数：" + "\n" + paycheckdetailInfo;
					return paycheckdetailInfo;
				}
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
				paycheckdetailInfo += '\n';
			}		
		}
		
		try
		{  
			bpsql = "select * from batchpackdetail " +
			"where PayAcct='"+ PayAcct +"' and BANKTRADATE>='"+ BegineDate +"' and BANKTRADATE<='"+ EndDate +"' and checkstatus='1'";
	
			logger.info("批量缴税SQL is:"+bpsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		}
		catch(Exception e)
		{
			logger.info("查询批量缴税结果出错！！");
			msg.set("AddWord", "查询批量缴税结果出错！！");
			paycheckdetailInfo = "查询批量缴税结果出错！！";
//			e.printStackTrace();
			paycheckdetailInfo ="账号："+ PayAcct+" 缴款总金额为：                  " + "       缴款总次数：" + "\n" + paycheckdetailInfo;
			return paycheckdetailInfo;
		}
		if (bpqueryList.size()>0) 
		{			
			for (int i=0;i<bpqueryList.size();i++) 
			{				
				Map row=(Map)bpqueryList.get(i);				
				paycheckdetailInfo += format + id(count++);
				paycheckdetailInfo += ACEPackUtil.leftStrFormat("10",(String)row.get("BANKTRADATE")," ");				
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("10",(String)row.get("trano")," ");
				String traAmt = (String)row.get("traamt");
				allTraAmt = allTraAmt + Float.parseFloat(traAmt);
				if(traAmt==null||"".equals(traAmt))
		        	   traAmt="0.00";
		        traAmt = CurrencyUtil.getCurrencyFormat(traAmt); 
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("15",traAmt," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("20",(String)row.get("taxpaycode")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("60",(String)row.get("taxpayname")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("13",(String)row.get("taxorgcode")," ");
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("20",(String)row.get("taxvouno")," ");
				try
				{
					taxTypeName = DBUtil.queryForString("select taxTypeName from voucherTaxType where trano='"+ (String)row.get("trano") +"'");
				}
				catch(Exception e)
				{
//					e.printStackTrace();
					taxTypeName = "未能取到税种信息";
					msg.set("AddWord", "未能取到税种信息！！");
					paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
					paycheckdetailInfo += '\n';
					logger.info("未能取到税种信息");
					continue;
				}
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
				paycheckdetailInfo += '\n';
			}
		}
		if(rtqueryList.size() == 0&& bpqueryList.size()==0)
		{
			paycheckdetailInfo += '\n';
			paycheckdetailInfo += "没有查询到相关记录！！！";
			paycheckdetailInfo += '\n';
			return paycheckdetailInfo;
		}
		
		paycheckdetailInfo = "\n\n\n                            缴税情况明细" + '\n' + format + "账号："+PayAcct+" 缴款总金额为：" + CurrencyUtil.getCurrencyFormat(""+allTraAmt) + "元          缴款总次数：" +
								count + "次                                        \n" + paycheckdetailInfo;
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

















