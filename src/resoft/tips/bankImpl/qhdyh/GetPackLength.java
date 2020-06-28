package resoft.tips.bankImpl.qhdyh;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class GetPackLength {
    public static String getLength(Document doc)
    {
    	String strXml="";
		try { 
			OutputFormat opf = OutputFormat.createPrettyPrint();   
			opf.setEncoding("utf-8");   
			opf.setTrimText(true);
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
		return baowenlength+strXml;	 
    }
}
