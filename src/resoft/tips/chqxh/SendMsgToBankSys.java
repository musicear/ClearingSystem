package resoft.tips.chqxh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message; 
/**
 * <p>与银行核心系统进行扣款信息交换</p>
 * Author: liwei
 * Date: 2007-07-31
 * Time: 14:25:06
 */
public class SendMsgToBankSys {
	
	private static final Log logger = LogFactory.getLog(SendMsgToBankSys.class);	
	private Message msg=null;	
	private Socket sender = null;
	private ACEPackager aceSendPack=null;			//发送ACE包
	private ACEPackager aceRecPack=null;			//接收ACE包
	private static final String ACESender="TIPS";	//ACE报文发送者		
	Configuration conf = Configuration.getInstance();
	
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	
	public SendMsgToBankSys(Message msgInfo){
		this.msg=msgInfo;		
		this.aceSendPack=(ACEPackager)msg.get("ACESendObj");	//得到要发送的ACEObj
		logger.info("ready for send msg to："+BANKPAYSYSHOST+",端口："+BANKPAYSYSPORT);
	}
	
	//与核心中间业务平台发送和接收消息
	public void sendMsg(){
		try{
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			//对账的超时时间要长
			if(aceSendPack.transCode.equals("2035")){
				sender.setSoTimeout(60*1000);
			}else {
				sender.setSoTimeout(6*1000);
			}
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			//发送方标识
			out.write(ACESender.getBytes());
			//发送报文
			byte[] sendPack=aceSendPack.pack.getBytes();			
			logger.info("向银行核心系统发送字节长度："+aceSendPack.pack.getBytes().length+",报文：["+new String(sendPack)+"]");
			out.write(sendPack);
			out.flush();
			//接收回执
			DataInputStream in=new DataInputStream(sender.getInputStream());
			byte[] recPackLen=new byte[6];			
			in.read(recPackLen);			
			int length=(Integer.parseInt(new String(recPackLen))-6);
			byte[] recPack=new byte[length];
			in.read(recPack,0,length);
			logger.info("与银行核心支付系统交易成功,接收字节长度："+new String(recPackLen)+",接收报文：["+new String(recPackLen)+new String(recPack)+"]");
						
			//设置回执			
			if (aceSendPack.transCode.equals("2032")) {
				aceRecPack=new ACE2032(new String(recPackLen)+new String(recPack));
			}else if(aceSendPack.transCode.equals("2033")){
				aceRecPack=new ACE2033(new String(recPackLen)+new String(recPack));
			}else if(aceSendPack.transCode.equals("2035")){
				aceRecPack=new ACE2035(new String(recPackLen)+new String(recPack));
			}								
			msg.set("ACERecObj", aceRecPack);						
		}catch(Exception e){
			//添加对核心的错误处理，确保只有明确的成功交易才返回，否则返回失败			
			logger.info("与核心系统通讯错误！");
			aceRecPack=null;
			msg.set("ACERecObj", aceRecPack);
			e.printStackTrace();								
		}finally{
			if (sender!=null) {
				try {
					sender.close();
				}catch(Exception e){
					sender=null;
				}
			}
		}
	}
	 
}
