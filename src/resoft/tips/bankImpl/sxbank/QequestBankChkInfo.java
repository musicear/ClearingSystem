package resoft.tips.bankImpl.sxbank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.action.TransferAcctSX;
import resoft.tips.bankImpl.qhdyh.SzsmCheckPackager;
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
	private String rcvChargeMsg;
	byte[] recPack;
	private String sendori;
    public int execute(Message msg) throws Exception {
    	//List changeDateRow=QueryUtil.queryRowSet("select workDate from ChangeDate where id=1");
    	//Map changeDateMap=(Map)changeDateRow.get(0);
    	 logger.info("      in here               ");
    	String changeDate=msg.getString("ChkDate"); 
    	 
    	String workdate=msg.getString("//CFX/HEAD/WorkDate");
    	 
    	//yangyuanxu add
    	String payeeBankNo = msg.getString("PayeeBankNo"); 
    	sendori="TITIPS"+changeDate+".txt";	 
    	SzsmCheckPackager pack=new SzsmCheckPackager();
        pack.CreateSystemHeadData(workdate); //��Ҫ�ĳ���ȷ��
        pack.CreateBodyData(changeDate,changeDate,changeDate);
        String sendMsg = pack.MakeUpCheckPackager();//��Ҫʵ�� 
        byte[]sendbt=sendMsg.getBytes();
        sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
		sender.setSoTimeout(6*1000);		
		try
		{
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
	    	
			out.write(sendbt); 
			logger.info("real  send to shenmacheck   "+sendMsg);
			out.flush();
			byte[] packageLengthbyte=new byte[10];
			in.read(packageLengthbyte,0,10);
			String lengthStr=new String(packageLengthbyte);
			int packetlength=Integer.parseInt(lengthStr);
			recPack=new byte[packetlength];	
			in.read(recPack,0,packetlength); 
			 
		}
		catch(Exception e)
		{
			logger.info("yichang               ");
			result="94999";
	         addWord="�����ͨ���쳣";
	         return FAIL;
		}
		rcvChargeMsg = new String(recPack);
		
		pack.revXml=rcvChargeMsg.trim();
		logger.info("rece sm checkdata"+rcvChargeMsg);
    	
    	
		MsgRecordData=pack.GetParm("sys_header", "RET_CODE");
		if(MsgRecordData.equals("000000"))
		{
			 result="90000";
	         addWord="���׳ɹ�";	
	            String path=conf.getProperty("SXBankFtp", "3111TempFilePath")+"/"+sendori; 
	            logger.info("path         "+path);
	            msg.set("BankChkFileName",path);
		}
		else
		{
			result="24004";
			addWord=pack.GetParm("sys_header", "RET_MSG");
		}
    	
    	
    	
    	
    	
    	
    /*	//String payeeAcct = getPayAcct(payeeBankNo);
    	TuxedoPackager packager = new TuxedoPackager();
    	sendMsg = packager.getQequestCheckInfoStr(payeeBankNo,changeDate,3);//��ȡ���͵��ֶ�
    	String sendori=getYwbsh(payeeBankNo)+changeDate+".txt";				//�ļ�����
    	logger.info("�ļ�����Ϊ"+sendori);
    	ChangeStringToBytes strTobytes=new ChangeStringToBytes();
    	String sendType="C";
    	byte[]sendbt=strTobytes.GetBytes(sendMsg,sendType,sendori);		//��ȡ���͵�����
    	
    	//���ֶ��˱���
    	
    	//Message newMsg = new DefaultMessage();
        //newMsg.set("��Ա��", "666666");
        //newMsg.set("�����", "0004");
        //newMsg.set("ҵ������",changeDate );//msg.getString("ChkDate")changeDate"2007-06-19"
        //newMsg.set("������ˮ��", "00000000");
        //newMsg.set("������", "030619");
        //yangyuanxu add �鲢�˻�
        //newMsg.set("LI_MACCT",payeeAcct);
        //newMsg.set("LI_DFRQ", msg.getString("ChkDate"));//msg.getString("ChkDate")20070101
        //newMsg.set("LI_LTCD", "A15");
        //newMsg.set("LI_DFID", "");
        //MessageSender sender = MessageSenderUtil.getMessageSender();
    	try
    	{	
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//���ͷ���ʶ
			out.write(sendbt);
			
			logger.info("���Ͷ���TUXEDO����:["+sendMsg+"]");
			out.flush();
			byte[] recPack=new byte[100];			
			in.read(recPack,0,100);
			rcvMsg = new String(recPack);
			logger.info("���ն���TUXEDOӦ��["+rcvMsg+"]");
			if(!rcvMsg.equals("NULL"))
			{
				byte[]msgdatabyte=new byte[7];
				for(int i=0;i<msgdatabyte.length;i++)
				{
					msgdatabyte[i]=recPack[i+57];
	    		
				}
				MsgRecordData=new String(msgdatabyte);
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
			logger.info("����� �ǣ�"+MsgRecordData);
			if(MsgRecordData.equals("AAAAAAA")) {	//���׳ɹ�	            
	            result="90000";
	            addWord="���׳ɹ�";	
	            
	            String path=conf.getProperty("SXBankFtp", "3111TempFilePath")+"/"+sendori;
	            msg.set("BankChkFileName",path);
	        }
			else{					//����ʧ��			        	
	        	if(MsgRecordData.equals("1840") || MsgRecordData.equals("2286") )//�˻�����
	        		result="24004";
	        	if(MsgRecordData.equals("1627"))//����
	        	    result="24007";
	        	if(MsgRecordData.equals("1624"))//
	        	    result="24005";
	        	if(MsgRecordData.equals("2297") || MsgRecordData.equals("1096") )//
	        	    result="24008";
	        	try{
	        		addWord = DBUtil.queryForString("select addword from transresult where flag='" + MsgRecordData+"' ");
	        		if(addWord.length()>=15){
	        			addWord=addWord.substring(0,14);
	        		}
	        	}catch (Exception e){
	        		e.printStackTrace();
	        		addWord = "��������";
	        	}
	        }
	    }*/
    	
        /*try {
            Message returnMsg = sender.send(newMsg);
            if("000".equals(returnMsg.getString("RespCode"))) {
            	//DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            	long dateLong=System.currentTimeMillis();//df.format(new Date());
            	String fileName=String.valueOf(dateLong)+".txt";
            	String path="";//conf.getProperty("general", "tmpPath")+"\\";
                ClassLoader  loader  =  getClass().getClassLoader();
                URL urlpath=loader.getResource(""); 
                String filePath=urlpath.getFile();
                String downPath=filePath+"filedown/";//filePath.substring(0, filePath.length()-4)+"filedown/"
                System.out.println("the downPath is:..................."+downPath);
                path=downPath+fileName;//�ļ�·��//downPath.substring(1,downPath.length())+fileName
            	FileWriter fw=new FileWriter(path); 
            	BufferedWriter bw=new BufferedWriter(fw);
            	List list = (List) returnMsg.get("CheckInfoList");
            	for(int i=0;i<list.size();i++){
            		Message check= (Message) list.get(i);
            		String overFlag = check.getString("���һ����־");
            		if("0".equals(overFlag)){
            			String TraDate=check.getString("LO_TXDATE");
            			String bankTraDate=TraDate.substring(0,4)+TraDate.substring(5,7)+TraDate.substring(8,10);
            			String TraTime=" ";
            			String TransNo=check.getString("LO_PTXSEQ");            			
            			String TaxOrgCode=check.getString("LO_ORGNO");
            			String TaxPayCode=" ";
            			String DebitAcct=check.getString("LO_ACCT1");
            			String TraAmt=check.getString("LO_TXAMT");
            			String lineStr=bankTraDate+"|"+TraTime+"|"+TransNo+"|"+TaxOrgCode+"|"+TaxPayCode+"|"+DebitAcct+"|"+TraAmt;
            			bw.write(lineStr);
            			bw.newLine();//���� 
            		}
            	}
            	bw.flush();//�����ݸ������ļ� 
            	fw.close();//�ر��ļ���
                msg.set("BankChkFileName",path);
                return SUCCESS;
            } else {
                logger.error("����ʧ�ܡ�RespCode:" + returnMsg.get("RespCode") + ";RespDesc:" + returnMsg.get("RespDesc"));
                return FAIL;
            }
        } catch(MessageSendException e) {
            logger.error("��������ļ�ʧ��",e);
            return FAIL;
        }
        */
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


