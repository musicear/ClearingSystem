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
 * ������˰�˱��������Э�����ȡ����˰�˵�Э�����
 * ������˰�˵�Э����Ŵ�ʵʱ��˰�����ж�ֱ�ɡ�������˰��ϸ����ȡ��������ˮ�š���˰Ʊ����
 * ���ݽ�����ˮ�Ŵ�˰Ʊ˰����Ϣ����ȡ��˰����Ϣ
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
	 * ��ʼ����˰��ϸ��Ϣ
	 * @param TaxPayCode	����˰�˱���
	 * @param BegineDate	����ʼTips������
	 * @param EndDate		����ֹTips������
	 * @param TaxAmt		�����
	 * @return				�����ؽ�˰��ϸ��Ϣ
	 * @throws Exception	���׳�SQLException
	 */
	public String initTaxFileInfo(String PayAcct,String BegineDate,String EndDate,Message msg)
	{
		paycheckdetailInfo += format;
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("5","���"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","��������"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("10","��ˮ��"," ");	
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("15","ʵ�ɽ��"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("20","��˰�˱���"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("60","��˰������"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("13","���ջ��ش���"," ");
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("20","˰Ʊ����"," ");	
		paycheckdetailInfo += ACEPackUtil.leftStrFormat("30","˰������"," ");
		paycheckdetailInfo += '\n'; 
		/**
		 * ���ж�ֱ�ɣ�δ����
		 */
		List rtqueryList = null;
		List bpqueryList = null;
		try
		{
			//rtsql = "select taxorgcode,traamt,taxpayname,trano,taxvouno,payacct,BANKTRADATE,PROTOCOLNO from realtimepayment " +
			//"where PayAcct='"+ PayAcct +"' and replace(BANKTRADATE,'-','')>='"+ BegineDate +"' and replace(BANKTRADATE,'-','')<='"+ EndDate +"' and checkstatus='1'";
			rtsql = "select * from realtimepayment where PayAcct='"+ PayAcct +"' and BANKTRADATE>='"+ BegineDate +"' and BANKTRADATE<='"+ EndDate +"' and checkstatus='1'";
			logger.info("ʵʱ��˰SQL is:"+rtsql);
			rtqueryList=QueryUtil.queryRowSet(rtsql); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("��ѯʵʱ��˰���������");
			msg.set("AddWord", "��ѯʵʱ��˰���������");
			paycheckdetailInfo = "��ѯʵʱ��˰���������";
//			e.printStackTrace();
			paycheckdetailInfo ="�˺ţ�"+ PayAcct+" �ɿ��ܽ��Ϊ��                  " + "       �ɿ��ܴ�����" + "\n" + paycheckdetailInfo;
			return paycheckdetailInfo;
		}
		
		if (rtqueryList.size()>0) 
		{			
			for (int i=0;i<rtqueryList.size();i++) 
			{
				logger.info("����˴���˵����ѯ�ɹ���");
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
				logger.info("����2����˵����ѯ�ɹ���");
				try
				{
					taxTypeName = DBUtil.queryForString("select taxTypeName from voucherTaxType where trano='"+ (String)row.get("trano") +"'");
				}
				catch(Exception e)
				{
//					e.printStackTrace();
					taxTypeName = "δ��ȡ��˰����Ϣ";
					msg.set("AddWord", "δ��ȡ��˰����Ϣ����");
					paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
					paycheckdetailInfo += '\n';
					paycheckdetailInfo = "�˺�:"+PayAcct+" �ɿ��ܽ��Ϊ��      " + "�ɿ��ܴ�����" + "\n" + paycheckdetailInfo;
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
	
			logger.info("������˰SQL is:"+bpsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		}
		catch(Exception e)
		{
			logger.info("��ѯ������˰���������");
			msg.set("AddWord", "��ѯ������˰���������");
			paycheckdetailInfo = "��ѯ������˰���������";
//			e.printStackTrace();
			paycheckdetailInfo ="�˺ţ�"+ PayAcct+" �ɿ��ܽ��Ϊ��                  " + "       �ɿ��ܴ�����" + "\n" + paycheckdetailInfo;
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
					taxTypeName = "δ��ȡ��˰����Ϣ";
					msg.set("AddWord", "δ��ȡ��˰����Ϣ����");
					paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
					paycheckdetailInfo += '\n';
					logger.info("δ��ȡ��˰����Ϣ");
					continue;
				}
				paycheckdetailInfo +=ACEPackUtil.leftStrFormat("30",taxTypeName.trim()," ");
				paycheckdetailInfo += '\n';
			}
		}
		if(rtqueryList.size() == 0&& bpqueryList.size()==0)
		{
			paycheckdetailInfo += '\n';
			paycheckdetailInfo += "û�в�ѯ����ؼ�¼������";
			paycheckdetailInfo += '\n';
			return paycheckdetailInfo;
		}
		
		paycheckdetailInfo = "\n\n\n                            ��˰�����ϸ" + '\n' + format + "�˺ţ�"+PayAcct+" �ɿ��ܽ��Ϊ��" + CurrencyUtil.getCurrencyFormat(""+allTraAmt) + "Ԫ          �ɿ��ܴ�����" +
								count + "��                                        \n" + paycheckdetailInfo;
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

















