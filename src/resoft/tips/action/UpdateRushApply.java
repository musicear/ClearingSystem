package resoft.tips.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.bankImpl.qhdyh.SzsmRushApplyPackager;
import resoft.xlink.core.Message;

/**
 * <p>更新冲正表状态，更新实时扣税的冲正标志</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 19:11:18
 */
public class UpdateRushApply extends AbstractSyncAction {
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	private Socket sender=null;
	String result="",addWord="";
	private String MsgRecordData="fail";
	private static final Log logger = LogFactory.getLog(TransferAcctSX.class);
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	private String taxOrgCode;
	private String payAcct;
	private String protocolNo;
	private String taxPayName;
	private String OriTraNo;
	private Object OriEntrustDate;
	private String STATUS;
	private String CARDFLAG;
	private String sendmsg;
	private String rcvChargeMsg;
	private String oriEntrustDate;
	private String oriTraNo;
	private String DR_ACCT_NO;
	private String CR_ACCT_NO;
	private String TRAN_AMT;
	private String CHANNEL_SEQ_NO;
	private String BU_SETTLEMENT_DATE;
	private String REVERSAL_REASON;
	private String REF_NO;
	private String Ret_statue;
	private String RET_STATUS="F";
    public int execute(Message msg) throws Exception {
    	String taxOrgCode = msg.getString("//CFX/MSG/RushApply1021/TaxOrgCode");  
        String oriEntrustDate = msg.getString("//CFX/MSG/RushApply1021/OriEntrustDate");
        String oriTraNo = msg.getString("//CFX/MSG/RushApply1021/OriTraNo"); 
        String WorkDate=msg.getString("//CFX/HEAD/WorkDate");
        
        REVERSAL_REASON=msg.getString("//CFX/MSG/RushApply1021/CancelReason");
        DR_ACCT_NO=msg.getString("DR_ACCT_NO");
        CR_ACCT_NO=msg.getString("CR_ACCT_NO");
        TRAN_AMT=msg.getString("TRAN_AMT");
        BU_SETTLEMENT_DATE=msg.getString("BU_SETTLEMENT_DATE");
        REF_NO=msg.getString("REF_NO");
        
        //是否加入 yang
        CHANNEL_SEQ_NO = oriTraNo;
        logger.info("REF_NO       "+REF_NO);
    	
    	

		SzsmRushApplyPackager pushpack=new SzsmRushApplyPackager();  
				
			

				logger.info("REF_NO       "+REF_NO);
				pushpack.CreatePushSystemHeadData(WorkDate);
				if(null == REF_NO || REF_NO.equals("null"))
					REF_NO = "";
				pushpack.CreateCardBodyData(REF_NO, REVERSAL_REASON, CHANNEL_SEQ_NO);
			    sendmsg=pushpack.MakeUpCardRushApplyPackager();
			    byte[]sendbt=sendmsg.getBytes();
		        logger.info("send cardpush:["+sendmsg+"]");
		        try {
					sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
					sender.setSoTimeout(6*1000);			
					DataOutputStream out=new DataOutputStream(sender.getOutputStream());
					DataInputStream in=new DataInputStream(sender.getInputStream());
				 
					out.write(sendbt); 
				 
					out.flush();
					byte[] packageLengthbyte=new byte[10];
					in.read(packageLengthbyte,0,10);
					String lengthStr=new String(packageLengthbyte);
					int packetlength=Integer.parseInt(lengthStr);
					byte[] recPack=new byte[packetlength];	
					in.read(recPack,0,packetlength); 
					rcvChargeMsg = new String(recPack);
					pushpack.revXml=rcvChargeMsg.trim();
					logger.info("rece sm cardpushpack"+rcvChargeMsg);
				}
		        catch (IOException e) { 
					e.printStackTrace();
				} 
		
		
		if(rcvChargeMsg.equals("NULL")){//与核心通讯错误
    		result="94999";		
    		addWord="与主机通讯异常";
	    }
		logger.info("征收机关代码："+taxOrgCode+"委托日期："+oriEntrustDate+"流水号："+oriTraNo);
		String sql = "update RealtimePayment set checkStatus='4',cancelFlag='Y' " +
		
        " where  TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
		logger.info("冲正sql："+sql);
		 DBUtil.executeUpdate(sql);
		 
		/*String returnreslut=pushpack.GetParm("sys_header", "RET_CODE");
		RET_STATUS=pushpack.GetParm("sys_header", "RET_STATUS");
		if(returnreslut.equals("000000")&&RET_STATUS.equals("S"))
		{
			String sql = "update RealtimePayment set checkStatus='4',cancelFlag='Y' " +
		
	         " where result='90000' and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
			 DBUtil.executeUpdate(sql);
		}
  
		else
		{
			result="94999";		
    		addWord="冲正失败";
		} */
        return SUCCESS;
    }

   

}
