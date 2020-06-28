package resoft.tips.chqsh;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.MoneyUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * select 
 * @author fanchengwei
 *
 */
public class FileFail implements Action {

	private static final Log logger = LogFactory.getLog(FileFail.class);
	private String cardInfo = "";
	private Calendar date = Calendar.getInstance();
	
	public FileFail(){
		
	}
	public int execute(Message msg) 
    {
		String BranchNo = msg.getString("BranchNo").trim();
		String ChkDate = msg.getString("WorkDate");
		
		if(ChkDate == null)
		{
			msg.set("ReturnResult", "N");
			msg.set("AddWord", "工作日期不能为空！！");
		}
		if(BranchNo.equals("0001"))
		{
			List failList = null;
			Map row = null;
			Map params = null;
			String payOpBkCode = "";
			
//			int count = 0;
			try
			{
				failList = QueryUtil.queryRowSet("select distinct payOpBkCode,sum(traAmt) from AdjustAcct where adjustStatus='1' and MSG_DATA<>'9004' and CHKDATE='"+ ChkDate +"'");
				for(int i=0;i<failList.size();i++)
				{
					row = (Map)failList.get(i);
					payOpBkCode = (String)row.get("payOpBkCode");
					params.put("payOpBkCode", payOpBkCode);
					params.put("traAmt", row.get("traAmt"));
					params.put("failCount", ""+DBUtil.queryForInt("select count()"));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				msg.set("ReturnResult", "N");
				msg.set("AddWord", "未能查询到相关记录！！");
			}
		}
		else
		{
			msg.set("ReturnResult", "N");
			msg.set("AddWord", "机构不具有此项权利！！");
		}
		
		logger.info("BranchNo" + BranchNo);
		return 0;
    }
	public String InitTraAmtCard(String sumAccont,String sumAmt,String ChkDate,String ChkAcctOrd,String UserId)
	{
		String forMat = "　　　　　　　　　　";
		String upTraAmt = "";
		String buf = "";
		int count = 0;
		String WorkDate = ChkDate.substring(0, 4) + "年" + ChkDate.substring(4, 6) + "月" + ChkDate.substring(6) + "日";
//		String fromatAmt = "";
		if(ChkAcctOrd.equals("1001"))
		{
			ChkAcctOrd += "（日间）";
		}
		else
		{
			ChkAcctOrd += "（日切）";
		}
		upTraAmt = MoneyUtil.amountToChinese(Double.parseDouble(sumAmt));
		buf = "";
		count = 0;
		for(int i=sumAmt.length();i>0;i--)
		{
			buf = sumAmt.substring(i-1, i);
			count++;
			if(buf.equals("."))
			{
				count = 0;
			}
			if(count == 3 && i != 1)
			{
				count = 0;
				sumAmt = sumAmt.substring(0, i-1) + "," + sumAmt.substring(i-1);
			}
		}
		cardInfo = "\n\n";
		cardInfo += "                                       资金清算划款单" + "\n\n\n";
		cardInfo += forMat.substring(0, 5) + "      Tips工作日期：" + WorkDate+ "                                批次：" + ChkAcctOrd;
		cardInfo += forMat.substring(0, 5) + "\n\n";
		cardInfo += forMat.substring(0, 5) + "　总笔数：" + sumAccont + "\n";
		cardInfo += forMat.substring(0, 5) + "　金额人民币（小写）：￥" + sumAmt + "元\n";
		cardInfo += forMat.substring(0, 5) + "　金额人民币（大写）：" + upTraAmt + "\n";
		cardInfo += "\n";
		cardInfo += forMat.substring(0, 5) + "      打印日期：" + date.get(Calendar.YEAR) + "年" + date.get(Calendar.MONTH) + "月" + date.get(Calendar.DAY_OF_MONTH) + "日";
		cardInfo += "                                    柜员号： " + UserId;
		return cardInfo;	
	}
}
