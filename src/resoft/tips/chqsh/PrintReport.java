package resoft.tips.chqsh;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.bankImpl.qhdyh.SelectPrintInfoPack;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.tips.util.FTPUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 *<p>宽行打印成功对账报表 行：(66+8*2=82*2=164);列：(27*2+6*2=54+12=66)</p>
 * Author: liwei
 * Date: 2007-11-05
 * Time: 10:26:06
 * 
 * Update by liwei 2008.10.25
 * Description：修改了对于异常报表的查询SQL处理  
 */
public class PrintReport implements Action{  
	private static final Log logger = LogFactory.getLog(PrintReport.class);
	private static Configuration conf = Configuration.getInstance();
	
	String printType="",queryCount="",collectRowInfo="",TRANO="";
	String sql="",sqlWhere="";
	String tradeStatus="";  
	String fileName = "",bankOrgCode="",inputTeller="",payOpBkName="",title="",rowInfo="";
	int pages=0,curPage=1,curRowIndex=1,rowByteCount=164;

	int pageCount = 112;
	int queryRowsCount=0;
    String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");   

	String[] titleList={"委托日期","收款行行号","征收机构代码","交易金额","流水号","付款人帐号","纳税人编码","记账日期","处理结果","纳税人名称"};
	String[] deatilList={"EntrustDate","PayeeBankNo","TaxOrgCode","TraAmt","TraNo","PayAcct","TaxPayCode","BankTraDate","AddWord","TaxPayName"};	
	String[] deatilListFormat={"10","13","13","19","9","17","16","10","9","30"};
	
	String[] failTitleList={"记账日期","流水号","付款人帐号","交易金额","纳税人编码","纳税人名称","征收机构代码","处理结果"};
	String[] failDeatilList={"Iadac_dat","TraNo","PayAcct","TraAmt","TaxPayCode","TaxPayName","TaxOrgCode","msg_data"};	
	String[] failDeatilListFormat={"10","10","18","16","21","40","15","30"};
	
