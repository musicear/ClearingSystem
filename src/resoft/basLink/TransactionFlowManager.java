package resoft.basLink;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import resoft.basLink.util.AbstractCacheTemplate;

/**
 * function: �������й�����
 * User: albert lee
 * Date: 2005-9-26
 * Time: 14:17:51
 */

public class TransactionFlowManager extends AbstractCacheTemplate{
    private static Log logger = LogFactory.getLog(TransactionFlowManager.class);
    private static TransactionFlowManager ourInstance = new TransactionFlowManager();

    public static TransactionFlowManager getInstance() {
        return ourInstance;
    }

    private TransactionFlowManager() {
    }

    /**
     * ���ݽ�����õ���������
     * */
    public TransactionFlow getTransactionFlow(String transCode) {
        return (TransactionFlow) get(transCode);
    }
    /**
     * ���ݽ��������¼���
     * */
    protected Object load(String transCode) throws Exception {
        TransactionFlow flow = null;
        //��ȡ�ļ�����
        String filePath = "conf/modules/" + transCode + ".xml";
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(filePath);
        if(is!=null) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                SAXParser parser = spf.newSAXParser();
                flow = new TransactionFlow();
                parser.parse(is,new TransactionFlowParser(flow));
            } catch (Exception e) {
                logger.error("�����ļ�" + filePath + "ʧ��",e);
            }
        }
        //set interceptors
        if(flow!=null) {
            flow.setModuleInterceptors(getInterceptors("moduleInterceptors"));
            flow.setTransactionInterceptors(getInterceptors("transactionInterceptors"));
        }

        return flow;
    }
    /**
     * �õ�������
     * */
    private List getInterceptors(String categoryName) {
        Configuration conf = Configuration.getInstance();
        Collection interceptorNames = conf.listAllProperties(categoryName);
        List interceptors = new ArrayList();
        for(Iterator itr = interceptorNames.iterator();itr.hasNext();) {
            String name = (String) itr.next();
            String className = conf.getProperty(categoryName,name);
            try {
                Object interceptor = Class.forName(className).newInstance();
                interceptors.add(interceptor);
            } catch(Exception e) {
                logger.error("����transactionInterceptorʧ��:" + className,e);
            }
        }
        return interceptors;
    }

    /**
     * ������������
     * */
    private class TransactionFlowParser extends DefaultHandler {
        TransactionFlowParser(TransactionFlow flow) {
            this.flow = flow;
        }
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(qName.equals("action")) {
                String name = attributes.getValue("class");
                currentAct = new Activity(name);
            } else if(qName.equals("transition")) {
                //������ת
                String on = attributes.getValue("on");
                String to = attributes.getValue("to");
                currentAct.addTransition(on,to);
            } else if(qName.equals("label")) {
                flow.addLabel(attributes.getValue("name"));
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(qName.equals("action")) {
                flow.addActivity(currentAct);
            }

        }

        private TransactionFlow flow;
        private Activity currentAct;//��ǰActivity
    }

    /** @link dependency */
    /*# TransactionFlow lnkTransactionFlow; */
}
