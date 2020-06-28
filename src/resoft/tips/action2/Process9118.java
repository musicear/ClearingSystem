package resoft.tips.action2;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p> * ����֧���ʺ���֤�������ݿ� * </p>
 * User: chenlujia 
 * Date: 2008-8-25
 */
public class Process9118 implements Action {
    private static final Log logger = LogFactory.getLog(Process9118.class);

    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("�����ļ�");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");
        String localName = "", parentLocalName="";
        String sendOrgCode = "", payeeBankNo="", entrustDate = "", packNo= "";
        //����Ϣ
        Map packParams = new HashMap();
        //�����Ϣ
        Map proveParams = new HashMap();
        Stack tagStack = new Stack();
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();

                    tagStack.push(localName);
                    if (localName.equals("BatchHead9118")) {
                    	packParams.clear();
                    	
                    } else if (localName.equals("ProveInfo9118")) {
                    	proveParams.clear();
                    } 
                    if (tagStack.size() > 1) {
                    	parentLocalName=(String)(tagStack.get(tagStack.size()-2));
                    } else {
                    	parentLocalName="";
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                    	if (parentLocalName.equals("BatchHead9118")) {
                    		packParams.put(localName, text);
                    		if (localName.equals("SendOrgCode")) {
                    			sendOrgCode = text;
                            } else if (localName.equals("EntrustDate")) {
                                entrustDate = text;
                            } else if (localName.equals("PackNo")) {
                                packNo = text;
                            } else if (localName.equals("PayeeBankNo")) {
                            	payeeBankNo = text;
                            } else if (localName.equals("AllNum")) {
                            	packParams.put(localName, Integer.valueOf(text));
                            }
                    	} else if (parentLocalName.equals("ProveInfo9118")) {
                    		proveParams.put(localName, text); 	
                    	}
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    tagStack.pop();
                   if (localName.equals("ProveInfo9118")) {
                    	proveParams.put("SendOrgCode", sendOrgCode);
                    	proveParams.put("entrustDate", entrustDate);
                    	proveParams.put("packNo", packNo);
                    	proveParams.put("PayeeBankNo", payeeBankNo);
                    	DBUtil.insert("PayAcctProveDetail", proveParams);
                    } else if (localName.equals("BatchHead9118")) {
                    	packParams.put("checkStatus", "0");// δ��֤״̬
                    	packParams.put("sendReturn", "0");// δ���ͻ�ִ״̬
                    	packParams.put("procstatus", "0");// δ����״̬
                    	DBUtil.insert("PayAcctProvePack", packParams);
                    	logger.info("��֤�����(9118)����ɹ�.packNo=" + packNo + ";entrustDate=" + entrustDate + ";SendOrgCode=" + sendOrgCode);

                    }
                    break;
            }
        }

        return SUCCESS;
    }

   
}
