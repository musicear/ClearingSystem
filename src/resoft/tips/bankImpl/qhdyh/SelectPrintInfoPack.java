package resoft.tips.bankImpl.qhdyh;
 
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

public class SelectPrintInfoPack{
	private static final Log logger = LogFactory.getLog(SelectPrintInfoPack.class);
	String[] header_dataName = {"RET_STATUS","BRANCH_ID","SOURCE_BRANCH_NO","DEST_BRANCH_NO","MESSAGE_TYPE","MESSAGE_CODE","TRAN_DATE","TRAN_TIMESTAMP","RET","SEQ_NO","SERVICE_CODE"};
	String[] header_dataLen = {"1","6","6","6","4","6","8","9","","16","12"};
	String[] header_data=new String[11];
	
	String[] bodyArray={"ENTRUSTDATE","TRAN_ARRAY","TRAAMT","TAXPAYCODE","TAXPAYNAME","PAYEEACCT","HANDORGNAME","PAYOPBKCODE","TAX_NAME","TIPS_SEQ","PRINTEGERTIMES","PAYEENAME","TAXVOUNO"};
	String[] bodydateLen={"30","","20","20","100","50","100","100","100","30","5","100","18"};
	String[] bodydataType={"string","","double","string","string","string","string","string","string","string","double","string","string"};
	String[] bodydate=new String[13]; 
	String ret_code;
	String ret_msg;
	private String[] bodyArrayname={"TAX_TYPE_NAME","TAX_TIME","TAX_AMT"};
	private String[] bodyarrattype={"string","string","string"};
	public String[][] bodyarray_data;
	private String[] bodyarray_datalen={"100","30","20"};
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
	
	public void CreateBodyData(String ENTRUSTDATE, String TRAAMT,String TAXPAYCODE,String TAXPAYNAME, String PAYEEACCT,String HANDORGNAME,String PAYOPBKCODE,String TAX_NAME,String TIPS_SEQ,String PRINTEGERTIMES,String payeename,String TaxVouNo)
	{
		logger.info(ENTRUSTDATE+"   "+TRAAMT+"   "+TAXPAYCODE+"   "+TAXPAYNAME+"   "+PAYEEACCT+"   "+HANDORGNAME+"   "+PAYOPBKCODE+"   "+TAX_NAME+"   "+TIPS_SEQ+"    "+PRINTEGERTIMES);
		
		bodydate[0]=ENTRUSTDATE;
		bodydate[2]=TRAAMT;
		bodydate[3]=TAXPAYCODE;
		bodydate[4]=TAXPAYNAME;
		bodydate[5]=PAYEEACCT;
		bodydate[6]=HANDORGNAME;
		bodydate[7]=PAYOPBKCODE;
		bodydate[8]=TAX_NAME;
		bodydate[9]=TIPS_SEQ;
		bodydate[10]=PRINTEGERTIMES;
		bodydate[11]=payeename;
		bodydate[12]=TaxVouNo;
	}
	public String MakeUpCheckPackager(String RET_STATUE) {
		
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
		if(RET_STATUE=="S")
		{	
			logger.info("RET_STATUE        "+RET_STATUE);
			for(int i=0;i<bodydate.length;i++)
			{ 
				   Element data = body.addElement("data").addAttribute("name", bodyArray[i]);
					 
					if(bodyArray[i].equals("TRAN_ARRAY"))
					{
						Element array=data.addElement("array");  
							  
								for(int j=0;j<bodyarray_data.length;j++){ 
									Element struct=array.addElement("struct"); 
									String[] bodyarray_dataarray=bodyarray_data[j];
									for(int counti=0;counti<bodyArrayname.length;counti++)
									{
										Element arraydata=struct.addElement("data").addAttribute("name", bodyArrayname[counti]); 
										Element arrayfield=arraydata.addElement("field").addAttribute("type", bodyarrattype[counti]).addAttribute("length", bodyarray_datalen[counti]);
											 
												if(bodyarray_dataarray[counti]!=null)
													arrayfield.setText(bodyarray_dataarray[counti]);
												else
												{ 
													arrayfield.setText("");  		
												}
									}
								}
					}
					else
					{
						Element field = data.addElement("field").addAttribute("type", bodydataType[i]).addAttribute("length", bodydateLen[i]).addAttribute("encrypt-mode", "");
						 
						if(bodydate[i]!=null)
							field.setText(bodydate[i]);
						else
							field.setText("");
					}
					
			}
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

	public void CreateBodyDataArray(int index,String tAXTYPENAME, String tAXTIME,
			String tAXAMT) {
		// TODO Auto-generated method stub 
		bodyarray_data[index][0]=tAXTYPENAME;
		bodyarray_data[index][1]=tAXTIME;
		bodyarray_data[index][2]=tAXAMT;
	} 
}