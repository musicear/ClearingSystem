package resoft.tips.action2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

public class Process3143 implements Action {
	private static final Log logger = LogFactory.getLog(Process3143.class);
	private static Configuration conf = Configuration.getInstance();
	public int execute(Message msg) throws Exception {
		String filePath = msg.getString("批量文件");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filePath), "GBK");
		String localName = "", parentLocalName = "";
		// 包信息
		Map packParams = new HashMap();
		// 额度信息
		Map billParams = new HashMap();
		int billCount = 0;
		Stack tagStack = new Stack();
		for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				localName = reader.getLocalName();

				tagStack.push(localName);
				if (localName.equals("BatchHead3143")) {
					packParams.clear();
					billCount = 0;
				} else if (localName.equals("Bill3143")) {
					billCount++;

				}
				if (tagStack.size() > 1) {
					parentLocalName = (String) (tagStack.get(tagStack.size() - 2));
				} else {
					parentLocalName = "";
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				String text = reader.getText().trim();
				if (!text.equals("")) {
					if (parentLocalName.equals("BatchHead3143")) {
						packParams.put(localName, text);
						if (localName.equals("EntrustDate")) {
							packParams.put(localName, text);
						} else if (localName.equals("PackNo")) {
							packParams.put(localName, text);
						} else if (localName.equals("OriPackNo")) {
							packParams.put(localName, text);
						} else if (localName.equals("OriEntrustDate")) {
							packParams.put(localName, text);
						}  else if (localName.equals("AllNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("AllAmt")) {
							packParams.put(localName, Double.valueOf(text));
						}
					} else if (parentLocalName.equals("Bill3143")) {
						billParams.put(localName, text);
						if (localName.equals("OriTraNo")) {
							billParams.put(localName, text);
						} else if (localName.equals("PayDictateNo")) {
							billParams.put(localName, text);
						}else if (localName.equals("PayMsgNo")) {
							billParams.put(localName, text);
						}else if (localName.equals("PayEntrustDate")) {
							billParams.put(localName, text);
						}else if (localName.equals("PaySndBnkNo")) {
							billParams.put(localName, text);
						}else if (localName.equals("PayResult")) {
							billParams.put(localName, text);
						}else if (localName.equals("AddWord")) {
							billParams.put(localName, text);
						}else if (localName.equals("AcctDate")) {
							billParams.put(localName, text);
						}else if (localName.equals("Amt")) {
							billParams.put(localName, Double.valueOf(text));
						}
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				tagStack.pop();
				
			
				
					if (localName.equals("Bill3143")) {
						if("0".equals(billParams.get("PayResult"))){
							updateBill(billParams);
							makeReceiveFile(packParams, "0");
							logger.info("划款请求成功！......");
						}else{//失败交易			
							String sql_PayOrder = "update PayOrder C set C.PROCSTATUS = 6, C.ADDWORD = '" + billParams.get("AddWord") + "' where C.ID = " +
							"(SELECT A.ID FROM PayOrder A, PayRequest B WHERE " +
							"A.ID = B.payID and B.TraNo = '"+billParams.get("OriTraNo")+"')";
							DBUtil.executeUpdate(sql_PayOrder);
										
							makeReceiveFile(packParams, "1");
										
							logger.info("划款请求失败！......");
						}
						
					} else if (localName.equals("BatchHead3143")) {
						updatePack(packParams);
						
					} 
					
					
				
				break;
			}
		}
		return SUCCESS;
	}

	private void updatePack(Map packParams) {
		String sql = "UPDATE PayRequestPack SET RemitStatus='2',r_entrustDate = '"
			+ packParams.get("EntrustDate") + "', r_packNo='"
			+ packParams.get("PackNo") + "', r_AllNum="
			+ packParams.get("AllNum") + ", r_AllAmt="
			+ packParams.get("AllAmt") + " WHERE entrustDate = '"
			+packParams.get("OriEntrustDate")+"' and REQUESTPACKNO= '"+packParams.get("OriPackNo")+"'";
		DBUtil.executeUpdate(sql);
		
		DBUtil.executeUpdate("update PayOrderPack C set C.PROCSTATUS = 4 where C.ID= "+
				"(select A.ID from PayOrderPack A, PayRequestPack B where B.entrustDate = '"
			+packParams.get("OriEntrustDate")+"' and B.REQUESTPACKNO= '"+packParams.get("OriPackNo")+"' and" +
						" B.Paypackid = A.Id)");
	}

	private void updateBill(Map billParams) throws SQLException {
		String sql = "UPDATE PayRequest SET PayDictateNo='"
				+ billParams.get("PayDictateNo") + "', PayMsgNo='"
				+ billParams.get("PayMsgNo") + "', PayEntrustDate='"
				+ billParams.get("PayEntrustDate") + "', PaySndBnkNo='"
				+ billParams.get("PaySndBnkNo") + "', PayResult='"
				+ billParams.get("PayResult") + "', AddWord='"
				+ billParams.get("AddWord") + "', AcctDate='"
				+ billParams.get("AcctDate") + "' ,RequestAmt=" 
				+ billParams.get("Amt")+" WHERE TraNo='"+billParams.get("OriTraNo")+"'";
		DBUtil.executeUpdate(sql);	
		String sql_order = "update PayOrder C set C.PROCSTATUS = 4, C.ADDWORD='"+ billParams.get("AddWord") +"' where C.ID = " +
		"(SELECT A.ID FROM PayOrder A, PayRequest B WHERE " +
		"A.ID = B.payID and B.TraNo = '"+billParams.get("OriTraNo")+"')";
		logger.info("sql_order is: "+ sql_order);
		DBUtil.executeUpdate(sql_order);

	}
	
	private void makeReceiveFile(Map packParams , String checkStatus) throws SftpException{
		
		String path = conf.getProperty("SFTPADD", "ReceiveFile");
	
		File f = new File(path);
		if(!f.exists()){
		  f.mkdirs();
		} 
		logger.info("OriEntrustDate is: " + packParams.get("OriEntrustDate"));
		logger.info("OriPackNo is: " + packParams.get("OriPackNo"));
		String fileName = packParams.get("OriEntrustDate") + "" + packParams.get("OriPackNo") + "A" + checkStatus;
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