	String[] mozhangTitleList={"账号","金额","核心日期","传票号","网点号","差错原因"};
	String[] mozhangDeatilList={"PayAcct","TraAmt","IADAC_DAT","VCH_NO","BRANCHNO","REASON"};
	String[] mozhangDeatitleListFormat={"18","15","12","10","10","30"};
	private String RET_STATUS="",SEQ_NO="",SOURCE_BRANCH_NO="",DEST_BRANCH_NO="",BRANCH_ID="",RET_CODE="",RET_MSG="";
	SelectPrintInfoPack chargeinfopack=new SelectPrintInfoPack();
	private static final SendMsgToBankSystem send = new SendMsgToBankSystem();
	private String rcvMsg = "";
	private String ENTRUSTDATE;
	private String TRAAMT;
	private String TAXPAYCODE;
	private String TAXPAYNAME;
	private String PAYACCT;
	private String HANDORGNAME;
	private String PAYOPBKCODE;
	private String TAX_NAME;
	private String TIPS_SEQ;
	private String PRINTEGERTIMES;
	private String TAX_TYPE_NAME;
	private String TAX_TIME;
	private String TAX_AMT;
	private String PayAcct;
	private String BegineDate;
	private String EndDate;
	private String MESSAGE_TYPE;
	private String MESSAGE_CODE;
	private String TRAN_DATE;
	private String TRAN_TIMESTAMP;
	private String SERVICE_CODE;
	private String TAXORGCODE;
	private String BANKTRADATE;
	private String taxOrgCode;
	private String TaxVouNo;
	private String PAYEENAME;
	private String PACKNO;
	private String PAYEEBANKNO; 
	public int execute(Message msg)throws Exception{
		BANKTRADATE=(msg.getString("TRAN_TIME")).trim();					//TIPS日期
		TRANO=(msg.getString("TRANO")).trim();//tips交易流水号
		TAXORGCODE=(msg.getString("TAXORGCODE")).trim();
		SEQ_NO=msg.getString("SEQ_NO"); 
		try
		{
			SOURCE_BRANCH_NO=msg.getString("SOURCE_BRANCH_NO");
			DEST_BRANCH_NO=msg.getString("DEST_BRANCH_NO");
			BRANCH_ID=msg.getString("BRANCH_ID");
			MESSAGE_TYPE=Integer.parseInt(msg.getString("MESSAGE_TYPE"))+10+"";
			MESSAGE_CODE=msg.getString("MESSAGE_CODE");
			TRAN_DATE=msg.getString("TRAN_DATE");
			TRAN_TIMESTAMP="";
			SERVICE_CODE=msg.getString("SERVICE_CODE");
		}
		catch(Exception e)
		{
			
		}
		Selctinfo(msg);
		chargeinfopack.CreateSystemHeadData(RET_STATUS, BRANCH_ID, SOURCE_BRANCH_NO, DEST_BRANCH_NO,MESSAGE_TYPE,MESSAGE_CODE,TRAN_DATE,TRAN_TIMESTAMP, RET_CODE, RET_MSG, SEQ_NO,SERVICE_CODE);
		 String sendMsg = chargeinfopack.MakeUpCheckPackager(RET_STATUS);
		msg.set("packFile", "T3100returnsm");
		msg.set("sendMsg", sendMsg);
		
		
   		 
		return SUCCESS;
	}
	private void Selctinfo(Message msg) {
		// TODO Auto-generated method stub
		//String traDate=msg.getString("TRAN_TIME");
		String TRANO=msg.getString("TRANO");
		String SelectStr="select * from realtimepayment" +
				" where trano='"+TRANO+"' and BANKTRADATE='"+BANKTRADATE+"' and taxorgcode ='"+TAXORGCODE+"'";
		logger.info("实时扣税sql       "+SelectStr);
		String batchSelectStr="select * from batchpackdetail" +
				" where trano='"+TRANO+"' and BANKTRADATE='"+BANKTRADATE+"' and taxorgcode ='"+TAXORGCODE+"'";
		try
		{
			List realtrowlist=QueryUtil.queryRowSet(SelectStr); 
			List batchrowlist=QueryUtil.queryRowSet(batchSelectStr);
			if(realtrowlist.size()+batchrowlist.size()==0)
			{
				RET_STATUS="F";
				RET_CODE="222222";
				RET_MSG="无次记录";
				return;
			}
			for (int i=0;i<realtrowlist.size();i++) 
			{  
				Map row=(Map)realtrowlist.get(i); 
				ENTRUSTDATE =(String)row.get("ENTRUSTDATE");
				TRAAMT =(String)row.get("TRAAMT");
				TAXPAYCODE=(String)row.get("TAXPAYCODE");
				TAXPAYNAME=(String)row.get("TaxPayName");
				taxOrgCode=(String)row.get("taxOrgCode");
				PAYACCT=(String)row.get("payAcct");
				HANDORGNAME=(String)row.get("handOrgName");
				PAYOPBKCODE=(String)row.get("payOpBkCode");
				TAX_NAME=GetTaxPayName((String)row.get("taxorgcode"));
				PAYEENAME=(String)row.get("PAYEENAME");
				//TAX_NAME=(String)row.get("taxorgcode");
				TaxVouNo=(String)row.get("TaxVouNo");//新添加
				TIPS_SEQ=TRANO; 
				PRINTEGERTIMES= (String)row.get("printtimes");
				if(TRAAMT==null||"".equals(TRAAMT))
					TRAAMT="0.00";   
				//String PRINTCOUNT =(String)row.get("printcount"); 
				String TAXNAME=(String)row.get("taxpayname");
				chargeinfopack.CreateBodyData(ENTRUSTDATE, TRAAMT, TAXPAYCODE, TAXPAYNAME, PAYACCT, HANDORGNAME,PAYOPBKCODE,TAX_NAME,TIPS_SEQ,PRINTEGERTIMES,PAYEENAME,TaxVouNo);
				String VoucherTaxTypeselect="select * from VoucherTaxType" +
						" where traNo='"+TRANO+"' and entrustdate='"+ENTRUSTDATE+"' and taxOrgCode='"+ taxOrgCode+"'";
			
				List taxTypelist=QueryUtil.queryRowSet(VoucherTaxTypeselect);
				logger.info(VoucherTaxTypeselect+"  taxTypelist         "+taxTypelist.size());
				chargeinfopack.bodyarray_data=  new String[taxTypelist.size()][3];
				for(int j=0;j<taxTypelist.size();j++)
				{
					Map taxtyperow=(Map)taxTypelist.get(j);
					TAX_TYPE_NAME=(String)taxtyperow.get("TaxTypeName");
					TAX_TIME=(String)taxtyperow.get("TaxStartDate")+"-"+(String)taxtyperow.get("TaxEndDate");;
					TAX_AMT=(String)taxtyperow.get("TaxTypeAmt");
					chargeinfopack.CreateBodyDataArray(j,TAX_TYPE_NAME,TAX_TIME,TAX_AMT);
				}
				//更新打印次数
				int printtimes=Integer.parseInt((String)row.get("printtimes"))+1;
				String updatePrintcount="update realtimepayment set printtimes='"+printtimes+"' where trano='"+TRANO+"' and BANKTRADATE='"+BANKTRADATE+"' and taxorgcode ='"+TAXORGCODE+"'";

				DBUtil.executeUpdate(updatePrintcount);
				String timestr="select printtimes from realtimepayment where trano='"+TRANO+"' and BANKTRADATE='"+BANKTRADATE+"' and taxorgcode ='"+TAXORGCODE+"'";
				logger.info(DBUtil.queryForString(timestr));
			}	
			for (int i=0;i<batchrowlist.size();i++) 
			{  
				Map row=(Map)batchrowlist.get(i); 
				ENTRUSTDATE =(String)row.get("ENTRUSTDATE");
				TRAAMT =(String)row.get("TRAAMT");
				TAXPAYCODE=(String)row.get("TAXPAYCODE");
				TAXPAYNAME=(String)row.get("TaxPayName");
				PAYACCT=(String)row.get("payAcct");
				taxOrgCode=(String)row.get("taxOrgCode");
				HANDORGNAME=(String)row.get("handOrgName");
				PAYOPBKCODE=(String)row.get("payOpBkCode");
				TAX_NAME=GetTaxPayName((String)row.get("taxorgcode"));
				//TAX_NAME=(String)row.get("taxorgcode");
				TaxVouNo=(String)row.get("TaxVouNo");//新添加
				PACKNO=(String)row.get("PACKNO");
				PAYEEBANKNO=(String)row.get("PAYEEBANKNO");
				PAYEENAME=GetPayeeName(taxOrgCode, ENTRUSTDATE, PACKNO, PAYEEBANKNO);
				TIPS_SEQ=TRANO;//渠道流水号
				PRINTEGERTIMES= (String)row.get("printtimes");
				int printtimes_add = Integer.parseInt(PRINTEGERTIMES)+1;
				PRINTEGERTIMES= Integer.toString(printtimes_add);
				if(TRAAMT==null||"".equals(TRAAMT))
					TRAAMT="0.00";     
				String TAXNAME=(String)row.get("taxpayname"); 
				chargeinfopack.CreateBodyData(ENTRUSTDATE, TRAAMT, TAXPAYCODE, TAXPAYNAME, PAYACCT, HANDORGNAME,PAYOPBKCODE,TAX_NAME,TIPS_SEQ,PRINTEGERTIMES,PAYEENAME,TaxVouNo);
				String VoucherTaxTypeselect="select * from VoucherTaxType" +
						" where traNo='"+TRANO+"' and ENTRUSTDATE='"+ENTRUSTDATE+"' and taxOrgCode='"+ taxOrgCode+"'";
			
				List taxTypelist=QueryUtil.queryRowSet(VoucherTaxTypeselect);
				logger.info("taxTypelist         "+taxTypelist.size());
				chargeinfopack.bodyarray_data=  new String[taxTypelist.size()][3];
				for(int j=0;j<taxTypelist.size();j++)
				{
					Map taxtyperow=(Map)taxTypelist.get(j);
					TAX_TYPE_NAME=(String)taxtyperow.get("TaxTypeName");
					TAX_TIME=(String)taxtyperow.get("TaxStartDate")+"-"+(String)taxtyperow.get("TaxEndDate");
					TAX_AMT=(String)taxtyperow.get("TaxTypeAmt");
					chargeinfopack.CreateBodyDataArray(j,TAX_TYPE_NAME,TAX_TIME,TAX_AMT);
				}
				//更新打印次数
				int printtimes=Integer.parseInt(PRINTEGERTIMES)+1;
				String updatePrintcount="update batchpackdetail set printtimes='"+printtimes+"' where trano='"+TRANO+"' and BANKTRADATE='"+BANKTRADATE+"' and taxorgcode ='"+TAXORGCODE+"'";

				DBUtil.executeUpdate(updatePrintcount); 
			}	
			RET_STATUS="S";
			RET_CODE="000000";
			RET_MSG="成功";
		}
		catch(SQLException e)
		{}
		try
		{
			sql="select a.*,b.PayeeBankNo,b.PayeeAcct,b.PayeeOrgCode,b.PayeeName from batchPackDetail a,BatchPackage b where a.taxOrgCode=b.taxOrgCode and a.packNo=b.packNo and a.entrustDate=b.entrustDate "+sqlWhere;		      
			List batchList=QueryUtil.queryRowSet(sql);
			for(int i=0;i<batchList.size();i++)
			{
				
			}
		}
		catch(SQLException e)
		{}
	}
	private String GetPayeeName(String taxOrgCode2, String eNTRUSTDATE2,
			String pACKNO2, String pAYEEBANKNO2) throws SQLException {
		// TODO Auto-generated method stub

		String strsel="select PAYEENAME from batchpackage where taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+eNTRUSTDATE2+"' and PACKNO='"+pACKNO2+"' and PAYEEBANKNO='"+pAYEEBANKNO2+"'";
		String name=DBUtil.queryForString(strsel);
		return name;
	}
	private String GetTaxPayName(String taxorgcode) {
		// TODO Auto-generated method stub
		String str="select taxOrgName from TaxOrgMng where TaxOrgCode='"+taxorgcode+"'";
		logger.info("           "+str);
		try
		{
			String taxorgname= DBUtil.queryForString(str);
			return taxorgname;
		}
		catch(Exception e)
		{ 
			return null;
		}
		
	}
	/**
	 * 生成抹帐失败清单
	 */
	/*public String makereport(String bankOrgCode){
		
		try {
			File f = new File(tmpPath, fileName);
			Writer writer = new FileWriter(f);
			double mozhangSumAmt=0.00;
			String tmpSql="",tmpSqlWhere=" 1=1 ",orderSql=" order by BRANCHNO,IADAC_DAT,VCH_NO ";
			//清算中心统计下属支行数据，网点只统计网点的数据
			if (getStatus(bankOrgCode) == 0) {
				tmpSqlWhere+=" and a.BRANCHNO='"+ bankOrgCode +"' ";
			}
			if(getStatus(bankOrgCode) == 1){
				tmpSqlWhere+=" and a.PayeeBankNo='"+ getPayeeBankNo(bankOrgCode) +"' ";
			}
			//根据TIPS日期过滤数据
			tmpSqlWhere+=" and a.chkDate='"+tipsDate+"' ";			
						
			//处理批量
			tmpSql="select * from adjustAcct a where "+tmpSqlWhere;
			logger.info("查询批量抹帐失败清单 SQL is："+tmpSql);
			List adjustList=QueryUtil.queryRowSet(tmpSql);
			
			queryRowsCount=(adjustList.size())+1;	
			//总页数
			pages=(queryRowsCount)/pageCount+((queryRowsCount)%pageCount>0?1:0);
						
			//处理批量
			for (int i=0;i<adjustList.size();i++) {
				((Map)adjustList.get(i)).put("traAmt", CurrencyUtil.getCurrencyFormat((String)((Map)adjustList.get(i)).get("traAmt")));
				printPageInfo(writer,(Map)adjustList.get(i),Integer.parseInt(printType));
				mozhangSumAmt+=Double.parseDouble((String)((Map)adjustList.get(i)).get("traAmt"));
			}
			
			collectRowInfo = ACEPackUtil.leftStrFormat("50", ""," ")+"TIPS日期:"+tipsDate+",总笔数："+(adjustList.size())+",总金额："+CurrencyUtil.getCurrencyFormat(""+mozhangSumAmt) + "\n";
			
			writer.write(collectRowInfo);
			writer.flush();
			writer.close();
			logger.info("消息存放于:" + f.getAbsolutePath());					
			return ""+(adjustList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询抹账信息出错！");
			return "0" ;
		}
	}
	
	/**
	 * 1：成功报表；2：差错报表
	 */
	/*public String makeReport(int flag)throws Exception {
		File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);
		//查询实时
		sql="select * from realTimePayMent a where 1=1 "+sqlWhere;		     
		List realList=QueryUtil.queryRowSet(sql);		
		logger.info("查询实时："+sql);
		//查询批量
		sql="select a.*,b.PayeeBankNo,b.PayeeAcct,b.PayeeOrgCode,b.PayeeName from batchPackDetail a,BatchPackage b where a.taxOrgCode=b.taxOrgCode and a.packNo=b.packNo and a.entrustDate=b.entrustDate "+sqlWhere;		      
		List batchList=QueryUtil.queryRowSet(sql);
		logger.info("查询批量："+sql);
		//对账总金额
        double checkSumAmt=0.00;	
		//要打印的明细行数，包括合计
		queryRowsCount=(realList.size()+batchList.size())+1;	
		//总页数
		pages=(queryRowsCount)/pageCount+((queryRowsCount)%pageCount>0?1:0);
		//实时
		for(int i=0;i<realList.size();i++){
			((Map)realList.get(i)).put("traAmt", CurrencyUtil.getCurrencyFormat((String)((Map)realList.get(i)).get("traAmt")));
			printPageInfo(writer,(Map)realList.get(i),flag);
			checkSumAmt+=Double.parseDouble((String)((Map)realList.get(i)).get("traAmt"));
		}
		//批量
		for(int i=0;i<batchList.size();i++){
			((Map)batchList.get(i)).put("traAmt", CurrencyUtil.getCurrencyFormat((String)((Map)batchList.get(i)).get("traAmt")));
			printPageInfo(writer,(Map)batchList.get(i),flag);
			checkSumAmt+=Double.parseDouble((String)((Map)batchList.get(i)).get("traAmt"));
		}
		collectRowInfo += ACEPackUtil.leftStrFormat("104", ""," ")+"TIPS日期:"+tipsDate+",总笔数："+(realList.size()+batchList.size())+",总金额："+CurrencyUtil.getCurrencyFormat(""+checkSumAmt) + "\n";
		//打印汇总信息	 
	
		writer.write(collectRowInfo);
		writer.flush();
		writer.close();
		logger.info("消息存放于:" + f.getAbsolutePath());					
		return ""+(realList.size()+batchList.size());
	}	
	//打印页面信息
	/*public void printPageInfo(Writer wriTmp,Map temp,int flag) throws Exception{
		if (curRowIndex==1) {
			//设置标题
			makeTitle(flag);	
			wriTmp.write(title);
			wriTmp.flush();
		}		
		wriTmp.write(makeRowFormat(temp,flag));
		wriTmp.flush();
		curRowIndex+=1;
		if ( (curRowIndex-1) == pageCount ){
			//换页
			curRowIndex=1;
			curPage++;
			//wriTmp.write("\n\n");
			wriTmp.write("\f");
			wriTmp.flush();
		}
	}
	//设置行格式
	/*public String makeRowFormat(Map temp,int flag){
		rowInfo="";
		if(flag == 0){
			for(int i=0;i<deatilList.length;i++){
					//"收款人名称" 和 "缴款单位名称"  只打印15个汉字
					if (deatilList[i].equals("HandOrgName")) {
						try{
							rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], ((String)temp.get(deatilList[i])).substring(0,(Integer.parseInt(deatilListFormat[i])/2)), " ");
						}catch(Exception e){
							rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], (String)temp.get(deatilList[i]), " ");
						}
					}else {
						rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], (String)temp.get(deatilList[i]), " ");
					}
				}
		}
		 if(flag == 1){
			 for(int i=0;i<failDeatilList.length;i++){
				    //"收款人名称" 和 "缴款单位名称"  只打印15个汉字
					if (failDeatilList[i].equals("HandOrgName")) {
						try{
							rowInfo+=ACEPackUtil.leftStrFormat(failDeatilListFormat[i], ((String)temp.get(failDeatilList[i])).substring(0,(Integer.parseInt(failDeatilListFormat[i])/2)), " ");
						}catch(Exception e){
							rowInfo+=ACEPackUtil.leftStrFormat(failDeatilListFormat[i], (String)temp.get(failDeatilList[i]), " ");
						}
					}else if(failDeatilList[i].equals("msg_data")){
						String checkStatus=(String)temp.get("checkStatus");
						if (checkStatus.equals("2")||checkStatus.equals("3")) {//进行过冲账的信息
							rowInfo+=ACEPackUtil.leftStrFormat(failDeatilListFormat[i], (String)temp.get("AddWord"), " ");
							continue;
						}
						String buf = ((String)temp.get(failDeatilList[i])).trim();
						try{
							buf = DBUtil.queryForString("select addword from transresult where flag='"+ buf +"'");
						}catch(Exception e){
							buf = "其它错误";
						}
						rowInfo+=ACEPackUtil.leftStrFormat(failDeatilListFormat[i], buf, " ");
					}else{
						rowInfo+=ACEPackUtil.leftStrFormat(failDeatilListFormat[i], (String)temp.get(failDeatilList[i]), " ");
					}
				}
			}
		 
		 if( flag == 2){			 
			 for(int i=0;i<mozhangDeatilList.length;i++){
				rowInfo+=ACEPackUtil.leftStrFormat(mozhangDeatitleListFormat[i], (String)temp.get(mozhangDeatilList[i]), " ");					
			 }			 			
			 rowInfo+="对账不成功";				//抹帐原因
		 }
		 
		 rowInfo+=" ";
		 rowInfo+="\n";
		 return rowInfo;
	}*/
	
