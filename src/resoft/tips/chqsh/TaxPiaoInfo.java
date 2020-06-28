package resoft.tips.chqsh;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.chqxh.MoneyUtil;
import resoft.tips.util.CurrencyUtil;
import resoft.tips.util.DateTimeUtil;
/**
 * <p>税票信息</p>
 * Author: fanchengwei
 * Date: 2007-10-22
 * Time: 18:06:06
 */
public class TaxPiaoInfo {
	private static final Log logger = LogFactory.getLog(TaxPiaoInfo.class);
	String lineBytesCount="104";
	private String[] deatilNames={"TaxPayCode","TaxPayName","HandOrgName","PayAcct","PayOpBkName","TaxOrgName","PayeeOrgName","TraAmt","TraNo","TraAmtStr","TaxVodNo"};
	private String[] deatilNamesFormats={lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,lineBytesCount,"35","46","35","46"};
	
	private String sql="";
	public String piaoInfo="",taxTypeInfo="";
	private String PayOpBkName = "";
	private String TaxOrgName = "";
	public static String printTeller="",lineStartFormat="                ";
	public static int taxVodTypeCount=5,startNullRows=2;	
	
	public Map packTags=new HashMap();
	
	public TaxPiaoInfo(){
		//交易报文头
		packTags.put("TaxPayCode", "纳税人编码：");							//纳税人编码			13*2,104
		packTags.put("TaxPayName","纳税人名称：");								//纳税人名称 			104
		packTags.put("HandOrgName","缴款单位名称：");							//缴款单位名称			6*2,104  		
		packTags.put("PayAcct","付款人账号：");								//付款人帐号			6*2,104			  		
		packTags.put("PayOpBkName","付款人开户银行：");							//付款人开户银行		8*2,104
		packTags.put("TaxOrgName","征收机关名称：");							//征收机关名称			7*2,104		  					  		
		packTags.put("PayeeOrgName","收款国库(银行)名称：");					//收款国库名称			10*2,104	
		
		packTags.put("TraAmt","小写(合计)金额：￥");							//小写金额			8*2+16,22*2				  		
		packTags.put("TraNo","缴款书交易流水号：");								//交易号				9*2,30*2	  			
		
		packTags.put("TraAmtStr", "大写(合计)金额：");							//大写金额			8*2+16,38*2	
		packTags.put("TaxVodNo","税票号码：");								//税票号码			5*2+18,28
										
	}		
	
	public String initTaxPiaoInfo(Map params) throws Exception {
		
		/*int printcount=Integer.parseInt((String)params.get("printTimes"))+1;
		if(printcount==2||printcount==3)
		{
			taxTypeInfo+=ACEPackUtil.rightStrFormat("164","补打"," ");
		}*/
		String taxVodType=(String)params.get("TaxVodType");
		if (taxVodType.equals("1")) {
			sql="select * from VoucherTaxType where bizCode='1' and taxOrgCode='"+(String)params.get("taxOrgCode")+"' and entrustDate='"+(String)params.get("entrustDate")+"' and traNo='"+(String)params.get("traNo")+"' ";
		}else if(taxVodType.equals("2")){
			sql="select * from VoucherTaxType where bizCode='2' and taxOrgCode='"+(String)params.get("taxOrgCode")+"' and entrustDate='"+(String)params.get("entrustDate")+"' and packNo='"+(String)params.get("packNo")+"' and traNo='"+(String)params.get("traNo")+"' ";
		}else if(taxVodType.equals("3")){
			sql="select * from VoucherTaxType where bizCode='3' and taxOrgCode='"+(String)params.get("taxOrgCode")+"' and entrustDate='"+(String)params.get("entrustDate")+"' and traNo='"+(String)params.get("traNo")+"' ";
		}
		//logger.info("税种信息查询："+sql);
		
		packTags.put("TaxPayCode", (String)packTags.get("TaxPayCode")+(String)params.get("taxPayCode"));
		packTags.put("TaxPayName", (String)packTags.get("TaxPayName")+(String)params.get("taxPayName"));
		packTags.put("HandOrgName", (String)packTags.get("HandOrgName")+(String)params.get("handOrgName"));
		packTags.put("PayAcct", (String)packTags.get("PayAcct")+(String)params.get("payAcct"));
		packTags.put("TraAmt", (String)packTags.get("TraAmt")+MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat((String)params.get("traAmt"))));
		packTags.put("TraNo", (String)packTags.get("TraNo")+(String)params.get("TraNo"));
		packTags.put("TaxVodNo", (String)packTags.get("TaxVodNo")+(String)params.get("taxVouNo"));
		packTags.put("TraAmtStr",(String)packTags.get("TraAmtStr")+MoneyUtil.amountToChinese(Double.parseDouble((String)params.get("traAmt"))));
					
