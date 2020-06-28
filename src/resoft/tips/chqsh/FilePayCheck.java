package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.util.FTPUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * 对账结果查询

 * 
 */
public class FilePayCheck implements Action{

	/**
	 * 根据Tips工作日期来查询PayCheck表，将对账信息发送到柜面	
	 */
	private static final Log logger = LogFactory.getLog(FilePayCheck.class);
	private static Configuration conf = Configuration.getInstance();
	private String fileName = "";
	private String tmpPath = conf.getProperty("SXBankFtp", "TempFilePath");
	private String BranchNo = "";
	private String WorkDate = "";
	private String PreWorkDate = "";
	private String PayeeBankNo = "";
	Calendar date = Calendar.getInstance();
	
	public int execute(Message msg) throws SQLException {		
		
		String ReturnResult="";
		msg.set("AddWord", "");
		msg.set("ReturnResult", "");	
		
		WorkDate = msg.getString("CheckDate").trim();
		BranchNo = msg.getString("BranchNo").trim();
		PayeeBankNo = msg.getString("PayeeBankNo").trim();
		if( WorkDate.trim().equals("") ){
			try{
				//默认查询当前TIPS工作日工作日的对账情况
				WorkDate = DBUtil.queryForString("select workdate from sysstatus");				
			}catch(Exception e){
				e.printStackTrace();
				logger.info("查询sysstatus异常！！");
				msg.set("AddWord", "操作数据库出错");
				msg.set("ReturnResult", "N");		    	    	
				return FAIL;
			}
		}
		
		fileName = "DZ" + System.currentTimeMillis() + BranchNo;
		if(WorkDate.length() > 3){
			WorkDate = WorkDate.trim();
		}
		
		if(getPayeeBankNo(BranchNo).equals(PayeeBankNo)){
			if(PayeeBankNo == null ){
				msg.set("AddWord", "没填写付款行行号！");
				msg.set("ReturnResult", "N");		    	    	
				return FAIL;
			}
		}
		if(getStatus(BranchNo)==1){
			if(getBankStatus(PayeeBankNo)==0){
				msg.set("AddWord", "收款行行号错误！");
				msg.set("ReturnResult", "N");		    	    	
				return FAIL;
			}
		}
		//
		if(!BranchNo.equals("0001") && getStatus(BranchNo)==1 && !getPayeeBankNo(BranchNo).equals(PayeeBankNo)){
			msg.set("AddWord", "查询不到相关记录，请核对录入信息！");
			msg.set("ReturnResult", "N");		    	    	
			return FAIL;
		}
		
		
		//检索该TIPS工作日内有没有对账信息
		String payCheckSQL="select count(*) from paycheck where chkDate='"+WorkDate+"' ";
		try{
			int payCheckCount=DBUtil.queryForInt(payCheckSQL);
			if(payCheckCount==0) {
				msg.set("AddWord", "没有该TIPS工作日内的对账信息");
				msg.set("ReturnResult", "N");		
				return FAIL;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//组装对账信息
		msg.set("ReturnFileName", fileName);
			    	
		ReturnResult = makePayCheckDeatil(WorkDate,PreWorkDate,BranchNo,PayeeBankNo);
		msg.set("ReturnResult", ReturnResult);	
		if(ReturnResult.equals("N")){
			msg.set("AddWord", "与主机通信异常！！");
			msg.set("ReturnResult", ReturnResult);		    	    	
			return FAIL;
		}
		if(ReturnResult.equals("F"))
		{
			msg.set("AddWord", "没有查询到相关记录！！");
			msg.set("ReturnResult", ReturnResult);		    	    	
			return SUCCESS;
		}   	
		return SUCCESS;
	}
	
	/**
	 * 生成对账明细
	 * @param WorkDate			：Tips工作日期
	 * @param BeforeWorkDate	：上一工作日期
	 * @param BranchNo			：机构号
	 * @return					：对账明细信息
	 */
	public String makePayCheckDeatil(String WorkDate,String PreWorkDate,String BranchNo,String PayeeBankNo){
		
		File f = new File(tmpPath, fileName);
		try{
			Writer writer = new FileWriter(f);
			FilePayCheckMessage message = new FilePayCheckMessage();
			String returnmsg="";
			if(BranchNo.equals("0001") || getPayeeBankNo(BranchNo).equals(PayeeBankNo)){	//清算中心
				returnmsg=message.initPayCheck(WorkDate,PreWorkDate,BranchNo,PayeeBankNo);
				writer.write( returnmsg+ "\n");
				logger.info(returnmsg);
				
			}else{							//分网点统计
				returnmsg=message.initPayCheckDetail(WorkDate,PreWorkDate,BranchNo);
				writer.write( returnmsg+ "\n");
				logger.info(returnmsg);
			}
			writer.flush();
			writer.close();
			if(returnmsg.contains("未能查询到相关记录"))
			{
				return "F";
				
			}
			this.ftpUpload(fileName);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("写文件失败或者未从银行核心系统取得数据！！");
			return "N";
		}
		return "Y";
	}
	//yangyuanxu add 
	public int getStatus(String BranchNo) throws SQLException{
		int len = 0;
		len=DBUtil.queryForInt("select count(*) from bank_relation where bankorgcode='"+BranchNo+"'");	
		if(len > 0)
			return 1;
		else
			return 0;
		
	}
	//yangyuanxu add 
	public int getBankStatus(String PayeeBankNo) throws SQLException{
		int len = 0;
		len=DBUtil.queryForInt("select count(*) from bank_relation where PayeeBankNo='"+PayeeBankNo+"'");	
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
	
	public void ftpUpload(String filename) throws IOException{
		FTPUtil ftp = new FTPUtil();
		ftp.setServer(conf.getProperty("SXBankFtp", "FtpServer"));
		ftp.setPort(Integer.parseInt(conf.getProperty("SXBankFtp", "FtpPort")));
		ftp.setPath(conf.getProperty("SXBankFtp", "FtpPath"));
		ftp.setUser(conf.getProperty("SXBankFtp", "FtpUser"));
		ftp.setPassword(conf.getProperty("SXBankFtp", "FtpPassword"));
		ftp.setLocalpath(conf.getProperty("SXBankFtp", "TempFilePath"));
		try {
			ftp.upload(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
