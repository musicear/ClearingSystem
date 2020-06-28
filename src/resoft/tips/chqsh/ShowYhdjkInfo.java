package resoft.tips.chqsh;


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
 * <p>返回柜面银行端缴款信息</p>
 * @Author: liwei
 * @Date: 2008-06-07
 * @Time: 18:06:06
 */

public class ShowYhdjkInfo implements Action{
	
	private static final Log logger = LogFactory.getLog(ShowYhdjkInfo.class);
	private static Configuration conf = Configuration.getInstance();
	
	SendMsgToBankSystem send = new SendMsgToBankSystem();
	private String payOpBkNameSql="select brtbrnam from brt where brtbr=";
	
	String fileName = "",rcvMsg="";
    String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	
	String bankOrgCode="",inputTeller="",payOpBkCode="",payOpBkName="",payBkName="";
	String printType="",taxOrgCode="",traNo="",entrustDate="";
	String taxOrgName="";
	String sqlWhere="",tempFileData="";
	String tradeStatus="";
	
	public int execute(Message msg) throws Exception {
		
		bankOrgCode=msg.getString("BranchNo");			//机构代码
		inputTeller=msg.getString("UserId");			//柜员号
		//打印柜员
		TaxPiaoInfo.printTeller=inputTeller;
				
		taxOrgCode=msg.getString("TaxOrgCode");			//征收机关
		entrustDate=msg.getString("EntrustDate");		//委托日期
		traNo=msg.getString("TraNo");					//流水号
		
		fileName="TIPS"+bankOrgCode+inputTeller+"000";		
		tradeStatus="000";
						
		//征收机关
		if(taxOrgCode!=null && !taxOrgCode.equals("")){			
			taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and taxOrgCode='"+taxOrgCode+"' ");
			tradeStatus="000";
		}
			
		
		//取开户行名称
		payOpBkNameSql+=bankOrgCode;
		rcvMsg = send.sendMsg(payOpBkNameSql, "8802");
		if(!rcvMsg.equals("") && rcvMsg != null){
			int i = rcvMsg.indexOf(10) + 1;
			payOpBkName = rcvMsg.substring(i).trim();
			logger.info("payOpBkName is:"+payOpBkName);			
		}else{
			msg.set("ReturnResult", "N");
			msg.set("AddWord", "未能取到付款开户行名称！");
			tradeStatus = "222";
		}
				
		if(tradeStatus.equals("000")){			
			//生成文件					
			tradeStatus="000";
			try{
				msg.set("FileName", fileName);
				String piaoCount=makeTaxPiaoDeatil();
				msg.set("PiaoCount", piaoCount);
				if (piaoCount.equals("0")){//没有符合条件的记录
					tradeStatus="040";
					msg.set("AddWord", "没有符合条件的记录");
				}
			}catch(Exception e){
				tradeStatus="010";
				msg.set("AddWord", "生成税票打印文件出现异常");
				logger.info("生成税票打印文件出现异常：");
				e.printStackTrace();				
			}
		}else if(!tradeStatus.equals("111")){
			tradeStatus="020";
			msg.set("AddWord", "未能取到征收机关或付款开户行行号");
		}
		msg.set("VCResult", tradeStatus);
		
		if (tradeStatus.equals("000")) {//成功
			msg.set("ReturnResult", "Y");	
			msg.set("ReturnFileName", fileName);
    	}else {
    		msg.set("ReturnResult", "N");
    	}    	    	
		return SUCCESS;
	}
	
	//生成查询文件
	public String makeTaxPiaoDeatil() throws Exception {		
        File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);              
		
		//银行端缴款
		String yhdjkSql="select * from DeclareInfo a where a.entrustDate='"+entrustDate+"' and a.taxOrgCode='"+taxOrgCode+"'  and traNo='"+traNo+"' ";
		logger.info("银行端缴款："+yhdjkSql);
		List yhdjkList=QueryUtil.queryRowSet(yhdjkSql);
		logger.info("################   yhdjkList  = " + yhdjkList.size());		 	
		
		//处理银行端缴款
		for(int i=0;i<yhdjkList.size();i++){
			Map row=(Map)yhdjkList.get(i);
			row.put("TaxVodType", "3");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);
			
			if(i%2 == 1){
				writer.write(piaoInfo.initTaxPiaoInfo(row));
				writer.write("\f\n");
			}else{
				writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n\n\n\n\n");
			}
		
            piaoInfo=null;            
            writer.flush();           
		}		
		writer.close();
		
		logger.info("消息存放于:" + f.getAbsolutePath());					
		return ""+(yhdjkList.size());
	}
	
}
