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
 * <p>XML描述的Message</p>
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
     * 得到默认类别的键值
     *
     * @param key String
     * @return Object
     */
    public Object get(String key) {
        return get("value", key);
    }

    /**
     * 得到默认类别的键值
     *
     * @param key String
     * @return Object
     */
    public String getString(String key) {
        return (String) get("value", key);
    }

    /**
     * 得到某类别的键值
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
                //XML节点
                Node node = doc.selectSingleNode(key);
                if (node == null) {
                    return "";
                } else {
                    return node.getText();
                }
            } else {
                //非xml节点
                return internalMsg.get(key);
            }

        }
    }

    /**
     * 得到某类别的键值
     */
    public String getString(String category, String key) {
        if (category.equals("list")) {
            throw new IllegalArgumentException("not allow list here");
        } else {
            if (key.startsWith("//")) {
                //XML节点
                Node node = doc.selectSingleNode(key);
                if (node == null) {
                    return "";
                } else {
                    return node.getText();
                }
            } else {
                //非xml节点
                return (String) internalMsg.get(key);
            }

        }
    }

    /**
     * 设置键值
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
                    //多个要素
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
                    //单个要素
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
     * 设置键值
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
            //XML节点
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
            //非xml节点
            internalMsg.set(key, value);
        }

    }

    /**
     * 清除所有信息
     */
    public void clear() {
        doc.clearContent();
    }

    /**
     * 得到所有的category
     */
    public Collection findAllCategories() {
        return new ArrayList();
    }

    /**
     * 得到某一个category的所有值
     */
    public Collection findAllKeysByCategory(String category) {
        return new ArrayList();
    }

    /**
     * 得到XML描述
     *
     * @return 返回XML
     */
    public String asXML() {
        //doc.setProcessingInstructions();
        
        return doc.asXML();
    }

    private Document doc;

    private Message internalMsg = new DefaultMessage();

    private Collection cdataPathList = new ArrayList(); //只为CDATA的节点路径列表

    private static final Pattern pattern = Pattern.compile("\\[\\d?\\d\\]$");
}
