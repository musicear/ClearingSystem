package junit;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import resoft.basLink.util.UnpackFormatCache;
import resoft.xlink.comm.PackException;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;


public class TestSAX implements Action {
	 private static final Log logger = LogFactory.getLog(TestSAX.class);
	
	 private static final boolean ADD_CRCF = true;
	 
	 private static SAXParserFactory spf = null;
	 
	 static {
	        //��ʼ��SAX������
	        spf = SAXParserFactory.newInstance();
	    }
	 
	    
	    public TestSAX() {

	    }

		public int execute(Message arg0) throws Exception {
			
				final Message msg = new DefaultMessage();
				SAXParser saxParser;
			try {
					saxParser = spf.newSAXParser();
			} catch (Exception e) {
	        	logger.info("����ʧ��1");
	            throw new RuntimeException(e.getMessage(), e);
	        }
				 
				File f=new File("D:\\test.xml");
				byte[] bytes = new byte[(int) f.length()];
				InputStream is = new FileInputStream(f);
		        is.read(bytes);
				 //InputSource input = new InputSource(url.openStream());
				try {
		            InputSource inputSource = new InputSource(new StringReader(new String(bytes, "GBK")));
		            inputSource.setEncoding("GB2312");
		            
		            saxParser.parse(inputSource, new PackReader(msg));
		        } catch (Exception e) {
		        	logger.info("����ʧ��2");
		            throw new PackException("����ʧ��", e);
		        }
			return 0;
		}
		
		class PackReader extends DefaultHandler {
	        PackReader(Message msg) {
	            this.msg = msg;
	        }

	        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        	if(qName.equals("CFX")){
	        		isCfx = true;
	        	}
	        	if (qName.equals("HEAD")) {
	                isHead = true;
	            }
	        	if (qName.equals("MSG")){
	        		isMsg = true;
	        	}
	            
	            if (!qName.equals("CFX") && !qName.equals("HEAD") && !qName.equals("MSG") && !qName.equals("PRI")) {
	                if (isHead) {
	                    //msg.setHeadValue(qName,value);
	                    headKey = qName;
	                } 
	                if (isMsg) {
	                	msgKey = qName;
	                 
	                }
	                
	            } 
	        }
	        
	        public void characters(char ch[], int start, int length) throws SAXException {
	        
	        	if (isHead && headKey != null) {
	                String value = new String(ch, start, length);
	                msg.set("head", headKey, value);
	                logger.info("headkey is:"+headKey);
	                logger.info("headkey  is:"+value);
	                headKey = null;
	            }
	        	if (isMsg && msgKey != null){
	        		String value = new String(ch, start, length);
	        		value=value.trim();
	                msg.set("msg", msgKey, value);
	                msg.set(msgKey, value);
	                logger.info("key is:"+msgKey);
	                logger.info("  value is:"+value);
	                msgKey = null;
	        		 
	            }  
	        	
	        }

	        public void endElement(String uri, String localName, String qName) throws SAXException {
	            if (qName.equals("HEAD")) {
	                isHead = false;
	            }
	            if(qName.equalsIgnoreCase("MSG"))
	            {
	            	isMsg=false;
	            }
	           
	        }

