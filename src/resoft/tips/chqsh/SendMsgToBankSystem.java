package resoft.tips.chqsh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
/**
 * <p>���̨tuxedo���������������ˡ����ʺͲ�ѯ��������</p>
 * Author: fanchengwei
 * Date  : 2007-10-10
 * Time  : 10:12:34
 * ע��Ҫ�㣺û����ȷ�ĳɹ��𸴾��Բ����Է��سɹ������������Ϊ��������
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
			logger.info("������������У��TUXEDO����:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("������������У��TUXEDOӦ��["+rcvMsg+"]");
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
	 * У����˰�˵�����
	 * @param SQL	����˰�˿��ţ�����
	 * @return		��У�鱨��
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
			logger.info("��������У��TUXEDO����:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("��������У��TUXEDOӦ��["+rcvMsg+"]");
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
	 * @param SQL		����ѯ���
	 * @param TL_TD		��������Ա
	 * @return			����ѯ���
	 */
	public String sendMsg(String SQL,String TL_TD){
		/**
		 * ��ѯ����
		 * ͨ������SQL���ݹ����Ĳ�ѯ������������̬����onlyone����װ�γɷ��͵�tuxedo�Ĺ淶������
		 * ���ĸ�ʽ������ͷ+data1+data2����ʽ�ɡ�С��֧�����˽��׽ӿڡ������´����ĺͲ�ѯ�˻�״̬���壩
		 * ����ͷ����TuxedoPackHeadInit���еľ�̬����SQLHead��ɣ����и���Ԫ�ظ������С�С��֧�����˽��׽ӿڡ���
		 * ���´����Ķ��塣�������������ⶼ�Ƕ�ֵ��
		 * data1��20���ַ���ɵ��ļ�����ʵ�ʳ�����û���õ�����ļ����ƣ�
		 */
		try{	
			sendMsg = onlyone.getSqlPackager(SQL, TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*10000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("����SQL��ѯTUXEDO����:["+sendMsg+"]");
			byte[] recPack=new byte[2048];			
			in.read(recPack,0,2048);
			rcvMsg = new String(recPack);
			logger.info("����SQL��ѯTUXEDOӦ��["+rcvMsg+"]");
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
	 * ���˽���
	 * @param PayAcct	����˰���ʺ�
	 * @param TraAmt	�����
	 * @param TaxTypeName:˰����Ϣ
	 * @param TL_TD		��������Ա
	 * @return			�����˽��
	 */
	//yangyuanxu update
	public synchronized String sendInMsg(String PayAcct,String TraAmt,String TaxTypeName,String PayeeBankNo,String TL_TD){
		/**
		 * ���˱���
		 * ���Ľṹ������ͷ+data1+data2
		 * ����ͷ�����ݸ����˻��Ĳ�ͬ����ͬ�����ݡ�������Ͻӿڡ��еĶ�����TuxedoPackHeadInit�ж���Ϊ��̬������
		 * data1�������˽��пۿ���ʺš��ۿ���ʺš��ۿ��
		 * data2��ͨ�á�����ת�ڲ����㡱�й涨�ĸ�ʽ��д��
		 */
		if(Double.parseDouble(TraAmt) < 0){
			return rcvMsg;
		}
		try{	
			sendMsg = onlyone.getInPackager(PayAcct, TraAmt, PayeeBankNo, "����"+TaxTypeName, TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//���ͷ���ʶ
			out.write(sendMsg.getBytes());
			logger.info("���ͼ���TUXEDO����:["+sendMsg+"]");
			out.flush();
			byte[] recPack=new byte[85];			
			in.read(recPack,0,85);
			rcvMsg = new String(recPack);
			logger.info("���ռ���TUXEDOӦ��["+rcvMsg+"]");
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
	 * Ĩ�˽���
	 * @param JRN_NO	����־��
	 * @param VCH_NO	����Ʊ��
	 * @param TR_CODE	��������
	 * @param TL_TD		��������Ա
	 * @return			�����ʽ��
	 */
	public String sendOutMsg(String JRN_NO,String VCH_NO,String TR_CODE,String TL_TD){
		/**
		 * Ĩ�˱���
		 * ���ĸ�ʽ������ͷ+data1
		 * ����ͷ��ͬ���˽��ױ���ͷ��ʽ
		 * data1:�����������Ҫ�Ľ�����־�š���Ʊ�š�������͹�Ա�ţ���Ա�����⣩
		 * ��������־�š���Ʊ�š������롢��Ա��
		 */
		try{	
			sendMsg = onlyone.getOutPackager(JRN_NO,VCH_NO,TR_CODE,TL_TD);
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			//���ͷ���ʶ
			out.write(sendMsg.getBytes());
			out.flush();
			logger.info("����Ĩ��TUXEDO����:["+sendMsg+"]");
			//���ջ�ִ
			DataInputStream in=new DataInputStream(sender.getInputStream());
			byte[] recPack=new byte[85];			
			in.read(recPack,0,85);
			rcvMsg = new String(recPack);
			logger.info("����Ĩ��TUXEDOӦ��["+rcvMsg+"]");
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



