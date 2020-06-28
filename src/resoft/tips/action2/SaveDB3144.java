package resoft.tips.action2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class SaveDB3144 implements Action {

	private static final Log logger = LogFactory.getLog(SaveDB3144.class);
	private static Configuration conf = Configuration.getInstance();
	public int execute(Message msg) throws Exception {
		String filePath = msg.getString("�����ļ�");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filePath), "GBK");
		Map params = new HashMap();

		List packTags = new ArrayList() {
			{
				add("AgentBnkCode");// ���������к�
				add("FinOrgCode");// �������ش���
				add("TreCode");// �����������
				add("EntrustDate");// ί������
				add("PackNo");// ����ˮ��
				add("OriEntrustDate");// ԭί������
				add("OriPackNo");// ԭ����ˮ��
				add("AllNum");// �ܱ���
				add("AllAmt");// �ܽ��
				add("PayoutVouType");// ֧��ƾ֤����
				add("PayMode");// ֧����ʽ

			}

		};

		List detailTags = new ArrayList() {
			{
				add("OriVouNo");// ԭƾ֤���
				add("OriVouDate");// ԭƾ֤����
				add("OriTraNo");// ԭ������ˮ��
				add("AcctDate");// ��������
				add("Amt");// ���
				add("Result");// ������
				add("Description");// ˵��
			}
		};

		String localName = "";
		
		//String sql_Pack = "update RefundPack set ProcStatus = '4'";
	
		//DBUtil.executeUpdate(sql_Pack);
		
		for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader
				.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				localName = reader.getLocalName();
				break;
			case XMLStreamConstants.CHARACTERS:
				String text = reader.getText().trim();
				if (!text.equals("")) {
					if (packTags.contains(localName)) {
						params.put(localName, text);
					}
					// ������ϸ����
					if (detailTags.contains(localName)) {
						params.put(localName, text);
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				String OriPackNo = (String) params.get("OriPackNo");
				
				// ��ȡ��ϸ��Ϣ�е�RESULT�ֶ�ֵ���ж��Ƿ�ɹ�
				if (localName.equals("BatchReturn3144")) {
					String Result = (String) params.get("Result");
					if ("90000".equals(Result)) {
                       //�˿�ɹ�!
						String OriTraNo = (String) params.get("OriTraNo");
						String sql = "update RefundInfo set RefundStatus = '2',ProcStatus = '4',Description='"+(String) params.get("Description")+"' where TraNo = '"
								+ OriTraNo + "'";
						
						DBUtil.executeUpdate(sql);
						
						makeReceiveFile(params,"0");
						logger.info("֧���˿�����ɹ���......");
					}  else {
						// ����˿�ʧ�ܣ������˿��״̬Ϊ��3��ʧ��
						String OriTraNo = (String) params.get("OriTraNo");
						String sql = "update RefundInfo set RefundStatus = '3',ProcStatus = '6',Description='"+(String) params.get("Description")+"' where TraNo = '"
								+ OriTraNo + "'";
						DBUtil.executeUpdate(sql);
						// ����˿�ʧ�ܣ�����֧������ϸ����״̬Ϊ��6���˿�ʧ��
						DBUtil.executeUpdate(sql);
						String sqls = "update PayOrderDetail set PROCSTATUS='6' where id in (select id from Refunddetail where Payid = (select id from RefundInfo where TraNo='"
								+ OriTraNo + "'))";
						DBUtil.executeUpdate(sqls);
						
						makeReceiveFile(params,"1");
						
						logger.info("֧���˿�����ʧ�ܣ�ʧ��ԭ��" + Result);
					}
				}
				
				break;
			}
		}

		msg.set("packFields", params);

		return 0;
	}
	
	private void makeReceiveFile(Map packParams,String checkStatus){
		
		String path = conf.getProperty("SFTPADD", "ReceiveFile");
	
		File f = new File(path);
		if(!f.exists()){
		  f.mkdirs();
		} 

		String fileName = packParams.get("OriEntrustDate").toString().trim() + packParams.get("OriPackNo").toString().trim() + "B" + checkStatus;
		File file = new File(f,fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		//sendToSftp(fileName);
	}
	private void sendToSftp(String fileName) {
		 String host = null;
		 String username = null;
		 String password = null;
		 int port = 22;
		 String localPath = conf.getProperty("SFTPADD", "ReceiveFile");
		 String uploadPath = conf.getProperty("SFTPADD", "UploadDir");
		
		 host=conf.getProperty("SFTPADD", "SftpHost");
		 username=conf.getProperty("SFTPADD", "Username");
		 password=conf.getProperty("SFTPADD", "Password");
		 port =Integer.parseInt(conf.getProperty("SFTPADD", "SftpPort"));
		 
		 FtpImpl sftpImpl = new FtpImpl();
		 sftpImpl.connect(host, username, password, port);
		
		 try {
			sftpImpl.uploadFile(fileName, localPath, uploadPath);
			sftpImpl.disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
	}

}
