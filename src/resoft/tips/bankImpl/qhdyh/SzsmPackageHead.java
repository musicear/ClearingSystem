package resoft.tips.bankImpl.qhdyh;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class SzsmPackageHead {
	 
	public String CreateSystemHead(String[] heder_data)
	{
		
		String strXml = null;
		int i;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("service").addAttribute("name", "");
		//sys-head
		Element header = root.addElement("sys-header");
		Element header_data = header.addElement("data").addAttribute("name", "SYS_HEAD");
		Element header_struct = header_data.addElement("struct");	
		String[] header_dataName = {"SERVICE_CODE","TRAN_CODE","TRAN_TYPE","REVERSAL_TRAN_TYPE","TRAN_MODE","SOURCE_TYPE","BRANCH_ID","USER_ID","TRAN_DATE","TRAN_TIMESTAMP","SERVER_ID","WS_ID","USER_LANG","SEQ_NO","PROGRAM_ID","AUTH_USER_ID","AUTH_PASSWORD","AUTH_FLAG","APPR_USER_ID","APPR_FLAG","SOURCE_BRANCH_NO","DEST_BRANCH_NO","MODULE_ID","MESSAGE_TYPE","MESSAGE_CODE"};
		String[] header_dataLen = {"8","4","4","4","10","2","6","30","8","9","30","30","20","22","8","30","30","1","30","1","6","6","2","4","6"};
		 
		for(i=0;i<header_dataName.length;i++){
			Element data = header_struct.addElement("data").addAttribute("name", header_dataName[i]);
			Element field = data.addElement("field").addAttribute("type", "String").addAttribute("length", header_dataLen[i]).addAttribute("encrypt-mode", "");
			field.setText(heder_data[i]);
		} 
		try {
			OutputFormat opf = OutputFormat.createPrettyPrint();   
			opf.setEncoding("utf-8");   
			opf.setTrimText(true);
			//生成XML文件   
		//	XMLWriter xmlOut = new XMLWriter(new FileOutputStream("city-dom4j.xml"), opf);   
		//	xmlOut.write(doc);   
		//	xmlOut.flush();   
		//	xmlOut.close();   

			//获取XML字符串形式   
			StringWriter writerStr = new StringWriter();   
			XMLWriter xmlw = new XMLWriter(writerStr, opf);   
			xmlw.write(doc);   
			strXml = writerStr.getBuffer().toString();   

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("str is: "+strXml);
		System.out.println("len is: "+strXml.length());
		return strXml;
	}
}
