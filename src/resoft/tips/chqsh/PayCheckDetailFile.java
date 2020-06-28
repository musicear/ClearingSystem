package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;

import resoft.basLink.Configuration;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class PayCheckDetailFile implements Action{

	private static Configuration conf = Configuration.getInstance();
	private String fileName = "";
	private String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	private String CheckDate = "";
	private String ReturnResult = "";
	Calendar date = Calendar.getInstance();
	
	public int execute(Message msg) throws Exception {
		// TODO Auto-generated method stub
		
		fileName = "DZ" + date.get(Calendar.YEAR) + date.get(Calendar.MONTH) + date.get(Calendar.DAY_OF_MONTH)
					+ date.get(Calendar.MINUTE) + date.get(Calendar.SECOND) + CheckDate ;
		
		ReturnResult = makeProveinfoDeatil(CheckDate);
		msg.set("ReturnFileName", fileName);
		msg.set("ReturnResult", ReturnResult);		    	    	
		return SUCCESS;
	}
	/**
	 * ����Message�����д��ݹ����Ĳ�ѯ�������������ݿ⣬����������浽�ļ��С�
	 * @param CheckDate		����������
	 * @return				�����ء�Y��
	 * @throws Exception	���׳�IO�쳣
	 */
	public String makeProveinfoDeatil(String CheckDate) throws Exception
	{
		File f = new File(tmpPath, fileName);
		Writer writer = new FileWriter(f);
		PayCheckDetailMessage message = new PayCheckDetailMessage();
		writer.write(message.initPayCheckDetailInfo(CheckDate) + "\n");
		writer.flush();
		writer.close();
		return "Y";
	}
}
