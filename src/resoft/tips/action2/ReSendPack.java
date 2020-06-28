package resoft.tips.action2;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


public class ReSendPack implements Action{  
	
	private static final Log logger = LogFactory.getLog(ReSendPack.class);
	private static Configuration conf = Configuration.getInstance();
	
	private String SendOrgCode = null;
	private String EntrustDate = null;
	private String OriPackMsgNo = null;
	private String OriChkDate = null;
	private String OriPackNo = null;
	private String OrgType = null;
	//private String RET_STATUS = "F";
	
	public int execute(Message msg) throws Exception {
		// TODO Auto-generated method stub
		SendOrgCode = conf.getProperty("general","BankSrcNodeCode");
		EntrustDate = DateTimeUtil.getDateString();
		OriPackMsgNo = msg.getString("ORIPACKMSGNO");
		OriChkDate = msg.getString("ORICHKDATE");
		OriPackNo = msg.getString("ORIPACKNO");
		OrgType = "3";
		
		
		
		String checkMsgNo = null;
		if(!OriPackMsgNo.equals("3200") && !OriPackMsgNo.equals("3201")){
			msg.set("ReturnResult","90001");
			msg.set("AddWord","OriPackMsgNo wrong!");
			return -1;
		}
		if(OriPackNo.length()!= 8){
			msg.set("ReturnResult","90001");
			msg.set("AddWord","OriPackNo wrong!");
			return -1;
		}
		if(OriChkDate.length()!= 8){
			msg.set("ReturnResult","90001");
			msg.set("AddWord","OriChkDate wrong");
			return -1;
		}
		if(!isNumeric(OriChkDate)){
			msg.set("ReturnResult","90001");
			msg.set("AddWord","OriChkDate wrong");
			return -1;
		}
		/*
		
		if(OriPackMsgNo.equals("3200"))
			checkMsgNo = Check2201(OriPackNo);
		if(OriPackMsgNo.equals("3201"))
			checkMsgNo = Check2202(OriPackNo);
		
		
		if(null == checkMsgNo || checkMsgNo.equals("")){			
			msg.set("ReturnResult","90001");
			msg.set("AddWord","没有对应请求包");
			return -1;
		}else{
			msg.set("ReturnResult","90001");
			*/
		msg.set("ReturnResult","90000");
			logger.info("进入...........................");
			msg.set("//CFX/MSG/GetMsg9117/SendOrgCode", SendOrgCode);
			msg.set("//CFX/MSG/GetMsg9117/EntrustDate", EntrustDate);
			msg.set("//CFX/MSG/GetMsg9117/OriPackMsgNo", OriPackMsgNo);
			msg.set("//CFX/MSG/GetMsg9117/OriChkDate", OriChkDate);
			msg.set("//CFX/MSG/GetMsg9117/OriPackNo", OriPackNo);
			msg.set("//CFX/MSG/GetMsg9117/OrgType", OrgType);
		
		return 0;
	}
	
	private String Check2201(String OriPackNo) throws SQLException{
		String sql="Select REQUESTPACKNO from PayRequestPack where REQUESTPACKNO='" + OriPackNo + "'";
		String result = DBUtil.queryForString(sql);
		return result;
	}
	private String Check2202(String OriPackNo) throws SQLException{

		String sql_ref="Select PACKNO from RefundPack where PACKNO='" + OriPackNo + "'";
		String result = DBUtil.queryForString(sql_ref);
		return result;
	}
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
}
