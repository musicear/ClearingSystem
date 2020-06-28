package resoft.tips.chqxh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message; 
/**
 * <p>�����к���ϵͳ���пۿ���Ϣ����</p>
 * Author: liwei
 * Date: 2007-07-31
 * Time: 14:25:06
 */
public class SendMsgToBankSys {
	
	private static final Log logger = LogFactory.getLog(SendMsgToBankSys.class);	
	private Message msg=null;	
	private Socket sender = null;
	private ACEPackager aceSendPack=null;			//����ACE��
	private ACEPackager aceRecPack=null;			//����ACE��
	private static final String ACESender="TIPS";	//ACE���ķ�����		
	Configuration conf = Configuration.getInstance();
	
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	
	public SendMsgToBankSys(Message msgInfo){
		this.msg=msgInfo;		
		this.aceSendPack=(ACEPackager)msg.get("ACESendObj");	//�õ�Ҫ���͵�ACEObj
		logger.info("ready for send msg to��"+BANKPAYSYSHOST+",�˿ڣ�"+BANKPAYSYSPORT);
	}
	
	//������м�ҵ��ƽ̨���ͺͽ�����Ϣ
	public void sendMsg(){
		try{
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			//���˵ĳ�ʱʱ��Ҫ��
			if(aceSendPack.transCode.equals("2035")){
				sender.setSoTimeout(60*1000);
			}else {
				sender.setSoTimeout(6*1000);
			}
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			//���ͷ���ʶ
			out.write(ACESender.getBytes());
			//���ͱ���
			byte[] sendPack=aceSendPack.pack.getBytes();			
			logger.info("�����к���ϵͳ�����ֽڳ��ȣ�"+aceSendPack.pack.getBytes().length+",���ģ�["+new String(sendPack)+"]");
			out.write(sendPack);
			out.flush();
			//���ջ�ִ
			DataInputStream in=new DataInputStream(sender.getInputStream());
			byte[] recPackLen=new byte[6];			
			in.read(recPackLen);			
			int length=(Integer.parseInt(new String(recPackLen))-6);
			byte[] recPack=new byte[length];
			in.read(recPack,0,length);
			logger.info("�����к���֧��ϵͳ���׳ɹ�,�����ֽڳ��ȣ�"+new String(recPackLen)+",���ձ��ģ�["+new String(recPackLen)+new String(recPack)+"]");
						
			//���û�ִ			
			if (aceSendPack.transCode.equals("2032")) {
				aceRecPack=new ACE2032(new String(recPackLen)+new String(recPack));
			}else if(aceSendPack.transCode.equals("2033")){
				aceRecPack=new ACE2033(new String(recPackLen)+new String(recPack));
			}else if(aceSendPack.transCode.equals("2035")){
				aceRecPack=new ACE2035(new String(recPackLen)+new String(recPack));
			}								
			msg.set("ACERecObj", aceRecPack);						
		}catch(Exception e){
			//��ӶԺ��ĵĴ�����ȷ��ֻ����ȷ�ĳɹ����ײŷ��أ����򷵻�ʧ��			
			logger.info("�����ϵͳͨѶ����");
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