		//查询税票税种明细信息
		List queryList=QueryUtil.queryRowSet(sql);		
		if (queryList.size()>0) {
			//打印票标题	
			piaoInfo="";
			logger.info("startNullRows:"+TaxPiaoInfo.startNullRows);
			for (int i=0;i<TaxPiaoInfo.startNullRows;i++) {
				piaoInfo+="    \n";
			}	
			//行开始打印格式
			//lineStartFormat=ACEPackUtil.leftStrFormat("14", ""," ");
			//打印转账日期
			piaoInfo += lineStartFormat.substring(0, 12)+ACEPackUtil.leftStrFormat("25", ""," ")+(String)params.get("bankTraDate")
						+ "                                             " //46
						+ (String)params.get("bankTraDate") + (String)params.get("TraNo") +"\n";						
			//打印票通用信息
			for(int i=0;i<deatilNames.length;i++) {
				//不打印行起始格式
				if(deatilNames[i].equals("TraAmt")||deatilNames[i].equals("TraAmtStr")) {
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],(String)packTags.get(deatilNames[i])," ");
					continue;
				}else if(deatilNames[i].equals("PayeeOrgName")) {
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],"收款国库（银行）：国家金库重庆市分库"," ");
					piaoInfo+="\n";
					continue;
				}else if(deatilNames[i].equals("TaxOrgName")){
					try{
						TaxOrgName = DBUtil.queryForString("select TaxOrgName from TaxOrgMng where TaxOrgCode='" + (String)params.get("TaxOrgCode") + "'");
						if(TaxOrgName == null){
							TaxOrgName = " ";
						}
					}catch(Exception e){
						e.printStackTrace();
						TaxOrgName = "";
					}
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],"征收机关名称："+TaxOrgName," ");
					piaoInfo+="\n";
					continue;
				}
				{//打印行起始格式					
					piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat(deatilNamesFormats[i],(String)packTags.get(deatilNames[i])," ");
					piaoInfo+="\n";
				}
			}
			piaoInfo+=lineStartFormat+ACEPackUtil.leftStrFormat("46","税(费)种名称"," ")+ACEPackUtil.leftStrFormat("12","所属时期"," ")+ACEPackUtil.leftStrFormat("8",""," ")+ACEPackUtil.leftStrFormat("38","实缴金额"," ");
			piaoInfo+="\n";
			//打印税种明细信息
			for (int i=0;i<queryList.size();i++) {				
				Map row=(Map)queryList.get(i);
				//打印行开始格式
				taxTypeInfo+=lineStartFormat;
				//税种 60
				taxTypeInfo+=ACEPackUtil.leftStrFormat("40",(String)row.get("TaxTypeName")," ");
				//所属期限 18
				taxTypeInfo+=ACEPackUtil.leftStrFormat("9"," "+(String)row.get("TaxStartDate")," ");
				taxTypeInfo+="-";
				taxTypeInfo+=ACEPackUtil.leftStrFormat("8",(String)row.get("TaxEndDate")," ");
				//8个空格
				taxTypeInfo+=ACEPackUtil.leftStrFormat("8",""," ");
				//实缴金额 18
				taxTypeInfo+=ACEPackUtil.leftStrFormat("38","￥"+MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat((String)row.get("TaxTypeAmt")))," ");
				taxTypeInfo+="\n";
			}
			//每张税票打印15个税种
			for (int i=0;i<(taxVodTypeCount-queryList.size());i++) {
				taxTypeInfo+="\n";
			}
			//10+2+6+6+10+20+10+10+30   打印打印时间次数
			//taxTypeInfo+="     " + ACEPackUtil.leftStrFormat("10",""," ")+"第"+ACEPackUtil.leftStrFormat("6",""+(Integer.parseInt((String)params.get("printTimes"))+1)," ")
			//				+"次打印"+ACEPackUtil.leftStrFormat("10",""," ")+ACEPackUtil.leftStrFormat("20","操作员："+TaxPiaoInfo.printTeller," ")
			//				+ACEPackUtil.leftStrFormat("10",""," ")+"打印时间："+ACEPackUtil.leftStrFormat("30",DateTimeUtil.getDateTimeString()," ");
			
			//wangmingmingxiugai
						
			 taxTypeInfo+="     " + ACEPackUtil.leftStrFormat("10",""," ")+ACEPackUtil.leftStrFormat("10",""," ")+ACEPackUtil.leftStrFormat("20","操作员："+TaxPiaoInfo.printTeller," ")
				+ACEPackUtil.leftStrFormat("10",""," ")+"打印时间："+ACEPackUtil.leftStrFormat("30",DateTimeUtil.getDateTimeString()," ");
			 
			
			 
		}
		
		return piaoInfo+taxTypeInfo;
	}	
}
