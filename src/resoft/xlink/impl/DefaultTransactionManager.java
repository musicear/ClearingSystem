package resoft.xlink.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import resoft.xlink.core.ActionInterceptor;
import resoft.xlink.transaction.Activity;
import resoft.xlink.transaction.Transaction;
import resoft.xlink.transaction.TransactionInterceptor;
import resoft.xlink.transaction.TransactionManager;

/**
 * <p>默认交易加载器</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:39:41
 */
public class DefaultTransactionManager implements TransactionManager {
    private static final Log logger = LogFactory.getLog(DefaultTransactionManager.class);

    /**
     * 根据交易码得到交易序列
     */
    public Transaction getTransaction(String transCode) {
        Transaction trans = null;
        //读取文件内容
//        String filePath = "/conf/trans/" + transCode + ".xml";.@todo 在Tomcat下可用
        String filePath = "./conf/trans/" + transCode + ".xml";//@todo 在普通Application下可用
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(filePath);
        if (is != null) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                SAXParser parser = spf.newSAXParser();
                trans = new Transaction();
                parser.parse(is, new TransactionParser(trans,globalProperties));
            } catch (Exception e) {
                logger.error("解析文件" + filePath + "失败", e);
            }
        }

        return trans;
    }

    /**
     * 设置交易的全局变量
     */
    public void setGlobalProperties(Map properties) {
        this.globalProperties = properties;
    }
    private Map globalProperties = new HashMap();
}

/**
 * 解析交易序列
 */
class TransactionParser extends DefaultHandler {
    private static final Log logger = LogFactory.getLog(TransactionParser.class);
    TransactionParser(Transaction trans,Map globalProperties) {
        this.trans = trans;
        this.globalProperties = globalProperties;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("activity")) {
            String name = attributes.getValue("name");
            currentAct = new Activity(name);
        } else if (qName.equals("transition")) {
            //增加跳转
            String on = attributes.getValue("on");
            String to = attributes.getValue("to");
            currentAct.addTransition(on, to);
        } else if(qName.equals("property")) {
            //增加属性
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            if(value.startsWith("${") && value.endsWith("}")) {
                value = value.substring(2,value.length() - 1);
                value = (String) globalProperties.get(value);
            }
            currentAct.addProperty(name,value);
        } else if (qName.equals("label")) {
            trans.addLabel(attributes.getValue("name"));
        } else if (qName.equals("transactionInterceptor")) {
            //交易拦截器
            String className = attributes.getValue("class");
            TransactionInterceptor transInterceptor = (TransactionInterceptor) newObject(className);
            transInterceptors.add(transInterceptor);
        } else if(qName.equals("actionInterceptor")) {
            String className = attributes.getValue("class");
            ActionInterceptor actionInterceptor = (ActionInterceptor) newObject(className);
            actionInterceptors.add(actionInterceptor);
        }
    }



    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("activity")) {
            trans.addActivity(currentAct);
        } else if(qName.equals("transaction")) {
            trans.setTransactionInterceptors(transInterceptors);
            trans.setActionInterceptors(actionInterceptors);
        }
    }

    private Object newObject(String className) {
        //noinspection EmptyCatchBlock
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            logger.error("加载类" + className + "失败");
        }
        return null;
    }

    private Transaction trans;
    private Activity currentAct;//当前Activity
    private List transInterceptors = new ArrayList();
    private List actionInterceptors = new ArrayList();
    private Map globalProperties;
//    private Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
//        //Matcher matcher = pattern.matcher(sql);
}

