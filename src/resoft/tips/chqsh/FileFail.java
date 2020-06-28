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
			msg.set("AddWord", "�������ڲ���Ϊ�գ���");
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
				msg.set("AddWord", "δ�ܲ�ѯ����ؼ�¼����");
			}
		}
		else
		{
			msg.set("ReturnResult", "N");
			msg.set("AddWord", "���������д���Ȩ������");
		}
		
		logger.info("BranchNo" + BranchNo);
		return 0;
    }
	public String InitTraAmtCard(String sumAccont,String sumAmt,String ChkDate,String ChkAcctOrd,String UserId)
	{
		String forMat = "��������������������";
		String upTraAmt = "";
		String buf = "";
		int count = 0;
		String WorkDate = ChkDate.substring(0, 4) + "��" + ChkDate.substring(4, 6) + "��" + ChkDate.substring(6) + "��";
//		String fromatAmt = "";
		if(ChkAcctOrd.equals("1001"))
		{
			ChkAcctOrd += "���ռ䣩";
		}
		else
		{
			ChkAcctOrd += "�����У�";
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
		cardInfo += "                                       �ʽ����㻮�" + "\n\n\n";
		cardInfo += forMat.substring(0, 5) + "      Tips�������ڣ�" + WorkDate+ "                                ���Σ�" + ChkAcctOrd;
		cardInfo += forMat.substring(0, 5) + "\n\n";
		cardInfo += forMat.substring(0, 5) + "���ܱ�����" + sumAccont + "\n";
		cardInfo += forMat.substring(0, 5) + "���������ң�Сд������" + sumAmt + "Ԫ\n";
		cardInfo += forMat.substring(0, 5) + "���������ң���д����" + upTraAmt + "\n";
		cardInfo += "\n";
		cardInfo += forMat.substring(0, 5) + "      ��ӡ���ڣ�" + date.get(Calendar.YEAR) + "��" + date.get(Calendar.MONTH) + "��" + date.get(Calendar.DAY_OF_MONTH) + "��";
		cardInfo += "                                    ��Ա�ţ� " + UserId;
		return cardInfo;	
	}
}
