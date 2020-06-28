package resoft.tips.bankImpl.qhdyh;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SzsmChargePackager{
	
	String[] header_dataName = {"SERVICE_CODE","TRAN_CODE","TRAN_TYPE","REVERSAL_TRAN_TYPE","TRAN_MODE","SOURCE_TYPE","BRANCH_ID","USER_ID","TRAN_DATE","TRAN_TIMESTAMP","SERVER_ID","WS_ID","USER_LANG","SEQ_NO","PROGRAM_ID","AUTH_USER_ID","AUTH_PASSWORD","AUTH_FLAG","APPR_USER_ID","APPR_FLAG","SOURCE_BRANCH_NO","DEST_BRANCH_NO","MODULE_ID","MESSAGE_TYPE","MESSAGE_CODE"};
	String[] header_dataLen = {"12","4","4","4","10","2","6","30","8","9","30","30","20","16","8","30","30","1","30","1","6","6","2","4","6"};
	String[] header_data=new String[25];
	
	//String[] SERV_DETAIL={"SERV_TYPE","SERV_CCY","SERV_TRAN_TYPE","SERV_AMT","SERV_BO_IND","SERV_CONS","SERV_CR_DR_FLAG","CHARGE_TYPE"};
	//String[] body_dataLen={"50","3","5","16","1","8","2","1","17","1","20","4","","3","8","1"};
	//String[] body_data=new String[16];
	//String[] app_header_dataName = {"PGUP_OR_PGDN","TOTAL_NUM","CURRENT_NUM","PAGE_START","PAGE_END"};
	//String[] app_header_dataLen = {"1","10","10","10","10"};
	//String[] app_heder_data = new String[5];
	//String[] local_header_dataName = {"RET","RET_CODE","RET_MSG"};
	//String[] local_header_dataLen = {"100","6","512"};
	Object[] local_headerr_data = new Object[3];
	
	String[] cardbody_dataName={"CCY","CARD_NO","CHECK_OPTION","CARD_PB_IND","EFFECT_DATE","INPUT_BAL_TYPE","PB_FLAG","TRAN_AMT","TRANSFER_MODE","TRAN_METHOD","TRAN_ARRAY","CUP_DATE","CONTRAST_BAT_NO","NARRATIVE","REFERENCE"};
	String[] cardTRAN_ARRAY={"BASE_ACCT_NO","CCY","CARD_PB_IND","TRAN_TYPE","PB_FLAG","TRAN_AMT","NARRATIVE","EFFECT_DATE"};
	String[] cardTRAN_ARRAY_datalen={"50","3","1","4","1","17","30","8"};
	String[] cardRAN_ARRAY_data=new String[8];
	//String[] SERV_DETAIL={"SERV_TYPE","SERV_CCY","SERV_TRAN_TYPE","SERV_AMT","SERV_BO_IND","SERV_CONS","SERV_CR_DR_FLAG","CHARGE_TYPE"};
	String[] cardbody_dataLen={"3","50","4","1","8","2","1","17","1","1","","8","20","90","50"};
	String[] cardbody_data=new String[15];
	
    boolean foundnode=false;
	
	public void CreateCardSystemHeadData(String WorkDate,String trano)
	{
		DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmssSSS");
    	String traDateTime=dtFormat.format(new Date());
    	String traDate=traDateTime.substring(0, 8);
    	String traTime=traDateTime.substring(8, traDateTime.length());
    	header_data[0]="SVR_CARD";
    	header_data[1]="3122";
    	header_data[2]="TI03";
    	header_data[4]="ONLINE";
    	header_data[5]="TI";
    	header_data[6]="60001"; 
    	header_data[7]="QDTIPS";//待商定 
    	header_data[8]=WorkDate;
    	header_data[9]=traTime;  
    	header_data[10]="127.0.0.1";   
    	header_data[12]="CHINESE";
    	Random rnd = new Random(); 
    	int randate=rnd.nextInt(99);
    	header_data[13]=trano;//渠道流水号
  
    	header_data[22]="RB";//模块标示
    	header_data[23]="1000";//报文类型
    	header_data[24]="6088";//活期转账
	}
	
	public void CreateAppHeadData()
	{
		
	}
	
	public void CreateLocalHeadData()
	{}
	
	
	public void CreateCardBodyData(String payAcct,String traTime,String traAmt,String payeeAcct,String payeeBankNo,String taxOrgCode,String protocolNo)
	{

		cardbody_data[0]="CNY";
		cardbody_data[1]=payAcct;
		cardbody_data[2]="0000";  
		cardbody_data[3]="C";
		cardbody_data[4]=traTime;
		cardbody_data[5]="CA";
		cardbody_data[6]="Y";
		cardbody_data[7]=traAmt;
		cardbody_data[8]="W";
		cardbody_data[9]="3"; 
		cardbody_data[11]=traTime; 
		cardbody_data[12]=payeeBankNo;
		cardbody_data[13]=protocolNo;
		cardbody_data[14]=taxOrgCode;
		cardRAN_ARRAY_data[0]=payeeAcct;
		cardRAN_ARRAY_data[1]="CNY";
		cardRAN_ARRAY_data[2]="P"; 
		cardRAN_ARRAY_data[3]="TI01";//交易类型
		cardRAN_ARRAY_data[4]="Y";
		cardRAN_ARRAY_data[5]=traAmt; 
		cardRAN_ARRAY_data[7]=traTime;
	}
	
	public String MakeUpCardChargePackager() {
		
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

		for(i=0;i<cardbody_dataName.length;i++){
			Element data = body.addElement("data").addAttribute("name", cardbody_dataName[i]);
			if(cardbody_dataName[i].equals("TRAN_ARRAY"))
			{
				Element struct=data.addElement("array").addElement("struct");
				for(int j=0;j<cardTRAN_ARRAY.length;j++)
				{
					Element arraydata=struct.addElement("data").addAttribute("name", cardTRAN_ARRAY[j]);
					String datatype="string";
					if(cardTRAN_ARRAY[j].equals("TRAN_AMT"))
						datatype="double";
					Element arrayfield=arraydata.addElement("field").addAttribute("type", datatype).addAttribute("length", cardTRAN_ARRAY_datalen[j]);
					//arrayfield.setText(TRAN_ARRAY_data[j]);
					if(cardRAN_ARRAY_data[j]!=null)
						arrayfield.setText(cardRAN_ARRAY_data[j]);
					else
						arrayfield.setText("");
				}
			}
			else
			{
				String datatype="string";
				if(cardbody_dataName[i].equals("TRAN_AMT"))
					datatype="double";
				Element field = data.addElement("field").addAttribute("type", datatype).addAttribute("length", cardbody_dataLen[i]).addAttribute("encrypt-mode", "");
				 
				if(cardbody_data[i]!=null)
					field.setText(cardbody_data[i]);
				else
					field.setText("");
			}
		}
	 
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
				Element ele=root.element("body"); 
				
				Iterator it =ele.elements().iterator(); 
				String result="";
				while(it.hasNext()&&!foundnode) 
				{ 
					Element info=(Element) it.next();  
					result = IteratordateNode(info,parm);
				}
				return result;
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