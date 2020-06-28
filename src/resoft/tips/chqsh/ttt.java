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
	 * 首先查出在这一个Tips工作日中有几个银行会计日
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
			logger.info("数据库操作出错，未能取到上一Tips工作日！！");
			return FAIL;
		}
		*/
		preworkdate = "20071116";
		/**
		 * 抽取实时扣税表、批量扣税明细表、银行端直接缴款表中上一Tips工作日中包含的会计日
		 * 在一个Tips工作日中一般会包含两个会计日，分别处理每一个工作日
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
			logger.info("数据库操作出错，未能取到上一Tips工作日！！");
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
			 * 按照会计日来处理记账信息
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
				logger.info("数据库操作出错，未能取到传票号！！");
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
	 * 查询一个Tips工作日中含有几个会计日
	 * @param rtIADAC_DATList	：实时扣税明细表
	 * @param bpIADAC_DATList	：批量扣税明细表
	 * @return					：会计日数组
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
			  * 如果一个List表中的数据插入完成则将另一个表的数据之直接插入即可
			  * j 记录实时List
			  */			
			 if(j == rtIADAC_DATList.size() && i != 0)
			 {
				 /**
				  * 实时录入完毕后将批量的全部录入即可
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
					  * 如果是批量的遍历结束，则将实时的录入即可
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
			  * j用于记录遍历到实时表的当前坐标
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
	 * 查询每一个会计日含有的传票号的数目
	 * @param rtvch_NoList	：实时扣税明细表
	 * @param bpvch_NoList	：批量扣税明细表
	 * @return				：传票号数组
	 */
	public int[] getVch_No(List rtvch_NoList,List bpvch_NoList)
	{
		int count = rtvch_NoList.size() + bpvch_NoList.size();
		int tl_td[] = new int[count];
		int j = 0;
		Map row = null;
		/**
		  * 把上一Tips工作日中的成功记录，按照从小到大的顺序插入整形数组tl_td中
		  */
		 for(int i=0;i<count;i++)
		 {
			 /**
			  * 如果一个List表中的数据插入完成则将另一个表的数据之直接插入即可
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
			  * j用于记录遍历到实时表的当前坐标
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
	 * 如果传票号出现中间中断、重复、除第一个会计日外未从头记录的情况，都属于异常
	 * @param tl_td					：传票号
	 * @param beforetipsworkdate	：上一Tips工作日
	 * @param IADAC_DAT				：会计日
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
			params.put("addword", "Tips工作日开始记账的传票号");
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
					logger.info("插入数据库出错");
				}
			}
			params.clear();
		}
		if((Integer.parseInt((""+id).substring(4)) !=1 ) && (IADAC_DAT < 100000000))
		{
			params.put("IADAC_DAT", ""+IADAC_DAT);
			params.put("tipsworkdate", tipsworkdate);
			params.put("vch_no", ("" + id).substring(0, 4));
			params.put("addword", "此柜员号未从头记录");
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
					logger.info("插入数据库出错");
				}
			}
			params.clear();
			params.put("IADAC_DAT", ""+IADAC_DAT);
			params.put("tipsworkdate", tipsworkdate);
			params.put("vch_no", ("" + id).substring(0, 4));
			params.put("addword", (""+id).substring(0, 4)+"柜员开始记账传票号");
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
					logger.info("插入数据库出错");
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
				params.put("addword", "银行会计日最后一笔交易");
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
						logger.info("插入数据库出错");
					}
				}
				break;
			}
			/**
			 * 传票号顺序正常则跳过这次循环
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
				* 传票号重复
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
					params.put("addword", "传票号重复");
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
							logger.info("插入数据库出错");
						}
					}
					continue;
				}
				/**
				 * 传票号空缺
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
						params.put("addword", "传票号空缺");
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
								logger.info("插入数据库出错");
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
						params.put("addword", "此柜员号未从头记录");
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
						params.put("addword", ("" + tl_td[i-1]).substring(0, 4)+"柜员最后一笔交易传票号");
						try
						{
//							DBUtil.insert("errmsg", params);
							System.out.println(""+params.get("vch_no") + "  " + params.get("addword"));
						}
						catch(Exception e)
						{
							e.printStackTrace();
							logger.info("插入数据库出错");
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
						params.put("addword", ("" + id).substring(0, 4)+"柜员开始记账传票号");
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
					logger.info("插入数据库出错");
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
