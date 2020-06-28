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
	 * ����Э���ѯ���������Ŀ��Բ�ѯȫ���Ϸ�Э�飬�����ڵ���������ѯ���ڵ�¼���Э��
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
			msg.set("addword", "û�в�ѯ����Ӧ�ļ�¼");
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
			logger.info("�����к�̨����ʧ�ܻ���д���ļ�������");
			return "N";
		}
		return "Y";
	}
}
