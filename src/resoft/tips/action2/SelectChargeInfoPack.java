package resoft.tips.action2;
 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


public class SelectChargeInfoPack{
	private static final Log logger = LogFactory.getLog(SelectChargeInfoPack.class);
	String[] header_dataName = {"RET_STATUS","BRANCH_ID","SOURCE_BRANCH_NO","DEST_BRANCH_NO","MESSAGE_TYPE","MESSAGE_CODE","TRAN_DATE","TRAN_TIMESTAMP","RET","SEQ_NO","SERVICE_CODE"};
	String[] header_dataLen = {"1","6","6","6","4","6","8","9","","16","12"};
	String[] header_data=new String[11];
	
	String[] bodyArrayname={"BANKTRADATE","TRANO","TRAAMT","TAXPAYCODE","TAXNAME","PRINTCOUNT","ACCT_NAME","TAXORGCODE"};
	String[] bodydateLen={"20","30","20","20","100","5","100","12"};
	String[] bodydataType={"string","string","string","string","string","double","string","string"};
	public String[][] body_data;
	String ret_code;
	String ret_msg;
	public void CreateSystemHeadData(String RET_STATUS,String BRANCH_ID,String SOURCE_BRANCH_NO,String DEST_BRANCH_NO,String MESSAGE_TYPE,String MESSAGE_CODE,String TRAN_DATE,String TRAN_TIMESTAMP,String  RET_CODE,String RET_MSG,String SEQ_NO,String SERVICE_CODE )
	{
		DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmssSSS");
    	String traDateTime=dtFormat.format(new Date());
    	String traDate=traDateTime.substring(0, 8);
    	String traTime=traDateTime.substring(8, traDateTime.length());
    	header_data[0]=RET_STATUS;
    	header_data[1]=BRANCH_ID;
    	header_data[2]=SOURCE_BRANCH_NO;
    	header_data[3]=DEST_BRANCH_NO;
    	header_data[4]=MESSAGE_TYPE;
    	header_data[5]=MESSAGE_CODE; 
    	header_data[6]=TRAN_DATE; 
    	header_data[7]=traTime;
    	header_data[9]=SEQ_NO;
    	header_data[10]=SERVICE_CODE;  
    	ret_code=RET_CODE;
    	ret_msg=RET_MSG;
	}
	
	public void CreateBodyData(int index,String BANKTRADATE, String TRANO,String TRAAMT,String TAXPAYCODE, String TAXNAME,String PRINTCOUNT,String ACCT_NAME,String TAXORGCODE)
	{
		body_data[index][0]=BANKTRADATE;
		body_data[index][1]=TRANO;
		body_data[index][2]=TRAAMT;  
		body_data[index][3]=TAXPAYCODE;
		body_data[index][4]=TAXNAME;
		body_data[index][5]=PRINTCOUNT; 
		body_data[index][6]=ACCT_NAME; 
		body_data[index][7]=TAXORGCODE; 
		
	}
	public String MakeUpCheckPackager(String  RET_STATUS) {
		
		String strXml = null; 
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("service").addAttribute("name", "");
		//sys-head
		Element header = root.addElement("sys-header");
		Element headerdata = header.addElement("data").addAttribute("name", "SYS_HEAD");
		Element header_struct = headerdata.addElement("struct");	
		 
		for(int i=0;i<header_dataName.length;i++){
			
			if(header_dataName[i].equals("RET"))
			{
				Element data = header_struct.addElement("data").addAttribute("name", header_dataName[i]);
				Element struct=data.addElement("array").addElement("struct");
				
				Element retcodedata = struct.addElement("data").addAttribute("name", "RET_CODE");
				Element retcodedele=retcodedata.addElement("field").addAttribute("scale", "0").addAttribute("type", "string").addAttribute("length", "6");
				retcodedele.setText(ret_code);
				Element retmsgdata = struct.addElement("data").addAttribute("name", "RET_MSG");
				Element retmsgele=retmsgdata.addElement("field").addAttribute("scale", "0").addAttribute("type", "string").addAttribute("length", "512");
				retmsgele.setText(ret_msg);
			}
			else
			{
				Element data = header_struct.addElement("data").addAttribute("name", header_dataName[i]);
				Element field = data.addElement("field").addAttribute("type", "string").addAttribute("length", header_dataLen[i]).addAttribute("encrypt-mode", "");
				if(header_data[i]!=null)
					field.setText(header_data[i]);
				else
					field.setText("");
			}
		}
		 
		//body 做成可配置读取的形式 根据不同交易
		Element body = root.addElement("body");
		if(RET_STATUS=="S")
		 {
		Element data = body.addElement("data").addAttribute("name", "TRAN_ARRAY");
		Element array=data.addElement("array");  
		}
		try {
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
	        logger.info("组成报文strXml is : "+strXml);

		} catch (IOException e) {
			e.printStackTrace();
		} 
		String baowenlength="0";
		try
		{
		    baowenlength=strXml.getBytes("UTF-8").length+"";
		}
		catch(Exception e)
		{}
		while(baowenlength.length()<10)
		{
			baowenlength="0"+baowenlength;
		}
		return strXml;	 
		// TODO Auto-generated method stub
		
	} 
}