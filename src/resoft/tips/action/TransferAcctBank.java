package resoft.tips.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.bankImpl.sxbank.TuxedoPackager;
import resoft.tips.util.ChangeStringToBytes;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;

public class TransferAcctBank extends AbstractSyncAction{
	
	private static final Log logger = LogFactory.getLog(TransferAcctBank.class);
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	private String MsgRecordData="0000";								//结果描述
	String TR_CODE = "fail";											//交易码
	String JRN_NO = "fail";												//交易日志号
	String VCH_NO = "fail";												//传票号
	//String MSG_DATA = "0000";											
	String IADAC_DAT = DateTimeUtil.getDateString();					//tuxedo返回的银行会计日
	String result="",addWord="";
	private Socket sender=null;
	Configuration conf = Configuration.getInstance();
	private String nowBankTraNo="";
	private String nowWorkData="";
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	
	public synchronized int execute(Message msg) throws SQLException{ 
		TuxedoPackager packager = new TuxedoPackager();
		String PayAcct = (String)msg.get("//CFX/MSG/Payment3001/PayAcct");
    	String TraAmt = (String)msg.get("//CFX/MSG/Payment3001/TraAmt");
    	String WorkDate = (String)msg.getString("//CFX/HEAD/WorkDate");
    	String TaxTypeName = "";
    	String payeeBankNo = (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo");
    	int taxTypeNum = Integer.parseInt(msg.getString("//CFX/MSG/Payment3001/TaxTypeNum"));
    	String protocolNo = (String)msg.getString("//CFX/MSG/RealHead3001/TraNo");
    	try{
	    	//处理批量
    		TaxTypeName=(String)msg.get("BATCHTAXTYPENAME")==null?"":(String)msg.get("BATCHTAXTYPENAME");
    		if (TaxTypeName.equals("")) {
    			//处理实时
	    		if(taxTypeNum == 1){
		    		TaxTypeName = (String)msg.get("//CFX/MSG/Payment3001/TaxTypeList3001[1]/TaxTypeName" );
		    	}else{
		    		TaxTypeName = (String)msg.get("//CFX/MSG/Payment3001/TaxTypeList3001[1]/TaxTypeName" ) + "等";			    		
		    	}
    		}
    	}catch(Exception e){
    		TaxTypeName="代扣税";
    	}
    	    	
    	sendMsg = packager.getPackager(protocolNo,PayAcct,TraAmt ,payeeBankNo,WorkDate,1);
    	ChangeStringToBytes strTobytes=new ChangeStringToBytes();
    	String sendtype="R";
    	byte[]sendbt=strTobytes.GetBytes(sendMsg,sendtype);
    	//byte[]sendbt=GetBytes(sendMsg);

    	try{	
    		
			
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//发送方标识
			//out.write(sendMsg.getBytes());
			out.write(sendbt);
			logger.info("发送记账TUXEDO请求:["+sendMsg+"]");
			//for(int i=0;i<sendbt.length;i++)
			//{
				//logger.info("发送数组中数据第"+i+"项为:["+sendbt[i]+"]");
				
			//}
			out.flush();
			byte[] recPack=new byte[85];			
			in.read(recPack,0,85);
			rcvMsg = new String(recPack);
			logger.info("接收记账TUXEDO应答：["+rcvMsg+"]");
			if(!rcvMsg.equals("NULL"))
			{
				byte[]msgdatabyte=new byte[7];
				for(int i=0;i<msgdatabyte.length;i++)
				{
					msgdatabyte[i]=recPack[i+57];
	    		
				}
				MsgRecordData=new String(msgdatabyte);
				
				byte[]nowbanktrabyte=new byte[12];
				for(int i=0;i<nowbanktrabyte.length;i++)
				{
					nowbanktrabyte[i]=recPack[i+43];
	    		
				}
				
				nowBankTraNo=new String(nowbanktrabyte);
				
				logger.info("流水号 是："+nowBankTraNo);
				
				byte[] nowworkdatabyte=new byte[8];
				for(int i=0;i<nowworkdatabyte.length;i++)
				{
					nowworkdatabyte[i]=recPack[i+31];
	    		
				}
				nowWorkData=new String(nowworkdatabyte);
				logger.info("交易日期 是："+nowWorkData);
			}
		}catch(Exception e){					
			e.printStackTrace();			
			rcvMsg = "NULL";
		}finally{
			if (sender!=null) {
				try {
					sender.close();
				}catch(Exception e){
					sender=null;
				}
			}
		}
		if(rcvMsg.equals("NULL")){//与核心通讯错误
    		result="94999";		
    		addWord="与主机通讯异常";
	    }else {						
	    	TR_CODE = rcvMsg.substring(11, 14);				//交易码
	    	IADAC_DAT = rcvMsg.substring(31, 39);					//会计日
	    	JRN_NO = rcvMsg.substring(54, 61);						//日志号
	    	VCH_NO = rcvMsg.substring(69, 77);						//传票号
	    	
	    	//MSG_DATA = rcvMsg.substring(57, 64);					//处理结果描述
	    	logger.info("结果码 是："+MsgRecordData);
	    	logger.info("Tuxedo： " + TR_CODE + "|" + IADAC_DAT + "|" + JRN_NO + "|" + VCH_NO + "|" + MsgRecordData);
	        if(MsgRecordData.equals("AAAAAAA")) {	//交易成功	            
	            result="90000";
	            addWord="交易成功";		            
	        } else{					//交易失败			        	
	        	if(MsgRecordData.equals("1840") || MsgRecordData.equals("2286") )//账户冻结
	        		result="24004";
	        	if(MsgRecordData.equals("1627"))//余额不足
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
	        		addWord = "其它错误！";
	        	}
	        }
	    }
		
		if(rcvMsg.equals("NULL")){
			msg.set("FLAG", "N");
		}else{
			if (MsgRecordData.equals("9004")) {
				msg.set("FLAG", "Y");
			} else {					
				msg.set("FLAG", "O");
			}
		}		
		msg.set("AddWord", addWord);
    	/*
    	msg.set("JRN_NO", JRN_NO);
    	msg.set("VCH_NO", VCH_NO);
    	msg.set("MSG_DATA", MsgRecordData);
    	msg.set("IADAC_DAT",nowWorkData);
    	msg.set("Result", result);
    	msg.set("AddWord", addWord);    	
    	msg.set("bankTraNo",nowBankTraNo);
        msg.set("bankTraDate",nowWorkData);
        msg.set("bankTraTime",DateTimeUtil.getDateTimeString().substring(11));    	    	
    	msg.set("TipsWorkDate", (String)msg.getString("//CFX/HEAD/WorkDate"));
    	//yangyuanxu add
    	msg.set("payeeBankNo", (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo"));
        msg.set(getResultNodePath(),result);
        msg.set(getAddWordNodePath(),addWord);
        */
    	return SUCCESS;
	}
	
	//测试用
	public byte[] GetBytes(String sendMsg)throws SQLException
	{
		
		int len = sendMsg.getBytes().length+2;
    	String lenStr = Integer.toString(len);
    	
    	byte[] bt=new byte[4];
    	
    	String str=Integer.toHexString(len).toString();
    	for(int i=0;i<4;i++){
            bt[i]=(byte)(len>>>(24-i*8));
		}

    	
    	for(int i=lenStr.length();i<10;i++)
    	{
    		lenStr = lenStr + " ";
    	}
    	logger.info("前十位长度为："+lenStr.trim());
    	
    	byte[] sendbt=new byte[len+lenStr.getBytes().length];
    	for(int i=0;i<sendbt.length;i++)
    	{
    		if(i<10)
    		{
    			sendbt[i]=lenStr.getBytes()[i];
    			
    		}
    		else if(i<12)
    		{
    			sendbt[10]=bt[0];
    			sendbt[11]=bt[1];
    		}
    		else
    		{
    			sendbt[i]=sendMsg.getBytes()[i-12];	
    		}
    		
    	}
    	logger.info("数组长度为："+sendbt.length);
    		return sendbt;
		
		
	}
}