	//打印报表的标题
	public void makeTitle(int flag){
		title="\n";
		if(printType.equals("0")){//成功明细
			title+=ACEPackUtil.leftStrFormat("60", "", " ")+"三峡银行TIPS对账成功明细清单"+"\n";
		}else if(printType.equals("1")){
			title+=ACEPackUtil.leftStrFormat("60", "", " ")+"三峡银行TIPS失败信息明细清单"+"\n";
		}else if(printType.equals("2")){
			title+=ACEPackUtil.leftStrFormat("35", "", " ")+"三峡银行TIPS电子缴库差错划款单"+"\n";
		}
		title+="\n";
		if(flag == 0){
			String titleTemp="机构号："+bankOrgCode;		
			title+=titleTemp+ACEPackUtil.rightStrFormat(""+(rowByteCount-titleTemp.getBytes().length-20), "打印日期："+DateTimeUtil.getDateTimeString()+"      共"+pages+"页,当前第"+ curPage+"页"," ")+"\n";
			title+="\n";				
			title += "委托日期  "+"收款行行号   "+"征收机构代码 "+"交易金额           "+"流水号   "+"付款人帐号       "+"纳税人编码      "+"记账日期  "+"处理结果 "+"纳税人名称  ";
			title+=" ";
			
		}else if(flag == 1){
			String titleTemp="机构号："+bankOrgCode;		
			title+=titleTemp+ACEPackUtil.rightStrFormat(""+(rowByteCount-titleTemp.getBytes().length-20), "打印日期："+DateTimeUtil.getDateTimeString()+"      共"+pages+"页,当前第"+ curPage+"页"," ")+"\n";
			title+="\n";
			title+="记账日期  "+"流水号    "+"付款人帐号        "+"交易金额        "+"纳税人编码           "
						+"纳税人名称                            "+"征收机构代码   "+"处理结果";
		}else if(flag == 2){
			String titleTemp="机构号："+bankOrgCode;	
			title+=titleTemp+ "    打印日期："+DateTimeUtil.getDateTimeString()+"      共"+pages+"页,当前第"+ curPage+"页"+"\n\n";
			for (int i=0;i<mozhangDeatitleListFormat.length;i++ ){
				title+=ACEPackUtil.leftStrFormat(mozhangDeatitleListFormat[i],mozhangTitleList[i]," ");
			}
			
		}
		title+="\n";
	}
		
