package resoft.tips.action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqsh.SendMsgToBankSystem;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;

/**
 * <p>具体的账务处理</p>
 * User: fanchengwei
 * Date: 2007-10-24
 * Time: 21:03:26
 */
public class TransferAcct extends AbstractSyncAction {
    
	private static final Log logger = LogFactory.getLog(TransferAcct.class);
    
    private static final String TL_TD = "880";
    
    public synchronized int execute(Message msg){ 
    	
    	SendMsgToBankSystem senMsg2Bank = new SendMsgToBankSystem();
    	String RcvMsg = "NULL";
    	String TR_CODE = "fail";											//交易码
    	String JRN_NO = "fail";												//交易日志号
    	String VCH_NO = "fail";												//传票号
    	String MSG_DATA = "0000";											//结果描述
    	String id = "fail";													//Tuxedo交易结果
    	String IADAC_DAT = DateTimeUtil.getDateString();					//tuxedo返回的银行会计日
		String result="",addWord="";
    	
		//synchronized(TuxedoPackager.getTuxedoPackager()){
	    	String PayAcct = (String)msg.get("//CFX/MSG/Payment3001/PayAcct");
	    	String TraAmt = (String)msg.get("//CFX/MSG/Payment3001/TraAmt");
	    	String WorkDate = (String)msg.getString("//CFX/HEAD/WorkDate");
	    	String TaxTypeName = "";
	    	String payeeBankNo = (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo");
	    	int taxTypeNum = Integer.parseInt(msg.getString("//CFX/MSG/Payment3001/TaxTypeNum"));
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
	    	
	    	logger.info("代扣税种名称为：" + TaxTypeName);
	    	
	    	
	    	
	    	RcvMsg = senMsg2Bank.sendInMsg(PayAcct, TraAmt, TaxTypeName,payeeBankNo,TL_TD + (1 + System.currentTimeMillis()%4));	    			
	    	if(RcvMsg.equals("NULL")){//与核心通讯错误
	    		result="94999";		
	    		addWord="与主机通讯异常";
		    }else {
		    	//logger.info("Message  from  tuxedo  :"+RcvMsg);
		    	id = RcvMsg.substring(37,38);							//后台交易结果
		    	TR_CODE = "0" + RcvMsg.substring(11, 14);				//交易码
		    	IADAC_DAT = RcvMsg.substring(21, 29);					//会计日
		    	JRN_NO = RcvMsg.substring(54, 61);						//日志号
		    	VCH_NO = RcvMsg.substring(69, 77);						//传票号
		    	MSG_DATA = RcvMsg.substring(81, 85);					//处理结果描述
		    	logger.info("结果码 是："+MSG_DATA);
		    	logger.info("Tuxedo： " + id + "|" + TR_CODE + "|" + IADAC_DAT + "|" + JRN_NO + "|" + VCH_NO + "|" + MSG_DATA);
		        if(id.equals("N")) {	//交易成功	            
		            result="90000";
		            addWord="交易成功";		            
		        } else{					//交易失败			        	
		        	if(MSG_DATA.equals("1840") || MSG_DATA.equals("2286") )//账户冻结
		        		result="24004";
		        	if(MSG_DATA.equals("1627"))//余额不足
		        	    result="24007";
		        	if(MSG_DATA.equals("1624"))//
		        	    result="24005";
		        	if(MSG_DATA.equals("2297") || MSG_DATA.equals("1096") )//
		        	    result="24008";
		        	try{
		        		addWord = DBUtil.queryForString("select addword from transresult where flag='" + MSG_DATA+"' ");
		        		if(addWord.length()>=15){
		        			addWord=addWord.substring(0,14);
		        		}
		        	}catch (Exception e){
		        		e.printStackTrace();
		        		addWord = "其它错误！";
		        	}
		        }
		    }
	    	
	    	logger.info("与后台TUXEDO扣税状态：["+addWord+"]");
	    	
	    	/**
	    	 * 与后台交易返回的结果为"NULL"时，表明没有从tuxedo返回任何信息，标志置明确的失败信息N
	    	 * 其余有状态码的返回结果，表明从tuxedo返回了相应的信息保存状态为Y或者O
	    	 */
	    	if(RcvMsg.equals("NULL")){
				msg.set("FLAG", "N");
			}else{
				if (MSG_DATA.equals("9004")) {
					msg.set("FLAG", "Y");
				} else {					
					msg.set("FLAG", "O");
				}
			}		
	    	msg.set("TR_CODE",TR_CODE);
	    	msg.set("JRN_NO", JRN_NO);
	    	msg.set("VCH_NO", VCH_NO);
	    	msg.set("MSG_DATA", MSG_DATA);
	    	msg.set("IADAC_DAT",IADAC_DAT);
	    	msg.set("Result", result);
	    	msg.set("AddWord", addWord);    	
	    	msg.set("bankTraNo",(String)msg.getString("//CFX/MSG/RealHead3001/TraNo"));
	        msg.set("bankTraDate",DateTimeUtil.getDateTimeString().substring(0,10));
	        msg.set("bankTraTime",DateTimeUtil.getDateTimeString().substring(11));    	    	
	    	msg.set("TipsWorkDate", (String)msg.getString("//CFX/HEAD/WorkDate"));
	    	//yangyuanxu add
	    	msg.set("payeeBankNo", (String)msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo"));
	        msg.set(getResultNodePath(),result);
	        msg.set(getAddWordNodePath(),addWord);
	        
	        //TR_CODE = JRN_NO = VCH_NO = MSG_DATA = IADAC_DAT = "fail";
    	//}
    	
        return SUCCESS;        
    }
}
