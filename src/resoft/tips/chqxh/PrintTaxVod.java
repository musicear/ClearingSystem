package resoft.tips.chqxh;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>打印税票信息</p>
 * Author: liwei
 * Date: 2007-10-22
 * Time: 18:06:06
 */

public class PrintTaxVod implements Action{
	private static final Log logger = LogFactory.getLog(PrintTaxVod.class);
	private static Configuration conf = Configuration.getInstance();
	
	String fileName = "";
    String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	
	String bankOrgCode="",inputTeller="",payOpBkCode="",payOpBkName="",payBkCode="402653000011",payBkName="";
	String printType="",taxOrgCode="",payAcct="",taxPayCode="",startDate="",endDate="";
	String taxOrgName="";
	String sqlWhere="",tempFileData="";
	
	public int execute(Message msg) throws Exception {
		ACEPackager ace2006=(ACE2006)msg.get("ACEObj");
    	//初始化ACE交易报文体
		ace2006.makeTransBody();
		bankOrgCode=(String)ace2006.pkTHHeadList.get("BankOrgCode");	//机构代码
		inputTeller=(String)ace2006.pkTHHeadList.get("InputTeller");	//柜员号
		//打印柜员
		TaxPiaoInfo.printTeller=inputTeller;
		
		printType=(String)ace2006.pkTHBodyList.get("PrintType");		//打印类型 1：实时；2：批量；3：按照记账日期查询
		taxOrgCode=(String)ace2006.pkTHBodyList.get("TaxOrgCode");		//征收机关
		payAcct=(String)ace2006.pkTHBodyList.get("PayAcct");			//帐号
		taxPayCode=(String)ace2006.pkTHBodyList.get("TaxPayCode");		//纳税人编码
		startDate=(String)ace2006.pkTHBodyList.get("StartDate");		//起日期
		endDate=(String)ace2006.pkTHBodyList.get("EndDate");			//止日期
		
		fileName="TIPS"+bankOrgCode+inputTeller+"000";
		
		//征收机关
		if(taxOrgCode!=null && !taxOrgCode.equals("") && printType.equals("2")){
			sqlWhere+=" and a.taxOrgCode='"+taxOrgCode+"' ";
			taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and taxOrgCode='"+taxOrgCode+"' ");
			ace2006.tradeStatus="000";
		}
		if(ace2006.tradeStatus.equals("000")){
			//进行银行代码转换
			payOpBkCode=DBUtil.queryForString("select DMBENO from pmdma where DMSBNO='"+bankOrgCode+"' ");
			//查找开户行名称
			payOpBkName=DBUtil.queryForString("select ORUCUN from agora where ORUCUS='"+bankOrgCode+"' ");
			//查找付款行行号
			payBkName=DBUtil.queryForString("select GENBANKNAME from bankMng where RECKBANKNO='"+payBkCode+"' ");
			
			//成功对账划款 (与TIPS对账成功)
			sqlWhere+=" and a.result='90000' and a.checkStatus='1' ";
			//开户行行号
			//sqlWhere+=" and a.payOpBkCode='"+payOpBkCode+"' ";			
			//付款帐号
			if(payAcct!=null && !payAcct.equals("")){
				sqlWhere+=" and a.payAcct='"+payAcct+"' ";
			}
			//纳税人编码
			if (taxPayCode!=null && !taxPayCode.equals("")) {
				sqlWhere+=" and a.taxPayCode='"+taxPayCode+"' ";
			}			
			//查询日期
			sqlWhere+=" and a.bankTraDate='"+startDate+"' ";							
			
			//生成文件					
			ace2006.tradeStatus="000";
			try {
				ace2006.tradeStatus="000";
				msg.set("FileName", fileName);
				msg.set("PiaoCount", makeTaxPiaoDeatil());
			}catch(Exception e){
				ace2006.tradeStatus="010";
				msg.set("AddWord", "生成税票打印文件出现异常");
				logger.info("生成税票打印文件出现异常：");
				e.printStackTrace();				
			}
		}else {
			ace2006.tradeStatus="020";
			msg.set("AddWord", "征收机关错误");
		}
		
		msg.set("VCResult", ace2006.tradeStatus);
		
		if (ace2006.tradeStatus.equals("000")) {//成功
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("FileName")+"|"+msg.getString("PiaoCount"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	    	    	
		return SUCCESS;
	}
	
	//生成查询文件
	public String makeTaxPiaoDeatil() throws Exception {		
        File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);
        
        //查询实时
        String realTimeSql="select * from realTimePayMent a where 1=1 "+sqlWhere;        
        List realTimeList=QueryUtil.queryRowSet(realTimeSql);
        
        //查询批量
		String batchSql="select * from BatchPackDetail a where 1=1 "+sqlWhere;
		List batchList=QueryUtil.queryRowSet(batchSql);
		
		if(realTimeList.size()>0||batchList.size()>0){
			writer.write("\033@\n");
			writer.write("\033(C1L1H10L10H\n");
		}
		
		//处理实时
		for(int i=0;i<realTimeList.size();i++){
			Map row=(Map)realTimeList.get(i);
			row.put("TaxVodType", "1");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);			
            writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n"); 
            piaoInfo=null;            
            writer.flush();
            //第一次打印开始是3行，之后就空5行
            if (TaxPiaoInfo.startNullRows==3) {
            	TaxPiaoInfo.startNullRows=4;
            }
            if (TaxPiaoInfo.startNullRows==4) {
            	TaxPiaoInfo.startNullRows=5;
            }else if(TaxPiaoInfo.startNullRows==5){
            	TaxPiaoInfo.startNullRows=4;
            }
		}		
		
		//处理批量	
		for(int i=0;i<batchList.size();i++){
			Map row=(Map)batchList.get(i);
			row.put("TaxVodType", "2");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);			
            writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n"); 
            piaoInfo=null;            
            writer.flush();
            //第一次打印开始是3行，之后就空5行
            if (TaxPiaoInfo.startNullRows==3) {
            	TaxPiaoInfo.startNullRows=4;
            }
            if (TaxPiaoInfo.startNullRows==4) {
            	TaxPiaoInfo.startNullRows=5;
            }else if(TaxPiaoInfo.startNullRows==5){
            	TaxPiaoInfo.startNullRows=4;
            }
		}		
		writer.close();
		
		//修改实时扣税的打印次数
		if (realTimeList.size()>0) {
			DBUtil.executeUpdate("update realTimePayMent a set a.printTimes=a.printTimes+1 where 1=1 "+sqlWhere);
		}
		//修改批量扣税的打印次数
		if (batchList.size()>0) {
			DBUtil.executeUpdate("update BatchPackDetail a set a.printTimes=a.printTimes+1 where 1=1 "+sqlWhere);
		}
		
		logger.info("消息存放于:" + f.getAbsolutePath());					
		return ""+(realTimeList.size()+batchList.size());
	}
	
}