	//yangyuanxu add 
	public int getStatus(String BranchNo) throws SQLException{
		int len = 0;
		logger.info("机构码是：："+BranchNo);
		len=DBUtil.queryForInt("select count(*) from bank_relation where bankorgcode='"+BranchNo+"'");	
		logger.info("len is "+len );	
		
		if(len > 0)
			return 1;
		else
			return 0;
		
	}
	//yangyuanxu add 
	public String getPayeeBankNo(String BranchNo) throws SQLException{
		String PayeeBankNo = "";
		PayeeBankNo=DBUtil.queryForString("select PayeeBankNo from bank_relation where bankorgcode='"+BranchNo+"'");	
		PayeeBankNo=PayeeBankNo.trim();
		return PayeeBankNo;
	}	
	
	public void ftpUpload(String filename) throws Exception{
		FTPUtil ftp = new FTPUtil();
		ftp.setServer(conf.getProperty("SXBankFtp", "FtpServer"));
		ftp.setPort(Integer.parseInt(conf.getProperty("SXBankFtp", "FtpPort")));
		ftp.setPath(conf.getProperty("SXBankFtp", "FtpPath"));
		ftp.setUser(conf.getProperty("SXBankFtp", "FtpUser"));
		ftp.setPassword(conf.getProperty("SXBankFtp", "FtpPassword"));
		ftp.setLocalpath(conf.getProperty("SXBankFtp", "TempFilePath"));
		ftp.upload(filename);
	}
	/*
	public static void main(String[] args) throws Exception{
		
		PrintReport test =new PrintReport();	
		Message temp=null;
		test.execute(temp);
		System.out.println("!!!!!!!!!!");
	}
*/
}