	        private boolean isHead = false;
	        private String headKey = null;
	        private boolean isMsg = false;
	        private String msgKey = null;
	        private Message msg;
	        private boolean isCfx=false;
	        private String bodyKey=null;
	        boolean isfield=false;
	        private boolean isSys_header;
	        private String Sys_headerkey=null;
	    } 

	    
			 

		/**
	     * û�и�ʽ�����ļ�
	     */
	    private byte[] packWithoutFormatFile(Message msg) {
	        //if(msg.get)
	        StringBuffer sb = new StringBuffer("<CFX>");
	        //HEAD TAG
	        if (msg.findAllKeysByCategory("head").size() > 0) {
	            sb.append("<HEAD>");
	            for (Iterator itr = msg.findAllKeysByCategory("head").iterator(); itr.hasNext();) {
	                String key = (String) itr.next();
	                if (key == null) {
	                    continue;
	                }
	                String value = (String) msg.get("head", key);
	                sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
	            }
	            sb.append("</HEAD>");
	        }
	        //MSG TAG

	        sb.append("<MSG>");
	        for (Iterator itr = msg.findAllKeysByCategory(Message.DefaultCategory).iterator(); itr.hasNext();) {
	            String key = itr.next().toString();
	            if (key.startsWith("//") || msg.get(key) == null) {
	                continue;
	            }
	            //String value = (String) msg.get("msg", key);
	            StringBuffer value = new StringBuffer(msg.get(key).toString());
	            //special char
	            replaceAll(value, "&", "&amp;");
	            replaceAll(value, "<", "&lt;");
	            replaceAll(value, ">", "&gt;");
	            replaceAll(value, "\"", "&quot;");
	            //value.replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;");
	            sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
	        }
	        
	        sb.append("</MSG>");

	        sb.append("</CFX>");

	        return sb.toString().getBytes();
	    }


	    /**
	     * �滻StringBuffer�е��ַ���
	     */
	    private void replaceAll(StringBuffer sb, String findText, String replacement) {
	        int pos = sb.indexOf(findText);
	        if (pos >= 0) {
	            sb.replace(pos, pos + findText.length(), replacement);
	        }
	    }

	   /**
	     * ���ļ���ȡ�ñ��ĸ�ʽ�����ļ�
	     *
	     * @param msg      Message
	     * @param filePath String   ��ʽ�����ļ�·����ָ����ClassPath�е����·��
	     */
	    private byte[] packByFile(Message msg, String filePath) throws PackException {
	        //filePath = "./conf/pack/" + filePath;

	        Map formatMap = UnpackFormatCache.getInstance().getFormat(filePath);
	        if (formatMap == null) {
	            throw new PackException();
	        }
	        StringBuffer xml = new StringBuffer();
	        xml.append("<CFX>");
	        //HEAD��
	        List headList = (List) formatMap.get("head");
	        if (headList.size() > 0) {
	            xml.append("<HEAD>");
	            for (Iterator itr = headList.iterator(); itr.hasNext();) {
	                String key = (String) itr.next();
	                String value = (String) msg.get("head", key);
	                if (value != null && !value.equals("")) {
	                    //��ʽ����<SRC>1000</SRC>
	                    xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
	                }
	            }
	            xml.append("</HEAD>");
	        }
	        //headList;
	        //MSG��
	        List msgList = (List) formatMap.get("msg");
	        if (msgList.size() > 0) {
	            xml.append("<MSG>");
	            for (Iterator itr = msgList.iterator(); itr.hasNext();) {
	                String key = (String) itr.next();
	                String value = (String) msg.get(key);
	                if (value != null && !value.equals("")) {
	                    //��ʽ����<��˰�˱��� val="34234"/>
	                    xml.append("<").append(key).append(" val=\"").append(value).append("\"/>");
	                }
	            }
	            xml.append("</MSG>");
	        }
	        //msgList = null;
	        //PRI��
	        List priList = (List) formatMap.get("pri");
	        if (priList.size() > 0) {
	            xml.append("<PRI>");
	            for (Iterator itr = priList.iterator(); itr.hasNext();) {
	                String key = (String) itr.next();
	                String value = (String) msg.get(key);
	                if (value != null && !value.equals("")) {
	                    //��ʽ����<��˰�˱��� val="34234"/>
	                    xml.append("<").append(key).append(" val=\"").append(value).append("\"/>");
	                }
	            }
	            xml.append("</PRI>");
	        }
	        xml.append("</CFX>");
	        if (ADD_CRCF) {
	            xml.append("\r\n");
	        }

	        try {
	            return xml.toString().getBytes("GBK");
	        } catch (UnsupportedEncodingException e) {
	            return new byte[]{};
	        }

	    }
	        
}

