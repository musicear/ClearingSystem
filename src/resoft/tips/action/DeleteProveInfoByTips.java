package resoft.tips.action;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.bankImpl.qhdyh.SzsmProveinfoPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>删除三方协议</p>

 * Time: 17:10:10
 */
public class DeleteProveInfoByTips implements Action {
	
	private static final Log logger = LogFactory.getLog(DeleteProveInfoByTips.class);
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	private Socket sender=null;
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	
	public int execute(Message msg) throws Exception {
		
		 //从地税中取出报文的相应内容，以便校验协议是否可用
		String taxOrgCode = msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode");
		String taxPayCode = msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode");
        String payAcct = msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"); 	           
        String protocolNo = msg.getString("//CFX/MSG/ProveInfo9114/ProtocolNo"); 
        String taxPayName = msg.getString("//CFX/MSG/ProveInfo9114/TaxPayName"); 
        String handOrgName =  msg.getString("//CFX/MSG/ProveInfo9114/HandOrgName");
        String payOpBkCode = msg.getString("//CFX/MSG/ProveInfo9114/PayOpBkCode");
        String payBkCode = msg.getString("//CFX/MSG/ProveInfo9114/PayBkCode");
        String VCSign = msg.getString("//CFX/MSG/ProveInfo9114/VCSign");
        String workDate=msg.getString("//CFX/HEAD/WorkDate");
     
        SzsmProveinfoPackager szsmpack = new SzsmProveinfoPackager();
        szsmpack.CreateSystemHeadData("1502",workDate);//注销三方协议
        szsmpack.CreateAppHeadData();
        szsmpack.CreateBodyData(taxOrgCode, taxPayCode, payAcct, protocolNo, taxPayName);
        sendMsg = szsmpack.MakeUpCheckinfoPackager();//需要实现
        logger.info("发送三方协议注销请求:["+sendMsg+"]");
        byte[]sendbt=sendMsg.getBytes();
        sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
		sender.setSoTimeout(6*1000);			
		DataOutputStream out=new DataOutputStream(sender.getOutputStream());
		DataInputStream in=new DataInputStream(sender.getInputStream());
		
		out.write(sendbt);
		out.flush();     
		
		byte[] recepack=new byte[10000];
		int packlen=in.read(recepack,0,10000);
		String recestr=new String(recepack);
		logger.info("解析整个报文：["+recestr+"]");
		
		byte[] packageLengthbyte=new byte[10];
		for(int k=0;k<10;k++)
		{
			packageLengthbyte[k]=recepack[k];
		}
		//in.read(packageLengthbyte,0,10);
		String lengthStr=new String(packageLengthbyte);
		int packetlength=Integer.parseInt(lengthStr);
		logger.info("接受报文长度:"+(packlen-10)+"   解析报文长度："+packetlength);
		byte[] recPack=new byte[packetlength];	
		for(int k=0;k<packetlength;k++)
		{
			recPack[k]=recepack[k+10];
		}
		//in.read(recPack,0,packetlength);
	    //rcvMsg = in.readUTF();
		rcvMsg = new String(recPack);
		logger.info("接收三方协议注销应答：["+rcvMsg+"]");
		szsmpack.revXml=rcvMsg.trim();
		String VCResult=szsmpack.GetParm("sys_header", "RET_CODE"); 
		String AddWord=szsmpack.GetParm("sys_header", "RET_MSG");
		String statue=szsmpack.GetParm("body", "STATUE"); 
		if(VCResult.equals("000000") || VCResult.equals("160005"))
		{
			msg.set("//CFX/MSG/ProveReturn9115/VCResult","0"); 
		    msg.set("//CFX/MSG/ProveReturn9115/AddWord","注销成功");
        }
		else  
		{ 
			msg.set("//CFX/MSG/ProveReturn9115/VCResult","1"); 
			msg.set("//CFX/MSG/ProveReturn9115/AddWord","注销失败，协议不存在或信息有误");
		} 
        
        //设置到原子交易里  
        msg.set("TaxOrgCode", taxOrgCode);
        msg.set("TaxPayCode", taxPayCode);
        msg.set("PayAcct", payAcct);
        msg.set("ProtocolNo", protocolNo);
        msg.set("PayOpBkCode", payOpBkCode);
        msg.set("PayBkCode", payBkCode);
        msg.set("handOrgName", handOrgName);
        
        
        //对返回的报文9115进行设置
        //设置基本回显信息
    	msg.set("//CFX/MSG/ProveReturn9115/OriSendOrgCode", msg.getString("//CFX/MSG/ProveInfo9114/SendOrgCode"));
    	msg.set("//CFX/MSG/ProveReturn9115/OriEntrustDate", msg.getString("//CFX/MSG/ProveInfo9114/EntrustDate"));
    	msg.set("//CFX/MSG/ProveReturn9115/OriVCNo", msg.getString("//CFX/MSG/ProveInfo9114/VCNo"));
    	msg.set("//CFX/MSG/ProveReturn9115/VCSign", msg.getString("//CFX/MSG/ProveInfo9114/VCSign"));
    	
    	
    	msg.set("//CFX/MSG/ProveReturn9115/TaxPayCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayCode"));
		msg.set("//CFX/MSG/ProveReturn9115/TaxOrgCode",msg.getString("//CFX/MSG/ProveInfo9114/TaxOrgCode"));
		msg.set("//CFX/MSG/ProveReturn9115/TaxPayName",msg.getString("//CFX/MSG/ProveInfo9114/TaxPayName"));
		msg.set("//CFX/MSG/ProveReturn9115/PayBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayBkCode"));
		msg.set("//CFX/MSG/ProveReturn9115/PayOpBkCode",msg.getString("//CFX/MSG/ProveInfo9114/PayOpBkCode"));    		
		msg.set("//CFX/MSG/ProveReturn9115/PayAcct",msg.getString("//CFX/MSG/ProveInfo9114/PayAcct"));
		msg.set("//CFX/MSG/ProveReturn9115/HandOrgName",msg.getString("//CFX/MSG/ProveInfo9114/HandOrgName"));
		msg.set("//CFX/MSG/ProveReturn9115/ProtocolNo",msg.getString("//CFX/MSG/ProveInfo9114/ProtocolNo"));
		msg.set("//CFX/MSG/ProveReturn9115/PayAcct", payAcct);
		msg.set("//CFX/MSG/ProveReturn9115/PayBkCode",payBkCode);
		msg.set("//CFX/MSG/ProveReturn9115/PayOpBkCode", payOpBkCode);
	
    	return  SUCCESS;
    }
}

