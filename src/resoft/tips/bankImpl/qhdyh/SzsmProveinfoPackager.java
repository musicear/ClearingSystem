package resoft.tips.bankImpl.qhdyh;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SzsmProveinfoPackager
{
	private static final Log logger = LogFactory.getLog(SzsmProveinfoPackager.class);
	String[] header_dataName = {"SERVICE_CODE","TRAN_CODE","TRAN_TYPE","REVERSAL_TRAN_TYPE","TRAN_MODE","SOURCE_TYPE","BRANCH_ID","USER_ID","TRAN_DATE","TRAN_TIMESTAMP","SERVER_ID","WS_ID","USER_LANG","SEQ_NO","PROGRAM_ID","AUTH_USER_ID","AUTH_PASSWORD","AUTH_FLAG","APPR_USER_ID","APPR_FLAG","SOURCE_BRANCH_NO","DEST_BRANCH_NO","MODULE_ID","MESSAGE_TYPE","MESSAGE_CODE"};
	String[] header_dataLen = {"12","4","4","4","10","2","6","30","8","9","30","30","20","16","8","30","30","1","30","1","6","6","2","4","6"};
	String[] header_data=new String[25];
	String[] body_dataName={"PAYACCT","TAXORGCODE","PROTOCOLNO","TAXPAYNAME","TAXPAYCODE"};
	String[] body_dataLen={"50","12","60","200","20"};
	String[] body_data=new String[5];
	String[] app_header_dataName = {"PGUP_OR_PGDN","TOTAL_NUM","CURRENT_NUM","PAGE_START","PAGE_END"};
	String[] app_header_dataLen = {"1","10","10","10","10"};
	String[] app_heder_data = new String[5];
	String[] local_header_dataName = {"RET","RET_CODE","RET_MSG"};
	String[] local_header_dataLen = {"100","6","512"};
	Object[] local_headerr_data = new Object[3];
	
    boolean foundnode=false;
	public void CreateSystemHeadData(String msgcode,String WorkDate)
	{
		DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
    	String traDateTime=dtFormat.format(new Date());
    	String traDate=traDateTime.substring(0, 8);
    	String traTime=traDateTime.substring(8, traDateTime.length());
    	header_data[0]="SVR_SIGN";
    	header_data[1]="3121";
    	header_data[2]="TI01";
    	header_data[4]="ONLINE";
    	header_data[5]="TI";
    	header_data[6]="60001";    
    	header_data[7]="QDTIPS";//待商定 
    	header_data[8]=WorkDate;
    	header_data[9]=traTime+"000";
    	header_data[10]="127.0.0.1"; 
    	header_data[12]="CHINESE";
    	header_data[13]=traDateTime+"00";//渠道流水号
  
    	header_data[22]="RB";//模块标示
    	header_data[23]="1220";//报文类型
    	header_data[24]=msgcode;//报文代码
	}
	
	public void CreateAppHeadData()
	{
		
	}
	
	public void CreateLocalHeadData()
	{}
	
	public void CreateBodyData(String taxOrgCode, String taxPayCode,
			String payAcct, String protocolNo, String taxPayName)
	{
		body_data[0]=payAcct;
		body_data[1]=taxOrgCode;
		body_data[2]=protocolNo;
		body_data[3]=taxPayName;
		body_data[4]=taxPayCode;
		
	}
	
	public String MakeUpCheckinfoPackager() {
		
		String strXml = null;
		int i;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("service").addAttribute("name", "");
		//sys-head
		Element header = root.addElement("sys-header");
		Element headerdata = header.addElement("data").addAttribute("name", "SYS_HEAD");
		Element header_struct = headerdata.addElement("struct");	
		 
		for(i=0;i<header_dataName.length;i++){
			Element data = header_struct.addElement("data").addAttribute("name", header_dataName[i]);
			Element field = data.addElement("field").addAttribute("type", "string").addAttribute("length", header_dataLen[i]).addAttribute("encrypt-mode", "");
			if(header_data[i]!=null)
				field.setText(header_data[i]);
			else
				field.setText("");
		}
		 
		//body 做成可配置读取的形式 根据不同交易
		Element body = root.addElement("body");

		for(i=0;i<body_dataName.length;i++){
			Element data = body.addElement("data").addAttribute("name", body_dataName[i]);
			Element field = data.addElement("field").addAttribute("type", "string").addAttribute("length", body_dataLen[i]).addAttribute("encrypt-mode", "");
			//field.setText(body_data[i]);
			
			if(body_data[i]!=null)
				field.setText(body_data[i]);
			else
				field.setText("");
		}
		/*try {
			OutputFormat opf = OutputFormat.createPrettyPrint();   
			opf.setEncoding("utf-8");   
			//opf.setTrimText(true);
			//生成XML文件   
			XMLWriter xmlOut = new XMLWriter(new FileOutputStream("city-dom4j.xml"), opf);   
			xmlOut.write(doc);   
			xmlOut.flush();   
			xmlOut.close();   

			//获取XML字符串形式   
			StringWriter writerStr = new StringWriter();   
			XMLWriter xmlw = new XMLWriter(writerStr, opf);   
			xmlw.write(doc);   
			strXml = writerStr.getBuffer().toString();   

		} catch (IOException e) {
			e.printStackTrace();
		} */
		return GetPackLength.getLength(doc);
		
		// TODO Auto-generated method stub
		
	}
	public String revXml;
	public String GetParm(String local,String parm) {
		// TODO Auto-generated method stub
		foundnode=false;
		SAXReader reader = new SAXReader(); 
		Document document; 
		try 
		{ 
			document = DocumentHelper.parseText(revXml); 
			Element root = document.getRootElement(); 
			//信息条数； 
			if(local.equalsIgnoreCase("sys_header"))
			{
				Element ele=root.element("sys-header");
				Element dataele=ele.element("data"); 
				 
				Element structele=dataele.element("struct");
				Element arrayele=dataele.element("array"); 
				if(structele!=null&&!foundnode)
				{
					return IteratorStructNode(structele,parm);
				}
				if(arrayele!=null&&!foundnode)
				{
					return IteratorArraytNode(arrayele,parm);
				}  
			}
			//Iterator it =root.elements().iterator(); 
			if(local.equalsIgnoreCase("body"))
			{
				Element ele=root.element(local); 
				
				return IteratorStructNode(ele,parm);  
			}
		} catch (DocumentException e1) 
		{ 
			e1.printStackTrace(); 
			return "";
		} 
		return "";
	}

	private String IteratorArraytNode(Element ele, String parm) {
		// TODO Auto-generated method stub
		 
		Element structele=ele.element("struct");
		Element arrayele=ele.element("array"); 
		if(structele!=null&&!foundnode)
		{
			return IteratorStructNode(structele,parm);
		}
		if(arrayele!=null&&!foundnode)
		{
			return IteratorArraytNode(arrayele,parm);
		} 
		return "";
	}

	private String IteratorStructNode(Element structele, String parm) {
		// TODO Auto-generated method stub
		//Element dataele=structele.element("data"); 
		if(structele==null)
		{
			logger.info("is null");
		}
		else
		{
			logger.info("not null:"+structele.getName());
			List eles=structele.elements();
			if(eles==null)
			{logger.info("no child");}
		}
		Iterator it =structele.elements().iterator(); 
		String result="";
		while(it.hasNext()&&!foundnode) 
		{ 
			Element info=(Element) it.next();  
			result = IteratordateNode(info,parm);
		}
		return result;
	}
	
	private String IteratordateNode(Element dataele, String parm)
	{
		if(dataele.attributeValue("name").equals(parm))
		{
			foundnode=true;
			return dataele.elementText("field");
		}
		Element structele=dataele.element("struct");
		Element arrayele=dataele.element("array");
		Element fieldele=dataele.element("field");
		if(structele!=null&&!foundnode)
		{
			return IteratorStructNode(structele,parm);
		}
		if(arrayele!=null&&!foundnode)
		{
			return IteratorArraytNode(arrayele,parm);
		} 
		return "";
	}

}