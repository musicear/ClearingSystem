package resoft.tips.bankImpl.qhdyh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.action.TransferAcctSX;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��������ļ�</p>
 * Author: zhuchangwu
 * Date: 2007-8-31
 * Time: 9:37:04
 */
public class QequestBankChkInfo implements Action {
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	private Socket sender=null;  
	String result="",addWord="";
	private String MsgRecordData="fail";
	private static final Log logger = LogFactory.getLog(TransferAcctSX.class);
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
    public int execute(Message msg) throws Exception {
    	logger.info("in there");
    	//List changeDateRow=QueryUtil.queryRowSet("select workDate from ChangeDate where id=1");
    	//Map changeDateMap=(Map)changeDateRow.get(0);
    	String changeDate=msg.getString("ChkDate");
    	//yangyuanxu add
    	String payeeBankNo = msg.getString("PayeeBankNo");
    	//String payeeAcct = getPayAcct(payeeBankNo);
    
    	SzsmCheckPackager packager = new SzsmCheckPackager();
    	packager.CreateBodyData(changeDate, changeDate, changeDate);
    	sendMsg = packager.MakeUpCheckPackager();//��ȡ���͵��ֶ�
    	String sendori="TI"+changeDate+".txt";				//�ļ�����
    	logger.info("�ļ�����Ϊ"+sendori); 
    	String sendType="C";
    	byte[]sendbt=sendMsg.getBytes();		//��ȡ���͵�����
    	
    	try
    	{	
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//���ͷ���ʶ
			out.write(sendbt);
			
			logger.info("���Ͷ�������:["+sendMsg+"]");
			out.flush();
			byte[] recPack=new byte[100];			
			in.read(recPack,0,100);
			rcvMsg = new String(recPack);
			logger.info("���ն���Ӧ��["+rcvMsg+"]");
			if(!rcvMsg.equals("NULL"))
			{
				packager.revXml=rcvMsg;
				
			}
    	}
    	catch(Exception e)
    	{					
    			e.printStackTrace();			
    			rcvMsg = "NULL";
    	}
		finally
		{
			if (sender!=null)
			{
				try
				{
					sender.close();
				}
				catch(Exception e){
					sender=null;
				}
			}
		}
		if(rcvMsg.equals("NULL")){//�����ͨѶ����
    		result="94999";		
    		addWord="������ͨѶ�쳣";
	    }
		else 
	    {	
			MsgRecordData=packager.GetParm("sys_header", "RET_CODE");
			result=MsgRecordData;
			logger.info("����� �ǣ�"+MsgRecordData);
			if(MsgRecordData.equals("000000")) {	//���׳ɹ�	 
	            addWord="���׳ɹ�";	
	            
	            String path=conf.getProperty("SXBankFtp", "3111TempFilePath")+"/"+sendori;
	            msg.set("BankChkFileName",path);
	        }
			else{
				addWord=packager.GetParm("sys_header", "RET_MSG"); 
				msg.set("result", "94999");
				msg.set("addword", "���״���ʧ��");
				return FAIL;
	        }
	    }
    	
		return SUCCESS;
    }
  //yangyuanxu add
    private String getPayAcct(String payeeBankNO) throws SQLException {
        String payAcct="";
        payAcct=DBUtil.queryForString("select payeeacct from payee_relation where payeeBankNo='"+payeeBankNO+"'");	
    	return payAcct;
    }
    
    
    public String getYwbsh(String payeeBankNo) throws SQLException{
		String ywbsh = "";
		ywbsh=DBUtil.queryForString("select ywbsh from bank_relation where PayeeBankNo='"+payeeBankNo+"'");	
		ywbsh=ywbsh.trim();
		return ywbsh;
	}
}


