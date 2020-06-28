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
		String filePath = msg.getString("批量文件");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filePath), "GBK");
		Map params = new HashMap();

		List packTags = new ArrayList() {
			{
				add("AgentBnkCode");// 代理银行行号
				add("FinOrgCode");// 财政机关代码
				add("TreCode");// 国库主体代码
				add("EntrustDate");// 委托日期
				add("PackNo");// 包流水号
				add("OriEntrustDate");// 原委托日期
				add("OriPackNo");// 原包流水号
				add("AllNum");// 总笔数
				add("AllAmt");// 总金额
				add("PayoutVouType");// 支出凭证类型
				add("PayMode");// 支付方式

			}

		};

		List detailTags = new ArrayList() {
			{
				add("OriVouNo");// 原凭证编号
				add("OriVouDate");// 原凭证日期
				add("OriTraNo");// 原交易流水号
				add("AcctDate");// 账务日期
				add("Amt");// 金额
				add("Result");// 处理结果
				add("Description");// 说明
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
					// 保存明细数据
					if (detailTags.contains(localName)) {
						params.put(localName, text);
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				String OriPackNo = (String) params.get("OriPackNo");
				
				// 读取明细信息中的RESULT字段值，判断是否成功
				if (localName.equals("BatchReturn3144")) {
					String Result = (String) params.get("Result");
					if ("90000".equals(Result)) {
                       //退款成功!
						String OriTraNo = (String) params.get("OriTraNo");
						String sql = "update RefundInfo set RefundStatus = '2',ProcStatus = '4',Description='"+(String) params.get("Description")+"' where TraNo = '"
								+ OriTraNo + "'";
						
						DBUtil.executeUpdate(sql);
						
						makeReceiveFile(params,"0");
						logger.info("支付退款请求成功！......");
					}  else {
						// 如果退款失败，更新退款表状态为‘3’失败
						String OriTraNo = (String) params.get("OriTraNo");
						String sql = "update RefundInfo set RefundStatus = '3',ProcStatus = '6',Description='"+(String) params.get("Description")+"' where TraNo = '"
								+ OriTraNo + "'";
						DBUtil.executeUpdate(sql);
						// 如果退款失败，更新支付令明细表处理状态为‘6’退款失败
						DBUtil.executeUpdate(sql);
						String sqls = "update PayOrderDetail set PROCSTATUS='6' where id in (select id from Refunddetail where Payid = (select id from RefundInfo where TraNo='"
								+ OriTraNo + "'))";
						DBUtil.executeUpdate(sqls);
						
						makeReceiveFile(params,"1");
						
						logger.info("支付退款请求失败！失败原因：" + Result);
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
