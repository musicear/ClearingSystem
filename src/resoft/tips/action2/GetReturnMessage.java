package resoft.tips.action2;


import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.chqsh.FileTax;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class GetReturnMessage implements Action{

	private static final Log logger = LogFactory.getLog(FileTax.class);
	private static Configuration conf = Configuration.getInstance();
	private String fileName = "";
	private String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	private String PayAcct = "";
	private String BegineDate = "";
	private String EndDate = "";
	private String RET_STATUS="",SEQ_NO="",SOURCE_BRANCH_NO="",DEST_BRANCH_NO="",BRANCH_ID="",RET_CODE="",RET_MSG="";
	private String TaxAmt = "";
	private String ReturnResult = "";
	private String BranchNo = "";
	List rtqueryList = null;
	List bpqueryList = null;
	SelectChargeInfoPack chargeinfopack=new SelectChargeInfoPack();
	Calendar date = Calendar.getInstance();
	private String MESSAGE_TYPE;
	private String MESSAGE_CODE;
	private String TRAN_DATE;
	private String TRAN_TIMESTAMP;
	private String SERVICE_CODE;
	private String TAXORGCODE; 
	private String protocolNo;


	public int execute(Message msg) throws Exception {
		String xml="";
		
		//Message returnMsg=new DefaultMessage();   
		
        
        try
		{
			SOURCE_BRANCH_NO=msg.getString("SOURCE_BRANCH_NO");
			DEST_BRANCH_NO=msg.getString("DEST_BRANCH_NO");
			BRANCH_ID=msg.getString("BRANCH_ID");
			MESSAGE_TYPE=Integer.parseInt(msg.getString("MESSAGE_TYPE"))+10+"";
			MESSAGE_CODE=msg.getString("MESSAGE_CODE");
			TRAN_DATE=msg.getString("TRAN_DATE");
			TRAN_TIMESTAMP="";
			SERVICE_CODE=msg.getString("SERVICE_CODE");
		}
		catch(Exception e)
		{
			
		}
        
        //String ReturnResult = msg.getString("//CFX/MSG/MsgReturn9121/Result").equals("90000")?"000000":"444444";
        //logger.info("result is : "+ReturnResult);
        //String AddWord = msg.getString("//CFX/MSG/MsgReturn9121/AddWord");
		String ReturnResult = msg.getString("ReturnResult").equals("90001")?"444444":"000000";
        String AddWord = msg.getString("AddWord");
        //String ReturnResult ="000000";
        
      	//if(ReturnResult.equals("000000"))
      		RET_STATUS = "S";
      	//else
      		//RET_STATUS = "F";
      	//logger.info("ret_status is: "+ RET_STATUS);
      	RET_CODE = ReturnResult;
      	if(null == AddWord || AddWord.equals(""))
      		RET_MSG = "Success";
      	else
      		RET_MSG = AddWord;
        
      	
        chargeinfopack.CreateSystemHeadData(RET_STATUS, BRANCH_ID, SOURCE_BRANCH_NO, DEST_BRANCH_NO,MESSAGE_TYPE,MESSAGE_CODE,TRAN_DATE,TRAN_TIMESTAMP, RET_CODE, RET_MSG, SEQ_NO,SERVICE_CODE);
		String sendMsg = chargeinfopack.MakeUpCheckPackager(RET_STATUS);
		logger.info("sendMsg is : " + sendMsg);
		msg.set("packFile", "T9117returnsm");
		msg.set("sendMsg", sendMsg);
        return 0;
    }
}

