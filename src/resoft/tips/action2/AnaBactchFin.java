package resoft.tips.action2;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.util.IDUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 保存付款申请到数据库
 * @author 
 *
 */
public class AnaBactchFin implements Action {
    
	private static final Log logger = LogFactory.getLog(AnaBactchFin.class);
    Configuration conf = Configuration.getInstance();
    private String saveFile = null;
    
    
	public int execute(Message msg) throws Exception {
		
		saveFile = conf.getProperty("SFTPADD", "SaveFile");
		File dir = new File(saveFile);
		String[] ziplist = dir.list();
		for (String name : ziplist){
			int length = name.trim().length();
			String transNo = name.substring(length-1,length);
			if("A".equals(transNo)){
				try{
					Ana2201(saveFile + name);
				}finally{
					moveFolder(saveFile+name, conf.getProperty("SFTPADD", "NFile"));
				}
			}else{
				try{
					Ana2202(saveFile + name);
				}finally{
					moveFolder(saveFile+name, conf.getProperty("SFTPADD", "NFile"));
				}
			}
				
		}
		
		return 0;
	}

	public void Ana2201(String filePath) throws Exception{
		
		int result=0;
		IDUtil util=IDUtil.getInstance();
		Long payOrderPackID=new Long(util.getMaxIDFromPayOrderPack());
		Long payOrderID=null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream inputStream = new FileInputStream(filePath);
		XMLStreamReader reader = factory.createXMLStreamReader(inputStream, "GBK");
		String localName = "", parentLocalName = "";
		String finOrgCode = "", entrustDate = "", packNo = "", traNo = "";
		// 包信息
		Map packParams = new HashMap();
		// 付款申请信息
		Map billParams = new HashMap();
		// 付款申请明细信息
		Map detailParams = new HashMap();
		Stack tagStack = new Stack();
		int billCount = 0, detailCount = 0;
		for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader
				.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				localName = reader.getLocalName();
				
				tagStack.push(localName);
				if (localName.equals("BatchHead2201")) {
					packParams.clear();
					billCount = 0;
				} else if (localName.equals("Bill2201")) {
					billParams.clear();
					payOrderID=new Long(util.getMaxIDFromPayOrder());
					billCount++;
					detailCount = 0;
				} else if (localName.equals("Detail2201")) {
					detailParams.clear();
					detailCount++;
				}
				if (tagStack.size() > 1) {
					parentLocalName = (String) (tagStack
							.get(tagStack.size() - 2));
				} else {
					parentLocalName = "";
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				String text = reader.getText().trim();
				if (!text.equals("")) {
					if (parentLocalName.equals("BatchHead2201")) {
						packParams.put(localName, text);
						if (localName.equals("FinOrgCode")) {
							finOrgCode = text;
						} else if (localName.equals("EntrustDate")) {
							entrustDate = text;
						} else if (localName.equals("PackNo")) {
							packNo = text;
						}else if (localName.equals("AllNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} /*else if (localName.equals("AllAmt")) {
							packParams.put(localName, Double.valueOf(text));
						} */
					} else if (parentLocalName.equals("Bill2201")) {
						billParams.put(localName, text);
						if (localName.equals("TraNo")) {
							traNo = text;
						} /*else if (localName.equals("Amt")) {
							billParams.put(localName, Double.valueOf(text));
						} */else if (localName.equals("StatInfNum")) {
							billParams.put(localName, Integer.valueOf(text));
						}
					} else if (parentLocalName.equals("Detail2201")) {
						detailParams.put(localName, text);
						if (localName.equals("SeqNo")) {
							detailParams.put(localName, Integer.valueOf(text));
						} /*else if (localName.equals("Amt")) {
							detailParams.put(localName, Double.valueOf(text));
						} */else if (localName.equals("EcnomicSubjectCode")) {
							detailParams.remove(localName);
							detailParams.put("EconSubjectCode", text);
						}
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				tagStack.pop();
				boolean checkStatus = checkInfo(packNo,entrustDate,0);
				if (localName.equals("Detail2201")) {
					Long detailID=new Long(util.getMaxIDFromPayOrderDetail());
					detailParams.put("ID",detailID);
					detailParams.put("payID",payOrderID);
					detailParams.put("FinOrgCode", finOrgCode);
					detailParams.put("entrustDate", entrustDate);
					detailParams.put("packNo", packNo);
					detailParams.put("TraNo", traNo);
					detailParams.put("procstatus", "2");
					//查看是否重复
					
						DBUtil.insert("PayOrderDetail", detailParams);
				} else if (localName.equals("Bill2201")) {
					billParams.put("ID",payOrderID);
					billParams.put("PackID",payOrderPackID);
					billParams.put("entrustDate", entrustDate);
					billParams.put("packNo", packNo);
					billParams.put("procstatus", "2");
					//查看是否重复
					
						DBUtil.insert("PayOrder", billParams);
				} else if (localName.equals("BatchHead2201")) {
					// TODO: set status.
					packParams.put("ID",payOrderPackID);
					packParams.put("procstatus", "2");
					packParams.put("Returnstatus","0");
					packParams.put("Checkstatus","0");
					//查看是否重复
					
						DBUtil.insert("PayOrderPack", packParams);
				} 
				break;
			}
		}
		if(null != inputStream)
			inputStream.close();
	}
	
	
	
	public void Ana2202(String filePath) throws Exception{
			
			int result=0;
			IDUtil util=IDUtil.getInstance();
			Long ReFundPackID=new Long(util.getMaxIDFromRefundPack());
			Long ReFundInfoID=null;
			XMLInputFactory factory = XMLInputFactory.newInstance();
			FileInputStream inputStream = new FileInputStream(filePath);
			XMLStreamReader reader = factory.createXMLStreamReader(inputStream, "GBK");
			String localName = "", parentLocalName = "";
			String finOrgCode = "", entrustDate = "", packNo = "", traNo = "";
			// 包信息
			Map packParams = new HashMap();
			// 付款申请信息
			Map billParams = new HashMap();
			// 付款申请明细信息
			Map detailParams = new HashMap();
			Stack tagStack = new Stack();
			int billCount = 0, detailCount = 0;
			for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader
					.next()) {
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					localName = reader.getLocalName();
	
					tagStack.push(localName);
					if (localName.equals("Head2202")) {
						packParams.clear();
						billCount = 0;
					} else if (localName.equals("Bill2202")) {
						billParams.clear();
						ReFundInfoID=new Long(util.getMaxIDFromRefundinfo());
						billCount++;
						detailCount = 0;
					} else if (localName.equals("Detail2202")) {
						detailParams.clear();
						detailCount++;
					}
					if (tagStack.size() > 1) {
						parentLocalName = (String) (tagStack
								.get(tagStack.size() - 2));
					} else {
						parentLocalName = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					String text = reader.getText().trim();
					if (!text.equals("")) {
						if (parentLocalName.equals("Head2202")) {
							packParams.put(localName, text);
							if (localName.equals("FinOrgCode")) {
								finOrgCode = text;
							} else if (localName.equals("EntrustDate")) {
								entrustDate = text;
							} else if (localName.equals("PackNo")) {
								packNo = text;
							}else if (localName.equals("AllNum")) {
								packParams.put(localName, Integer.valueOf(text));
							} /*else if (localName.equals("AllAmt")) {
								packParams.put(localName, Double.valueOf(text));
							} */
						} else if (parentLocalName.equals("Bill2202")) {
							billParams.put(localName, text);
							if (localName.equals("TraNo")) {
								traNo = text;
							} /*else if (localName.equals("Amt")) {
								billParams.put(localName, Double.valueOf(text));
							} */else if (localName.equals("StatInfNum")) {
								billParams.put(localName, Integer.valueOf(text));
							}
						} else if (parentLocalName.equals("Detail2202")) {
							detailParams.put(localName, text);
							if (localName.equals("SeqNo")) {
								detailParams.put(localName, Integer.valueOf(text));
							} /*else if (localName.equals("Amt")) {
								detailParams.put(localName, Double.valueOf(text));
							} */else if (localName.equals("EcnomicSubjectCode")) {
								detailParams.remove(localName);
								detailParams.put("EconSubjectCode", text);
							}
						}
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					localName = reader.getLocalName();
					tagStack.pop();
					//boolean checkStatus = checkInfo(packNo,entrustDate,1);
					if (localName.equals("Detail2202")) {
						Long detailID=new Long(util.getMaxIDFromRefundDetail());
						logger.info("Detail ID is:" + detailID);
						detailParams.put("ID",detailID);
						detailParams.put("payID",ReFundInfoID);
						detailParams.put("FinOrgCode", finOrgCode);
						detailParams.put("entrustDate", entrustDate);
						detailParams.put("packNo", packNo);
						detailParams.put("TraNo", traNo);
						detailParams.put("procstatus", "2");
						//查看是否重复
						
							DBUtil.insert("RefundDetail", detailParams);
					} else if (localName.equals("Bill2202")) {
						billParams.put("ID",ReFundInfoID);
						logger.info("Bill2202 ID is:" + ReFundInfoID);
						billParams.put("PackID",ReFundPackID);
						billParams.put("procstatus", "2");
						billParams.put("entrustDate", entrustDate);
						billParams.put("packNo", packNo);
						//查看是否重复
						
							DBUtil.insert("RefundInfo", billParams);
					} else if (localName.equals("Head2202")) {
						// TODO: set status.
						packParams.put("ID",ReFundPackID);
						logger.info("Head2202 ID is:" + ReFundPackID);
						packParams.put("procstatus", "2");
						packParams.put("Returnstatus","0");
						packParams.put("Checkstatus","0");
						//查看是否重复
						
							DBUtil.insert("RefundPack", packParams);
					} 
					break;
				}
			}
			if(null != inputStream)
				inputStream.close();
			
			
		}
	
	private void moveFolder(String src, String dest) throws InterruptedException {

		  File srcFolder = new File(src);
		  File destFolder = new File(dest);
		  File newFile = new File(dest + srcFolder.getName());
		  boolean fileStatus = srcFolder.renameTo(newFile);
		  while(false == fileStatus){
			  System.gc();
			  //Thread.sleep(10000);
			  fileStatus = srcFolder.renameTo(newFile);
		  }
	}
	
	private boolean checkInfo(String PackNo, String EntrustDate,int Type){
		
		String sql_check = "";
		if(Type == 0){
			sql_check = "SELECT packNo from PayOrderPack where PackNo = '" + PackNo + "' and entrustDate = '" +EntrustDate+"'";
		}else{
			sql_check = "SELECT packNo from RefundPack where PackNo = '" + PackNo + "' and entrustDate = '" +EntrustDate+"'";
		}
		List result = null;
		try {
			result = QueryUtil.queryRowSet(sql_check);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.size()>0)
			return true;
		else
			return false;
	}
}
