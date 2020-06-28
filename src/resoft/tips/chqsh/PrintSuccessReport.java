package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

//import resoft.basLink.Configuration;
import resoft.tips.chqxh.ACEPackUtil;
//import resoft.tips.util.CurrencyUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 *<p>宽行打印成功对账报表 行：(66+8*2=82*2=164);列：(27*2+6*2=54+12=66)</p>
 * Author: liwei
 * Date: 2007-11-05
 * Time: 10:26:06
 */
public class PrintSuccessReport implements Action{
	private static final Log logger = LogFactory.getLog(PrintSuccessReport.class);
//	private static Configuration conf = Configuration.getInstance();
	
	String tipsDate="20071113";
	String sql="";
	String fileName = "",bankOrgCode="",inputTeller="",title="",rowInfo="";
	int pages=0,pageCount=66-10,curPage=1,curRowIndex=1,rowByteCount=164;
	int queryRowsCount=0;
    //String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");   
	String tmpPath = "C:\\Documents and Settings\\liwei\\桌面\\temp";
    //String[] titleList={"委托日期","交易流水号","收款行行号","收款单位代码","收款人帐号","收款人名称","付款人行号","付款人开户行行号","付款人帐号","缴税单位名称","交易金额","记账日期","处理结果"};
	String[] titleList={"委托日期","收款行行号","交易金额","流水号","付款人开户行行号","付款人帐号","纳税人编码","缴税单位名称","记账日期","处理结果"};
	String[] deatilList={"EntrustDate","PayeeBankNo","TraAmt","TraNo","PayOpBkCode","PayAcct","TaxPayCode","HandOrgName","BankTraDate","AddWord"};	
	String[] deatilListFormat={"8","16","18","8","16","20","24","30","12","10"};
	
	//String[] titleList={"委托日期","收款行行号","收款人帐号","收款单位代码","收款人名称","交易金额","流水号","付款人开户行行号","付款人帐号","纳税人编码","缴税单位名称","记账日期","处理结果"};
	//String[] deatilList={"EntrustDate","PayeeBankNo","PayeeAcct","PayeeOrgCode","PayeeName","TraAmt","TraNo","PayOpBkCode","PayAcct","TaxPayCode","HandOrgName","BankTraDate","AddWord"};	
	//String[] deatilListFormat={"8","16","20","24","60","18","8","16","20","24","60","8","20"};
	
	public int execute(Message msg)throws Exception{
		bankOrgCode="2222222";
		inputTeller="3333333";
		//文件名
		fileName="TIPS"+bankOrgCode+inputTeller+"000.txt";
		//生成报表
		makeReport();
		
		return SUCCESS;
	}
	//生成报表
	public String makeReport()throws Exception {
		File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);
		//查询实时
		sql="select * from realTimePayMent a where a.checkStatus='1' and a.result='90000' and a.chkDate='"+tipsDate+"'  order by traNo ";
		List realList=QueryUtil.queryRowSet(sql);		
		
		//查询批量
		sql="select * from batchPackDetail a,BatchPackage b where a.taxOrgCode=b.taxOrgCode and a.packNo=b.packNo and a.entrustDate=b.entrustDate and a.checkStatus='1' and a.result='90000' and a.chkDate='"+tipsDate+"' order by a.packNo,a.traNo ";
		List batchList=QueryUtil.queryRowSet(sql);
		//要打印的明细行数，包括合计
		queryRowsCount=(realList.size()+batchList.size())*2+1;
		
		//总页数
		pages=(queryRowsCount)/pageCount+((queryRowsCount)%pageCount>0?1:0);		
		//实时
		for(int i=0;i<realList.size();i++){
			printPageInfo(writer,(Map)realList.get(i));
		}
		//批量
		for(int i=0;i<batchList.size();i++){
			printPageInfo(writer,(Map)batchList.get(i));
		}
								
		writer.close();
		
		logger.info("消息存放于:" + f.getAbsolutePath());					
		return ""+(realList.size()+batchList.size());
	}
	
	//打印页面信息
	public void printPageInfo(Writer wriTmp,Map temp) throws Exception{
		if (curRowIndex==1) {
			//设置标题
			makeTitle();	
			wriTmp.write(title);
			wriTmp.flush();
		}		
		wriTmp.write(makeRowFormat(temp));
		wriTmp.flush();
		curRowIndex+=2;
		if ((curRowIndex-1)==pageCount) {//换页
			curRowIndex=1;
			curPage++;
			wriTmp.write("\n\n\n\n");
			wriTmp.flush();
		}
	}
	//设置行格式
	public String makeRowFormat(Map temp){
		rowInfo="";
		for(int i=0;i<deatilList.length;i++){
			/**
			if (deatilList[i].equals("TraNo")) {
				rowInfo+="\n";
			}
			*/
			//"收款人名称" 和 "缴款单位名称"  只打印30个汉字
			if (deatilList[i].equals("HandOrgName")||deatilList[i].equals("PayeeName")) {
				try{
					rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], ((String)temp.get(deatilList[i])).substring(0,(Integer.parseInt(deatilListFormat[i])/2)), " ");
				}catch(Exception e){
					rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], (String)temp.get(deatilList[i]), " ");
				}
			}else {
				rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], (String)temp.get(deatilList[i]), " ");
			}
			rowInfo+=" ";
		}
		rowInfo+="\n";
		return rowInfo;
	}
	//打印没有的标题
	public void makeTitle(){
		title="\n";
		title+=ACEPackUtil.leftStrFormat("80", "", " ")+"三峡银行TIPS明细清单"+"\n";
		title+=ACEPackUtil.rightStrFormat(""+rowByteCount, "共"+pages+",当前第"+ curPage+"页"," ")+"\n";
		title+="\n";
		for (int i=0;i<titleList.length;i++) {
			/**
			if (deatilList[i].equals("TraAmt")) {
				//TraAmt = CurrencyUtil.getCurrencyFormat(traAmt);
				title+=ACEPackUtil.leftStrFormat(deatilListFormat[i], CurrencyUtil.getCurrencyFormat(titleList[i]), " ");
				break;
			}
			*/
			title+=ACEPackUtil.leftStrFormat(deatilListFormat[i], titleList[i], " ");
			title+=" ";
		}
		title+="\r\n";
	}
	public static void main(String[] args) throws Exception{
		
		PrintSuccessReport test =new PrintSuccessReport();	
		Message temp=null;
		test.execute(temp);
		
		/*
		int fuck=0;
		for(int i=0;i<PrintSuccessReport.deatilListFormat.length;i++){
			fuck+=Integer.parseInt(PrintSuccessReport.deatilListFormat[i]);
		}
		System.out.println(fuck);
		*/
		
		//System.out.println("1".substring(0,30));
	}
}
