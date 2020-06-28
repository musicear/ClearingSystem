package resoft.tips.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.bankImpl.qhdyh.SzsmChargePackager;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;

public class TransferAcctSX extends AbstractSyncAction{
	
	private static final Log logger = LogFactory.getLog(TransferAcctSX.class);
	private String sendMsg="fail";
	private String rcvMsg = "fail",rcvChargeMsg="";
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
	String WorkDate="";
	private String nowWorkData="";
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	String CARDFLAG;
	String STATUS="";
	String VCResult;
	String AddWord;  
	String tra_amt;
	private String seq_no;
	private String trano="";
	private String EntrustDate="";
	private String PayeeBankNo = "";
	String taxOrgCode;
	String protocolNo;
	String payAcct;
	String taxPayName;
	String taxPayCode="";
	private String Ret_statue="F";
	private String TaxPayCode="";
	private String PayeeAcct=conf.getProperty("BankSysConfig", "PayeeAcct");
	public synchronized int execute(Message msg) throws SQLException, IOException{ 
		EntrustDate = (String)msg.getString("//CFX/MSG/RealHead3001/EntrustDate");
		taxOrgCode=(String)msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
		PayeeBankNo = (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo");
		protocolNo = msg.getString("//CFX/MSG/Payment3001/ProtocolNo"); 
		//CARDFLAG="2";
		//STATUS="1"; 

			String PayAcct = (String)msg.get("//CFX/MSG/Payment3001/PayAcct"); 
	    	String TraAmt = (String)msg.get("//CFX/MSG/Payment3001/TraAmt");
	        WorkDate = (String)msg.getString("//CFX/HEAD/WorkDate"); 
	        //WorkDate="20110110";
	    	String TaxTypeName = "";
	    	String payeeBankNo = (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo");
	    	//int taxTypeNum = Integer.parseInt(msg.getString("//CFX/MSG/Paymeheant3001/TaxTypeNum"));
	        trano = (String)msg.getString("//CFX/MSG/RealHead3001/TraNo");
	         
	    	SzsmChargePackager szsmpack = new SzsmChargePackager();
			//seq_no = Getseq_no();
	    	seq_no = (String)msg.getString("//CFX/MSG/RealHead3001/TraNo");
	    	msg.set("CHANNEL_SEQ_NO",seq_no);
			szsmpack.CreateCardSystemHeadData(WorkDate,seq_no);
			szsmpack.CreateCardBodyData(PayAcct, WorkDate, TraAmt, PayeeAcct,"",taxOrgCode,protocolNo);
			sendMsg = szsmpack.MakeUpCardChargePackager();
			byte[]sendbt=sendMsg.getBytes();
			try {
				sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
				sender.setSoTimeout(10*1000);			
				DataOutputStream out=new DataOutputStream(sender.getOutputStream());
				DataInputStream in=new DataInputStream(sender.getInputStream());
					 
				out.write(sendbt);
				logger.info("发送转账业务请求:["+sendMsg+"]");
					 
				out.flush();
				byte[] packageLengthbyte=new byte[10];
				in.read(packageLengthbyte,0,10);
				String lengthStr=new String(packageLengthbyte);
				logger.info("lengthStr is:         " +lengthStr);
				int packetlength=Integer.parseInt(lengthStr);
				logger.info("packetlength is:         " +packetlength);
				byte[] recPack=new byte[packetlength];	
				in.read(recPack,0,packetlength); 
				rcvChargeMsg = new String(recPack);
				szsmpack.revXml=rcvChargeMsg.trim();
				logger.info(rcvChargeMsg); 
			}
			  catch (IOException e) { 
				  e.printStackTrace();
				  result="94999";		
				  addWord="与主机通讯异常,交易失败"; 
				  msg.set("Result", result);
				  msg.set("AddWord",addWord);
				  msg.set("FLAG", "N");
					//发起冲正
				  msg.set("CHANNEL_SEQ_NO",seq_no); 
				  msg.set("CARDFLAG", CARDFLAG); 
				  return FAIL;
				  
			} 
			
			if(rcvChargeMsg.equals("")){
				result="94999";		
	    		addWord="与主机通讯异常,交易失败"; 
	    		msg.set("Result", result);
	    		msg.set("AddWord",addWord);
				msg.set("FLAG", "N");
				//发起冲正
				msg.set("CHANNEL_SEQ_NO",seq_no); 
				msg.set("CARDFLAG", CARDFLAG); 
				return FAIL;
				
			}else{ 
		    	//TR_CODE = szsmpack.GetParm("sys_header","MESSAGE_TYPE");		//交易码
		    	//IADAC_DAT = szsmpack.GetParm("sys_header","TRAN_DATE");					//会计日
		    	//JRN_NO = "0";						//日志号
		    	//VCH_NO = szsmpack.GetParm("body","REF_NO");				//传票号*/ 
				//先校验交易是否成功
				MsgRecordData=szsmpack.GetParm("sys_header", "RET_CODE");
				if (MsgRecordData.equals("000000")) {
					msg.set("FLAG", "Y");
				} else {					
					msg.set("FLAG", "O");
				}
				
				nowWorkData=szsmpack.GetParm("sys_header", "TRAN_DATE");
				nowBankTraNo=szsmpack.GetParm("body","REF_NO");
				logger.info("nowBankTraNo:"+nowBankTraNo);
				 if(MsgRecordData.equals("000000")) {	//交易成功	            
					 	STATUS = szsmpack.GetParm("body", "TRANSFER_ACTUAL_BAL");
					 	if(STATUS.equals("0.0")){
				    		msg.set("AddWord", "账户未签约");
				            msg.set("Result", "24009");
				            result="94053";
				            addWord="账户未签约";
				        	msg.set("Result", result);
				        	msg.set("AddWord", addWord);
				    	}
				    	if(STATUS.equals("2.0")){
				    		 msg.set("AddWord", "三方协议验证失败");
				             msg.set("Result", "24009");
				             result="94053";
				             addWord="账户未签约";
				         	msg.set("Result", result);
				         	msg.set("AddWord", addWord);
				
				    	}
				    	if(STATUS.equals("3.0")){
				    		 msg.set("AddWord", "协议已注销");
				 	        msg.set("Result", "24009");
				             result="94053";
				             addWord="账户未签约";
				         	msg.set("Result", result);
				         	msg.set("AddWord", addWord);
				    	}
				    	if(STATUS.equals("1.0")){
				    		CARDFLAG=szsmpack.GetParm("body", "TRANSFER_LEDGER_BAL"); 
				    		TaxPayCode=szsmpack.GetParm("body", "BOP_RPT_NO"); 
				    		msg.set("TaxPayCode", TaxPayCode);
				    		result="90000";
				    		addWord="交易成功";		
				    	}
			        }
				 else{
					 //需增加三方协议情况
					 if(MsgRecordData.equals("300313"))
					 {
						 result="24001";
			            addWord="账号不存在";	
					 }
					 if(MsgRecordData.equals("300314"))
					 {
						 result="94053";
						 addWord="账号状态错";	
					 } 
					 if(MsgRecordData.equals("120080"))
					 {
						 result="24011";
						 addWord="三方协议验证失败";	
					 }
					 if(MsgRecordData.equals("120080"))
					 {
						 result="24011";
						 addWord="三方协议验证失败";	
					 }
					 else
					 {					//交易失败			        	
			        	if(null != MsgRecordData && !MsgRecordData.equals("")){
			        		result = "99090";
			        		addWord="银行交易失败";	
				            addWord=szsmpack.GetParm("sys_header", "RET_MSG");
			        	}else{
							result="99091";
				        	addWord="银行交易失败";	
			        	}
					 }
				 }
		} 
		 
		//msg.set("TR_CODE",TR_CODE);
    	/*msg.set("JRN_NO", JRN_NO);*/
    	msg.set("seq_no", seq_no);
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
    	return SUCCESS;
	} 
	private String Getseq_no() {
		// TODO Auto-generated method stub
		DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
		String traDateTime=dtFormat.format(new Date());
		String randomdata=Math.random()*10000+"";
		randomdata=randomdata.substring(0,2);
		if(randomdata.equals("0."))
			randomdata="12";
		logger.info("traDateTime:"+traDateTime+"   randomdata:"+randomdata);
		return traDateTime+randomdata;
		
	}
	//add by 20150227
	private String GetReturnMSG(String code) throws SQLException{
		
		String sql = "SELECT MSG FROM ReturnMSG WHERE Code = '"+code +"'";
		String result = DBUtil.queryForString(sql);
		return result;
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

