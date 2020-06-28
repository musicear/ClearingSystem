package resoft.xlink.comm.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>XML������Message</p>
 * User: liguoyin
 * Date: 2007-4-26
 * Time: 15:30:22
 */
public class GenericXmlMessage implements Message {
    public static final String NAMESPACE = "@@namespace";

    public GenericXmlMessage() {
        doc = DocumentHelper.createDocument();
        internalMsg.set("CDATAPathList",cdataPathList);
    }

    public GenericXmlMessage(Document doc) {
        this.doc = doc;
    }

    /**
     * �õ�Ĭ�����ļ�ֵ
     *
     * @param key String
     * @return Object
     */
    public Object get(String key) {
        return get("value", key);
    }

    /**
     * �õ�Ĭ�����ļ�ֵ
     *
     * @param key String
     * @return Object
     */
    public String getString(String key) {
        return (String) get("value", key);
    }

    /**
     * �õ�ĳ���ļ�ֵ
     */
    public Object get(String category, String key) {
        if (category.equals("list")) {
            List list = doc.selectNodes(key);
            List values = new ArrayList();
            for (Iterator itr = list.iterator(); itr.hasNext();) {
                Element element = (Element) itr.next();
                values.add(element.getText());
            }
            return values;
        } else {
            if (key.startsWith("//")) {
                //XML�ڵ�
                Node node = doc.selectSingleNode(key);
                if (node == null) {
                    return "";
                } else {
                    return node.getText();
                }
            } else {
                //��xml�ڵ�
                return internalMsg.get(key);
            }

        }
    }

    /**
     * �õ�ĳ���ļ�ֵ
     */
    public String getString(String category, String key) {
        if (category.equals("list")) {
            throw new IllegalArgumentException("not allow list here");
        } else {
            if (key.startsWith("//")) {
                //XML�ڵ�
                Node node = doc.selectSingleNode(key);
                if (node == null) {
                    return "";
                } else {
                    return node.getText();
                }
            } else {
                //��xml�ڵ�
                return (String) internalMsg.get(key);
            }

        }
    }

    /**
     * ���ü�ֵ
     */
    public void set(String key, Object value) {

        set("TEXT", key, value);
    }

    private Node createNewNodeByXPath(String xpath) {
        if (xpath.startsWith("//")) {
            String[] nodes = xpath.substring(2, xpath.length()).split("/");
            String root = nodes[0];
            Element currentElement = doc.getRootElement();
            if (currentElement == null) {
                currentElement = doc.addElement(root);
            }
            for (int i = 1; i < nodes.length; i++) {
                Matcher matcher = pattern.matcher(nodes[i]);
                if (matcher.find()) {
                    //���Ҫ��
                    String numberStr = matcher.group(0);
                    int n = Integer.parseInt(numberStr.substring(1, numberStr.length() - 1));
                    String elementName = nodes[i].substring(0, matcher.start());
                    int size = currentElement.elements(elementName).size();
                    if (size < n) {
                        for (int j = size; j < n; j++) {
                            currentElement.addElement(elementName);
                        }
                    }
                    currentElement = (Element) currentElement.elements(elementName).get(n - 1);
                } else {
                    //����Ҫ��
                    Element element = currentElement.element(nodes[i]);
                    if (element == null) {
                        element = currentElement.addElement(nodes[i]);
                    }
                    currentElement = element;
                }

            }
            return currentElement;
        }
        return null;
    }

    /**
     * ���ü�ֵ
     */
    public void set(String category, String key, Object value) {
        if(value==null) {
            return;
        }
        if(category.equals(NAMESPACE)) {
            Element rootElement = doc.getRootElement();
            if(rootElement!=null) {
                rootElement.addAttribute(key,(String) value);                
            }
        } else if (key.startsWith("//")) {
            //XML�ڵ�
            Node node = doc.selectSingleNode(key);
            if (node == null) {
                node = createNewNodeByXPath(key);
            }
            if (category.equalsIgnoreCase("CDATA")) {
                ((Element) node).addCDATA((String)value);
                cdataPathList.add(key);
                //System.out.println(node);
            } else {                
                node.setText((String) value);
            }
        } else {
            //��xml�ڵ�
            internalMsg.set(key, value);
        }

    }

    /**
     * ���������Ϣ
     */
    public void clear() {
        doc.clearContent();
    }

    /**
     * �õ����е�category
     */
    public Collection findAllCategories() {
        return new ArrayList();
    }

    /**
     * �õ�ĳһ��category������ֵ
     */
    public Collection findAllKeysByCategory(String category) {
        return new ArrayList();
    }

    /**
     * �õ�XML����
     *
     * @return ����XML
     */
    public String asXML() {
        //doc.setProcessingInstructions();
        
        return doc.asXML();
    }

    private Document doc;

    private Message internalMsg = new DefaultMessage();

    private Collection cdataPathList = new ArrayList(); //ֻΪCDATA�Ľڵ�·���б�

    private static final Pattern pattern = Pattern.compile("\\[\\d?\\d\\]$");
}
