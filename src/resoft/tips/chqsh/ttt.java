package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

//import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class ttt implements Action{

	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWrongMsg.class);
	/**
	 * ���Ȳ������һ��Tips���������м������л����
	 */
	public int execute(Message msg) { 
		
		String preworkdate = null;
		/**
		try
		{
			preworkdate = DBUtil.queryForString("select preworkdate from sysstatus");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("���ݿ��������δ��ȡ����һTips�����գ���");
			return FAIL;
		}
		*/
		preworkdate = "20071116";
		/**
		 * ��ȡʵʱ��˰��������˰��ϸ�����ж�ֱ�ӽɿ������һTips�������а����Ļ����
		 * ��һ��Tips��������һ��������������գ��ֱ���ÿһ��������
		 */
		String rtIADAC_DAT = "select distinct(IADAC_DAT) from realtimepayment where tipsworkdate='"+ preworkdate +"'";
		String bpIADAC_DAT = "select distinct(IADAC_DAT) from batchpackdetail where tipsworkdate='"+ preworkdate +"'";
		List rtIADAC_DATList = null;
		List bpIADAC_DATList = null;
		try
		{
			rtIADAC_DATList = QueryUtil.queryRowSet(rtIADAC_DAT);
			bpIADAC_DATList = QueryUtil.queryRowSet(bpIADAC_DAT);
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.info("���ݿ��������δ��ȡ����һTips�����գ���");
			return FAIL;
		}
		int IADAC_DAT[] = getIADAC_DAT(rtIADAC_DATList,bpIADAC_DATList);
		if(IADAC_DAT.length ==0)
		{
			return SUCCESS;
		}
		for(int date=0;date<IADAC_DAT.length;date++)
		{
			/**
			 * ���ջ���������������Ϣ
			 */
			String rtsql = "select vch_no from realtimepayment where MSG_DATA='9004' and tipsworkdate='"+ preworkdate +"' and IADAC_DAT='"+ IADAC_DAT[date] +"' order by vch_no";
			String bpsql = "select vch_no from batchpackdetail where MSG_DATA='9004' and tipsworkdate='"+ preworkdate +"' and IADAC_DAT='"+ IADAC_DAT[date] +"' order by vch_no";
			List rtvch_NoList = null;
			List bpvch_NoList = null;
			try
			{
				rtvch_NoList = QueryUtil.queryRowSet(rtsql);
				bpvch_NoList = QueryUtil.queryRowSet(bpsql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("���ݿ��������δ��ȡ����Ʊ�ţ���");
				return FAIL;
			}
			int tl_td[] = getVch_No(rtvch_NoList,bpvch_NoList);
			if(date == 0)
			{
				getError(tl_td,preworkdate,100000000+IADAC_DAT[date]);
			}
			else
			{
				getError(tl_td,preworkdate,IADAC_DAT[date]);
			}
			
		}
		return SUCCESS;
	}
	/**
	 * ��ѯһ��Tips�������к��м��������
	 * @param rtIADAC_DATList	��ʵʱ��˰��ϸ��
	 * @param bpIADAC_DATList	��������˰��ϸ��
	 * @return					�����������
	 */
	public int[] getIADAC_DAT(List rtIADAC_DATList,List bpIADAC_DATList)
	{
		int count = rtIADAC_DATList.size() + bpIADAC_DATList.size();
		int IADAC_DAT[] = new int[count];
		int j = 0;
		int k = 0;
		Map row = null;
		if(rtIADAC_DATList.size() == 0 && bpIADAC_DATList.size() == 0)
		{
			int DAT[] = new int[k];
			return DAT;
		}
		if(rtIADAC_DATList.size() == 0 || bpIADAC_DATList.size() == 0)
		{
			if(rtIADAC_DATList.size() == 0 && bpIADAC_DATList.size() != 0)
			{
				for(int i=0;i<bpIADAC_DATList.size();i++)
				{
					row = (Map)bpIADAC_DATList.get(i);
					String srt = (String)row.get("IADAC_DAT");
					if(srt.equals("") || srt.equals(null))
					{
						int DAT[] = new int[k];
						return DAT;
					}
					IADAC_DAT[k++] = Integer.parseInt((String)row.get("IADAC_DAT"));
				}
			}
			else
			{
				if(rtIADAC_DATList.size() != 0 && bpIADAC_DATList.size() == 0)
				{
					for(int i=0;i<rtIADAC_DATList.size();i++)
					{
						row = (Map)rtIADAC_DATList.get(i);
						String srt = (String)row.get("IADAC_DAT");
						if(srt.equals("") || srt.equals(null))
						{
							int DAT[] = new int[k];
							return DAT;
						}
						row = (Map)rtIADAC_DATList.get(i);
						IADAC_DAT[k++] = Integer.parseInt((String)row.get("IADAC_DAT"));
					}
				}
			}
			int DAT[] = new int[k];
			for(int i=0;i<k;i++)
			{
				DAT[i] = IADAC_DAT[i];
			}
			return DAT;
		}
		for(int i=0;i<count;i++)
		{
			/**
			  * ���һ��List���е����ݲ����������һ���������ֱ֮�Ӳ��뼴��
			  * j ��¼ʵʱList
			  */			
			 if(j == rtIADAC_DATList.size() && i != 0)
			 {
				 /**
				  * ʵʱ¼����Ϻ�������ȫ��¼�뼴��
				  */
				 for(;i<count;i++)
				 {
					 row=(Map)bpIADAC_DATList.get(i - j); 
					 if(Integer.parseInt((String)row.get("IADAC_DAT")) == IADAC_DAT[i-1])
					 {
						 continue;
					 }
					 IADAC_DAT[k++] = Integer.parseInt((String)row.get("IADAC_DAT"));
				 }
				 break;
			 }
			 else
			 {
				 if((i-j) == bpIADAC_DATList.size() && i != 0)
				 {
					 /**
					  * ����������ı�����������ʵʱ��¼�뼴��
					  */
					 for(;i<count;i++)
					 {
						 row=(Map)rtIADAC_DATList.get(i-j); 
						 if(Integer.parseInt((String)row.get("IADAC_DAT")) == IADAC_DAT[i-1])
						 {
							 continue;
						 }
						 IADAC_DAT[k++] = Integer.parseInt((String)row.get("IADAC_DAT"));
						 j++;
					 }
				 }
			 }
			 /**
			  * j���ڼ�¼������ʵʱ��ĵ�ǰ����
			  */
			 row = (Map)rtIADAC_DATList.get(j);
			 String rt = (String)row.get("IADAC_DAT");
			 row = (Map)bpIADAC_DATList.get(i-j);
			 String bp = (String)row.get("IADAC_DAT");
			 if(Integer.parseInt(rt) <= Integer.parseInt(bp))
			 {
				 j++;
				 IADAC_DAT[k++] = Integer.parseInt(rt);
			 }
			 else
			 {
				 IADAC_DAT[k++] = Integer.parseInt(bp);
			 }
		 }
		int DAT[] = new int[k];
		for(int i=0;i<k;i++)
		{
			DAT[i] = IADAC_DAT[i];
		}
		return DAT;
	}
	/**
	 * ��ѯÿһ������պ��еĴ�Ʊ�ŵ���Ŀ
	 * @param rtvch_NoList	��ʵʱ��˰��ϸ��
	 * @param bpvch_NoList	��������˰��ϸ��
	 * @return				����Ʊ������
	 */
	public int[] getVch_No(List rtvch_NoList,List bpvch_NoList)
	{
		int count = rtvch_NoList.size() + bpvch_NoList.size();
		int tl_td[] = new int[count];
		int j = 0;
		Map row = null;
		/**
		  * ����һTips�������еĳɹ���¼�����մ�С�����˳�������������tl_td��
		  */
		 for(int i=0;i<count;i++)
		 {
			 /**
			  * ���һ��List���е����ݲ����������һ���������ֱ֮�Ӳ��뼴��
			  */
			 if(j == rtvch_NoList.size())
			 {
				 for(;i<count;i++)
				 {
					 row = (Map)bpvch_NoList.get(i-j);
					 tl_td[i] = Integer.parseInt((String)row.get("vch_no"));
				 }
				 break;
			 }
			 else
			 {
				 if((i-j) == bpvch_NoList.size())
				 {
					 for(;i<count;i++)
					 {
						row = (Map)rtvch_NoList.get(j++);
					 	tl_td[i] = Integer.parseInt((String)row.get("vch_no"));
					 }
					 break;
				 }
			 }
			 /**
			  * j���ڼ�¼������ʵʱ��ĵ�ǰ����
			  */
			 row = (Map)rtvch_NoList.get(j);
			 String rt = (String)row.get("vch_no");
			 row = (Map)bpvch_NoList.get(i-j);
			 String bp = (String)row.get("vch_no");
			 if(Integer.parseInt(rt) < Integer.parseInt(bp))
			 {
				 j++;
				 tl_td[i] = Integer.parseInt(rt);
			 }
			 else
			 {
				 tl_td[i] = Integer.parseInt(bp);
			 }
		 }
		return tl_td;
	}
	/**
	 * �����Ʊ�ų����м��жϡ��ظ�������һ���������δ��ͷ��¼��������������쳣
	 * @param tl_td					����Ʊ��
	 * @param beforetipsworkdate	����һTips������
	 * @param IADAC_DAT				�������
	 */
	public void getError(int tl_td[],String tipsworkdate,int IADAC_DAT)
	{
		Map params = new HashMap();
		int id = tl_td[0];
		int count = 0;
		
		if(IADAC_DAT > 100000000)
		{
			params.put("IADAC_DAT", ""+(IADAC_DAT-100000000));
			params.put("tipsworkdate", tipsworkdate);
			params.put("vch_no", ""+id);
			params.put("addword", "Tips�����տ�ʼ���˵Ĵ�Ʊ��");
			if(params.size() > 0)
			{
				try
				{
//					DBUtil.insert("errmsg", params);
					System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.info("�������ݿ����");
				}
			}
			params.clear();
		}
		if((Integer.parseInt((""+id).substring(4)) !=1 ) && (IADAC_DAT < 100000000))
		{
			params.put("IADAC_DAT", ""+IADAC_DAT);
			params.put("tipsworkdate", tipsworkdate);
			params.put("vch_no", ("" + id).substring(0, 4));
			params.put("addword", "�˹�Ա��δ��ͷ��¼");
			if(params.size() > 0)
			{
				try
				{
//					DBUtil.insert("errmsg", params);
					System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.info("�������ݿ����");
				}
			}
			params.clear();
			params.put("IADAC_DAT", ""+IADAC_DAT);
			params.put("tipsworkdate", tipsworkdate);
			params.put("vch_no", ("" + id).substring(0, 4));
			params.put("addword", (""+id).substring(0, 4)+"��Ա��ʼ���˴�Ʊ��");
			if(params.size() > 0)
			{
				try
				{
//					DBUtil.insert("errmsg", params);
					System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.info("�������ݿ����");
				}
			}
			params.clear();
		}
		for(int i=0;i<tl_td.length;i++)
		{
			if(i == tl_td.length)
			{
				if(IADAC_DAT > 100000000)
				{
					params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
				}
				else
				{
					params.put("IADAC_DAT", ""+IADAC_DAT);
				}	
				params.put("tipsworkdate", tipsworkdate);
				params.put("vch_no", "" + id);
				params.put("addword", "���л�������һ�ʽ���");
				logger.info("vch_no = " + params.get("vch_no") + "     " + "addword = " + params.get("addword"));
				if(params.size() > 0)
				{
					try
					{
//						DBUtil.insert("errmsg", params);
						System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
					}
					catch(Exception e)
					{
						e.printStackTrace();
						logger.info("�������ݿ����");
					}
				}
				break;
			}
			/**
			 * ��Ʊ��˳���������������ѭ��
			 */
			//System.out.println(id+count + "      " + tl_td[i]);
			if((id+count) == tl_td[i])
			{
				count++;
				continue;
			}
			else
			{
				/**
				* ��Ʊ���ظ�
				*/
				if(tl_td[i-1] == tl_td[i])
				{
					//id = id - 1;
					//count++;
					if(IADAC_DAT > 100000000)
					{
						params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
					}
					else
					{
						params.put("IADAC_DAT", ""+IADAC_DAT);
					}			
					params.put("tipsworkdate", tipsworkdate);
					params.put("vch_no", "" + id);
					params.put("addword", "��Ʊ���ظ�");
					logger.info("vch_no = " + params.get("vch_no") + "     " + "addword = " + params.get("addword"));
					if(params.size() > 0)
					{
						try
						{
//							DBUtil.insert("errmsg", params);
							System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
						}
						catch(Exception e)
						{
							e.printStackTrace();
							logger.info("�������ݿ����");
						}
					}
					continue;
				}
				/**
				 * ��Ʊ�ſ�ȱ
				 */
				if((""+id).substring(0, 4).equals((""+tl_td[i]).substring(0, 4) ))
				{
					int j = 0;
					for(;j<(tl_td[i] - (id + count));j++)
					{
						if(IADAC_DAT > 100000000)
						{
							params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
						}
						else
						{
							params.put("IADAC_DAT", ""+IADAC_DAT);
						}	
						params.put("tipsworkdate", tipsworkdate);
						params.put("vch_no", "" + (id + count + j));
						params.put("addword", "��Ʊ�ſ�ȱ");
						logger.info("vch_no = " + params.get("vch_no") + "     " + "addword = " + params.get("addword"));
						//System.out.println("vch_no = " + params.get("vch_no") + "     " + "addword = " + params.get("addword"));
						if(params.size() > 0)
						{
							try
							{
//								DBUtil.insert("errmsg", params);
								System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
							}
							catch(Exception e)
							{
								e.printStackTrace();
								logger.info("�������ݿ����");
							}
						}
						params.clear();
					}
					count = count + 1 + j;			
				}
				else
				{
					id = tl_td[i];
					count = 0;
					i = i-1;
					if((Integer.parseInt((""+id).substring(4)) !=1 ) && (IADAC_DAT < 100000000))
					{
						if(IADAC_DAT > 100000000)
						{
							params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
						}
						else
						{
							params.put("IADAC_DAT", ""+IADAC_DAT);
						}	
						params.put("tipsworkdate", tipsworkdate);
						params.put("vch_no", ("" + id).substring(0, 4));
						params.put("addword", "�˹�Ա��δ��ͷ��¼");
					}
					else
					{
						if(IADAC_DAT > 100000000)
						{
							params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
						}
						else
						{
							params.put("IADAC_DAT", ""+IADAC_DAT);
						}	
						params.put("tipsworkdate", tipsworkdate);
						params.put("vch_no", ""+tl_td[i-1]);
						params.put("addword", ("" + tl_td[i-1]).substring(0, 4)+"��Ա���һ�ʽ��״�Ʊ��");
						try
						{
//							DBUtil.insert("errmsg", params);
							System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
						}
						catch(Exception e)
						{
							e.printStackTrace();
							logger.info("�������ݿ����");
						}
						params.clear();
						if(IADAC_DAT > 100000000)
						{
							params.put("IADAC_DAT", (""+IADAC_DAT).substring(1));
						}
						else
						{
							params.put("IADAC_DAT", ""+IADAC_DAT);
						}	
						params.put("tipsworkdate", tipsworkdate);
						params.put("vch_no", ""+id);
						params.put("addword", ("" + id).substring(0, 4)+"��Ա��ʼ���˴�Ʊ��");
					}
				}
			}
			if(params.size() > 0)
			{
				try
				{
//					DBUtil.insert("errmsg", params);
					System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.info("�������ݿ����");
				}
			}
			params.clear();
			continue;
		}
	}
	public static void main(String args[])
	{
		ttt test = new ttt();
		Message msg = null;
		try {
			test.execute(msg);
			System.out.println("Program is over !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
