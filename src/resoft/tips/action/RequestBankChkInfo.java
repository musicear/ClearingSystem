package resoft.tips.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.bankImpl.qhdyh.SzsmCheckPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>请求对账文件</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 2:37:04
 */
public class RequestBankChkInfo implements Action {
    private static final Log logger = LogFactory.getLog(RequestBankChkInfo.class);
    
    String startTime="",endTime="",cheTime="";
    String rcvMsg="";
    Socket sender;
    Configuration conf = Configuration.getInstance();
    private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));

    public int execute(Message msg) throws Exception {
        try { 
        	Message newMsg = new DefaultMessage();
	        newMsg.set("交易码","3000");
	        newMsg.set("TraDate",msg.getString("ChkDate"));
	        startTime=msg.getString("ChkDate");
	        endTime=msg.getString("ChkDate");
	        cheTime=msg.getString("ChkDate");
	        SzsmCheckPackager pack=new SzsmCheckPackager();
	        pack.CreateSystemHeadData(startTime);
	        pack.CreateAppHeadData();
	        pack.CreateBodyData(startTime,startTime,cheTime);
	        String sendMsg = pack.MakeUpCheckPackager();//需要实现
	        byte[]sendbt=sendMsg.getBytes();
	        sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
		 
			out.write(sendbt);
			logger.info("发送卡业务请求:["+sendMsg+"]");
		 
			out.flush(); 
			msg.set("BankChkFileName","TI"+cheTime+".txt");
	        //MessageSender sender = MessageSenderUtil.getMessageSender();
	       
          //  Message returnMsg = sender.send(newMsg);
             return SUCCESS;
        } catch(Exception e) {
            logger.error("请求对账文件失败",e);
            return FAIL;
        }
    }
}
