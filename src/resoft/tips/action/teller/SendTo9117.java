package resoft.tips.action.teller;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.util.TransCommUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;



public class SendTo9117 implements Action{

	
	private static Configuration conf = Configuration.getInstance();

    private static Log logger = LogFactory.getLog(SendTo9117.class);
	public int execute(Message msg) throws Exception {
		// TODO Auto-generated method stub
		
		
		String OriPackMsgNo = msg.getString("ORIPACKMSGNO");
		String OriChkDate = msg.getString("ORICHKDATE");
		String OriPackNo = msg.getString("ORIPACKNO");
		
		if(!"3200".equals(OriPackMsgNo) && !"3201".equals(OriPackMsgNo) ){
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", "原报文编号必须为3200或3201！");
			return 0;
		}
		if("".equals(OriChkDate) || null == OriChkDate){
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", "原核对日期不能为空！");
			return 0;
		}
		if("".equals(OriPackNo) || null == OriPackNo){
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", "原包流水号不能为空！");
			return 0;
		}
		
		//Message returnMsg;
		resoft.basLink.Message receiveMsg = new resoft.basLink.Message();
		resoft.basLink.Message sendMsg = new resoft.basLink.Message();
		sendMsg.setValue("ORIPACKMSGNO", OriPackMsgNo);
		sendMsg.setValue("ORICHKDATE", OriChkDate);
		sendMsg.setValue("ORIPACKNO", OriPackNo);
		
		try
		{
			receiveMsg = TransCommUtil.send("T9117", sendMsg);
			
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", "原包流水号不能为空！");
		}
		catch (IOException e)
		{
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", "访问后台程序失败！");
			return 0;
		}
		String result = receiveMsg.getValue("Result");
		String addword = receiveMsg.getValue("AddWord");
		logger.info("SendTo9117's Result is: "+ result);
		
		if("90000".equals(result)){
			msg.set("RET_STATUS", "S");
			msg.set("RET_CODE", "000000");
			msg.set("RET_MSG", addword);
		}else{
			msg.set("RET_STATUS", "F");
			msg.set("RET_CODE", "s11111");
			msg.set("RET_MSG", addword);
		}
		
		return 0;
	}
	
}
