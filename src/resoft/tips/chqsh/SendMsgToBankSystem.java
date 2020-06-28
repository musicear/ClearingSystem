package resoft.tips.chqsh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
/**
 * <p>与后台tuxedo交互，包含：记账、摸帐和查询三个交易</p>
 * Author: fanchengwei
 * Date  : 2007-10-10
 * Time  : 10:12:34
 * 注意要点：没有明确的成功答复绝对不可以返回成功！另外金额不可以为负！！！
 */
public class SendMsgToBankSystem {
	private static final Log logger = LogFactory.getLog(SendMsgToBankSystem.class);
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	
	private Socket sender=null;
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	TuxedoPackager onlyone = TuxedoPackager.getTuxedoPackager();
	
	public String sendChk(String SUP1_ID,String SUP1_PSWD,String SUP2_ID){
		try{	
			sendMsg = onlyone.getPswSqlPackager(SUP1_ID, SUP1_PSWD, SUP2_ID);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*10000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("发送主管密码校验TUXEDO请求:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("接收主管密码校验TUXEDO应答：["+rcvMsg+"]");
		}catch(Exception e){						
			e.printStackTrace();			
			rcvMsg = "NULL";
			return rcvMsg;
		}finally{
			if (sender!=null) {
				try {
					sender.close();
				}catch(Exception e){
					sender=null;
				}
			}
		}
		return rcvMsg;
	}
	
	/**
	 * 校验纳税人的密码
	 * @param SQL	：纳税人卡号，密码
	 * @return		：校验报文
	 */
	public String sendMsg(String SQL){
		try{	
			sendMsg = onlyone.getPswSqlPackager(SQL);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*10000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("发送密码校验TUXEDO请求:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("接收密码校验TUXEDO应答：["+rcvMsg+"]");
		}catch(Exception e){						
			e.printStackTrace();			
			rcvMsg = "NULL";
			return rcvMsg;
		}finally{
			if (sender!=null) {
				try {
					sender.close();
				}catch(Exception e){
					sender=null;
				}
			}
		}
		return rcvMsg;
	}
	/**
	 * @param SQL		：查询语句
	 * @param TL_TD		：操作柜员
	 * @return			：查询结果
	 */
	public String sendMsg(String SQL,String TL_TD){
		/**
		 * 查询报文
		 * 通过参数SQL传递过来的查询条件，经过单态函数onlyone的组装形成发送到tuxedo的规范化报文
		 * 报文格式：报文头+data1+data2（格式由”小额支付记账交易接口“中上下传报文和查询账户状态定义）
		 * 报文头：由TuxedoPackHeadInit类中的静态变量SQLHead组成，其中各个元素根据商行”小额支付记账交易接口“中
		 * 上下传报文定义。基本除交易码外都是定值。
		 * data1：20个字符组成的文件名（实际程序中没有用到这个文件名称）
		 */
		try{	
			sendMsg = onlyone.getSqlPackager(SQL, TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*10000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("发送SQL查询TUXEDO请求:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("接收SQL查询TUXEDO应答：["+rcvMsg+"]");
		}catch(Exception e){						
			e.printStackTrace();
			rcvMsg = "NULL";
			return rcvMsg;
		}finally{
			if (sender!=null) {
				try {
					sender.close();
				}catch(Exception e){
					sender=null;
				}
			}
		}
		return rcvMsg;
	}
	
	
	
	
	/**
	 * 记账交易
	 * @param PayAcct	：纳税人帐号
	 * @param TraAmt	：金额
	 * @param TaxTypeName:税种信息
	 * @param TL_TD		：操作柜员
	 * @return			：记账结果
	 */
	//yangyuanxu update
	public synchronized String sendInMsg(String PayAcct,String TraAmt,String TaxTypeName,String PayeeBankNo,String TL_TD){
		/**
		 * 记账报文
		 * 报文结构：报文头+data1+data2
		 * 报文头：根据各种账户的不同而不同，根据“增加组合接口”中的定义在TuxedoPackHeadInit中定义为静态变量。
		 * data1：定义了进行扣款的帐号、扣款的帐号、扣款金额。
		 * data2：通用“过渡转内部核算”中规定的格式填写。
		 */
		if(Double.parseDouble(TraAmt) < 0){
			return rcvMsg;
		}
		try{	
			sendMsg = onlyone.getInPackager(PayAcct, TraAmt, PayeeBankNo, "代扣"+TaxTypeName, TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//发送方标识
			out.write(sendMsg.getBytes());
			logger.info("发送记账TUXEDO请求:["+sendMsg+"]");
			out.flush();
			byte[] recPack=new byte[85];			
			in.read(recPack,0,85);
			rcvMsg = new String(recPack);
			logger.info("接收记账TUXEDO应答：["+rcvMsg+"]");
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
		return rcvMsg;
	}	
	
	/**
	 * 抹账交易
	 * @param JRN_NO	：日志号
	 * @param VCH_NO	：传票号
	 * @param TR_CODE	：交易码
	 * @param TL_TD		：操作柜员
	 * @return			：摸帐结果
	 */
	public String sendOutMsg(String JRN_NO,String VCH_NO,String TR_CODE,String TL_TD){
		/**
		 * 抹账报文
		 * 报文格式：报文头+data1
		 * 报文头：同记账交易报文头格式
		 * data1:传输记账所需要的交易日志号、传票号、交易码和柜员号（柜员号任意）
		 * 参数：日志号、传票号、交易码、柜员号
		 */
		try{	
			sendMsg = onlyone.getOutPackager(JRN_NO,VCH_NO,TR_CODE,TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			//发送方标识
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("发送抹账TUXEDO请求:["+sendMsg+"]");
			//接收回执
			DataInputStream in=new DataInputStream(sender.getInputStream());
			byte[] recPack=new byte[85];			
			in.read(recPack,0,85);
			rcvMsg = new String(recPack);
			logger.info("接收抹账TUXEDO应答：["+rcvMsg+"]");
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
		return rcvMsg;
	}
}



