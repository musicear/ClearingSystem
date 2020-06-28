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
 * <p>ɾ������Э��</p>

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
		
		 //�ӵ�˰��ȡ�����ĵ���Ӧ���ݣ��Ա�У��Э���Ƿ����
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
        szsmpack.CreateSystemHeadData("1502",workDate);//ע������Э��
        szsmpack.CreateAppHeadData();
        szsmpack.CreateBodyData(taxOrgCode, taxPayCode, payAcct, protocolNo, taxPayName);
        sendMsg = szsmpack.MakeUpCheckinfoPackager();//��Ҫʵ��
        logger.info("��������Э��ע������:["+sendMsg+"]");
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
		logger.info("�����������ģ�["+recestr+"]");
		
		byte[] packageLengthbyte=new byte[10];
		for(int k=0;k<10;k++)
		{
			packageLengthbyte[k]=recepack[k];
		}
		//in.read(packageLengthbyte,0,10);
		String lengthStr=new String(packageLengthbyte);
		int packetlength=Integer.parseInt(lengthStr);
		logger.info("���ܱ��ĳ���:"+(packlen-10)+"   �������ĳ��ȣ�"+packetlength);
		byte[] recPack=new byte[packetlength];	
		for(int k=0;k<packetlength;k++)
		{
			recPack[k]=recepack[k+10];
		}
		//in.read(recPack,0,packetlength);
	    //rcvMsg = in.readUTF();
		rcvMsg = new String(recPack);
		logger.info("��������Э��ע��Ӧ��["+rcvMsg+"]");
		szsmpack.revXml=rcvMsg.trim();
		String VCResult=szsmpack.GetParm("sys_header", "RET_CODE"); 
		String AddWord=szsmpack.GetParm("sys_header", "RET_MSG");
		String statue=szsmpack.GetParm("body", "STATUE"); 
		if(VCResult.equals("000000") || VCResult.equals("160005"))
		{
			msg.set("//CFX/MSG/ProveReturn9115/VCResult","0"); 
		    msg.set("//CFX/MSG/ProveReturn9115/AddWord","ע���ɹ�");
        }
		else  
		{ 
			msg.set("//CFX/MSG/ProveReturn9115/VCResult","1"); 
			msg.set("//CFX/MSG/ProveReturn9115/AddWord","ע��ʧ�ܣ�Э�鲻���ڻ���Ϣ����");
		} 
        
        //���õ�ԭ�ӽ�����  
        msg.set("TaxOrgCode", taxOrgCode);
        msg.set("TaxPayCode", taxPayCode);
        msg.set("PayAcct", payAcct);
        msg.set("ProtocolNo", protocolNo);
        msg.set("PayOpBkCode", payOpBkCode);
        msg.set("PayBkCode", payBkCode);
        msg.set("handOrgName", handOrgName);
        
        
        //�Է��صı���9115��������
        //���û���������Ϣ
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

