package junit;

import java.sql.SQLException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import resoft.xlink.core.Message;

public class XMLReaderTest extends DefaultHandler{
	
	
    
    public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
        

        
	}
	
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	if (qName.equals("HEAD")) {
            isHead = true;
        }
    	if (qName.equals("MSG")){
    		isMsg = true;
    	}
        if(qName.equalsIgnoreCase("body"))
        {
        	isBody=true;
        }
        if(qName.equalsIgnoreCase("field"))
        {
            isfield = true;
        }
        if(qName.equalsIgnoreCase("sys-header"))
        { 
			isSys_header=true;
        }
        if (!qName.equals("CFX") && !qName.equals("HEAD") && !qName.equals("MSG") && !qName.equals("PRI")) {
            if (isHead) {
                //msg.setHeadValue(qName,value);
                headKey = qName;
            } 
            if (isMsg) {
            	msgKey = qName;
             
            }
            if(isBody&&qName.equals("data"))
            {
            	bodyKey=attributes.getValue(0);
            }
            if(isSys_header&&qName.equals("data"))
            {
            	Sys_headerkey=attributes.getValue(0);
            }
        } 
    }
    
    public void characters(char ch[], int start, int length) throws SAXException {
    
    	if (isHead && headKey != null) {
            String value = new String(ch, start, length);

            System.out.println("value is"+ value);
            headKey = null;
        }
    	if (isMsg && msgKey != null){
    		String value = new String(ch, start, length);
    		value=value.trim();
           
            msgKey = null;
    		 
        }  
    	if(isfield&&isBody&&bodyKey!=null)
    	{ 
    		String value = new String(ch, start, length);
    		value=value.trim();
           
            bodyKey = null;
    	}
    	if(isSys_header&&isfield&&Sys_headerkey!=null)
    	{
    		String value = new String(ch, start, length);
    		value=value.trim();
           
            Sys_headerkey = null;
    	}
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("HEAD")) {
            isHead = false;
        }
        if(qName.equalsIgnoreCase("body"))
        {
        	isBody=false;
        }
        if(qName.equalsIgnoreCase("field"))
        {
            isfield = false;
        }
        if(qName.equalsIgnoreCase("sys-header"))
        {
        	isSys_header=false;
        }
        
    }

    private boolean isHead = false;
    private String headKey = null;
    private boolean isMsg = false;
    private String msgKey = null;
    private Message msg;
    private boolean isBody=false;
    private String bodyKey=null;
    boolean isfield=false;
    private boolean isSys_header;
    private String Sys_headerkey=null;
} 

