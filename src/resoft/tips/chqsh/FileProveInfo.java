package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class FileProveInfo implements Action{

	/**
	 * 三方协议查询，清算中心可以查询全部合法协议，各个节点机构仅需查询本节点录入的协议
	 */
	private static Configuration conf = Configuration.getInstance();
	private static final Log logger = LogFactory.getLog(PrintTaxVod.class);
	private String fileName = "";
	private String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	private String VerifyResult = "";
	private String BranchNo = "";
	private String ReturnResult = "";
	private String inputTeller = "";
	Calendar date = Calendar.getInstance();
	
	public int execute(Message msg) {
				
		VerifyResult = msg.getString("Dflag");
		BranchNo = msg.getString("BranchNo").trim();
		inputTeller=msg.getString("UserId").trim();		
		fileName = "XYS" + System.currentTimeMillis() + date.get(Calendar.SECOND) + BranchNo + inputTeller ;
		ReturnResult = makeProveinfoDeatil(VerifyResult,BranchNo);
		if(ReturnResult.equals("Y")){
			msg.set("ReturnFileName", fileName);
		}else{
			msg.set("addword", "没有查询到相应的记录");
		}
		msg.set("ReturnResult", ReturnResult);
			    	    	
		return SUCCESS;
	}
	
	public String makeProveinfoDeatil(String VerifyResult,String BranchNo){
		
		File f = new File(tmpPath, fileName);
		try{
			Writer writer = new FileWriter(f);
			FileProveInfoMessage message = new FileProveInfoMessage();
			writer.write(message.initProveInfo(VerifyResult,BranchNo) + "\n");
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			logger.info("与银行后台交易失败或者写入文件出错！！");
			return "N";
		}
		return "Y";
	}
}
