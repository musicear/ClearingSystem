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
 * ���˽����ѯ

 * 
 */
public class FilePayCheck implements Action{

	/**
	 * ����Tips������������ѯPayCheck����������Ϣ���͵�����	
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
				//Ĭ�ϲ�ѯ��ǰTIPS�����չ����յĶ������
				WorkDate = DBUtil.queryForString("select workdate from sysstatus");				
			}catch(Exception e){
				e.printStackTrace();
				logger.info("��ѯsysstatus�쳣����");
				msg.set("AddWord", "�������ݿ����");
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
				msg.set("AddWord", "û��д�������кţ�");
				msg.set("ReturnResult", "N");		    	    	
				return FAIL;
			}
		}
		if(getStatus(BranchNo)==1){
			if(getBankStatus(PayeeBankNo)==0){
				msg.set("AddWord", "�տ����кŴ���");
				msg.set("ReturnResult", "N");		    	    	
				return FAIL;
			}
		}
		//
		if(!BranchNo.equals("0001") && getStatus(BranchNo)==1 && !getPayeeBankNo(BranchNo).equals(PayeeBankNo)){
			msg.set("AddWord", "��ѯ������ؼ�¼����˶�¼����Ϣ��");
			msg.set("ReturnResult", "N");		    	    	
			return FAIL;
		}
		
		
		//������TIPS����������û�ж�����Ϣ
		String payCheckSQL="select count(*) from paycheck where chkDate='"+WorkDate+"' ";
		try{
			int payCheckCount=DBUtil.queryForInt(payCheckSQL);
			if(payCheckCount==0) {
				msg.set("AddWord", "û�и�TIPS�������ڵĶ�����Ϣ");
				msg.set("ReturnResult", "N");		
				return FAIL;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//��װ������Ϣ
		msg.set("ReturnFileName", fileName);
			    	
		ReturnResult = makePayCheckDeatil(WorkDate,PreWorkDate,BranchNo,PayeeBankNo);
		msg.set("ReturnResult", ReturnResult);	
		if(ReturnResult.equals("N")){
			msg.set("AddWord", "������ͨ���쳣����");
			msg.set("ReturnResult", ReturnResult);		    	    	
			return FAIL;
		}
		if(ReturnResult.equals("F"))
		{
			msg.set("AddWord", "û�в�ѯ����ؼ�¼����");
			msg.set("ReturnResult", ReturnResult);		    	    	
			return SUCCESS;
		}   	
		return SUCCESS;
	}
	
	/**
	 * ���ɶ�����ϸ
	 * @param WorkDate			��Tips��������
	 * @param BeforeWorkDate	����һ��������
	 * @param BranchNo			��������
	 * @return					��������ϸ��Ϣ
	 */
	public String makePayCheckDeatil(String WorkDate,String PreWorkDate,String BranchNo,String PayeeBankNo){
		
		File f = new File(tmpPath, fileName);
		try{
			Writer writer = new FileWriter(f);
			FilePayCheckMessage message = new FilePayCheckMessage();
			String returnmsg="";
			if(BranchNo.equals("0001") || getPayeeBankNo(BranchNo).equals(PayeeBankNo)){	//��������
				returnmsg=message.initPayCheck(WorkDate,PreWorkDate,BranchNo,PayeeBankNo);
				writer.write( returnmsg+ "\n");
				logger.info(returnmsg);
				
			}else{							//������ͳ��
				returnmsg=message.initPayCheckDetail(WorkDate,PreWorkDate,BranchNo);
				writer.write( returnmsg+ "\n");
				logger.info(returnmsg);
			}
			writer.flush();
			writer.close();
			if(returnmsg.contains("δ�ܲ�ѯ����ؼ�¼"))
			{
				return "F";
				
			}
			this.ftpUpload(fileName);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("д�ļ�ʧ�ܻ���δ�����к���ϵͳȡ�����ݣ���");
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
