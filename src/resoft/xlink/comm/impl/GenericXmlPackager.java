package resoft.xlink.comm.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import resoft.xlink.comm.PackException;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;

/**
 * <p>通用XML解析器</p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 20:14:59
 */
public class GenericXmlPackager implements Packager {
    private static final Log logger = LogFactory.getLog(GenericXmlPackager.class);

    /**
     * to Message
     */
    public Message unpack(byte[] bytes) throws PackException {
        Document doc;
        try {
            doc = DocumentHelper.parseText(new String(bytes));
        } catch (DocumentException e) {
            logger.error("创建Document失败", e);
            throw new PackException();
        }

        return new GenericXmlMessage(doc);
    }


    /**
     * Message ---->   byte[]
     */
    public byte[] pack(Message msg) {
        if (msg.get("packFile") == null) {
            return ((GenericXmlMessage) msg).asXML().getBytes();
        } else {
            return pack(msg, (String) msg.get("packFile"));
        }

    }

    /**
     * 按定义的格式解析报文
     */
    private byte[] pack(Message msg, String fileName) {
        GenericXmlMessage packMessage = new GenericXmlMessage();
        try {
//            ClassLoader cl = getClass().getClassLoader();
//            InputStream is = cl.getResourceAsStream(fileName);
            //InputStream is = GenericXmlPackager.class.getResourceAsStream(fileName);
            //@todo 与上一行到底什么区别？上句在id中正常，下句在tips中正常
            ClassLoader cl = getClass().getClassLoader();
            InputStream is = cl.getResourceAsStream(fileName);
            if (is == null) {
                logger.error("找不到格式定义文件:" + fileName);
            }
            //InputStream in = new URL("file:\\C:\\myWork\\development\\xlink\\src\\conf\\pack\\1000.xml").openStream();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            String localName;
            List pathList = new ArrayList();
            List cdataPathList = (List) msg.get("CDATAPathList");
            if (cdataPathList == null) {
                cdataPathList = new ArrayList();
            }

            for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        localName = reader.getLocalName();
                        boolean iterance = false;
                        if (reader.getAttributeCount() > 0) {
                            iterance = true;
                        }
                        pathList.add(new NodeDesc(localName, iterance));

                        //logger.info(localName);
                        //pathList.add(localName);
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        //String
                        itr(msg, packMessage, cdataPathList, "/", pathList);
                        pathList.remove(pathList.size() - 1);
                        break;
                }
            }
            reader.close();
        } catch (XMLStreamException e) {
            logger.error("解析XML错误", e);
        }
        //设置namespace
        for(Iterator itr = msg.findAllKeysByCategory(GenericXmlMessage.NAMESPACE).iterator();itr.hasNext();) {
            String key = (String) itr.next();
            String value = msg.getString(GenericXmlMessage.NAMESPACE,key);
            packMessage.set(GenericXmlMessage.NAMESPACE,key,value);
        }
        return packMessage.asXML().getBytes();
    }

    private boolean itr(Message msg, Message packMessage, List cdataPathList, String headPath, List tailList) {
        String path = headPath;
        if (tailList.size() == 0) {
            //最终节点的终止条件
            String value = msg.getString(path);
            if (value != null && !value.equals("")) {
                if (cdataPathList.contains(path)) {
                    packMessage.set("CDATA", path, msg.get(path));
                } else {
                    packMessage.set("TEXT", path, msg.get(path));
                }
                //logger.info(path);
                return true;
            } else {
                return false;
            }
        } else {
            //递归
            NodeDesc first = (NodeDesc) tailList.get(0);
            path += "/" + first.getName();
            List newTailList = new ArrayList();
            for (int i = 1; i < tailList.size(); i++) {
                newTailList.add(tailList.get(i));
            }
            if (first.isIterance()) {
                boolean returnFlag;
                int i = 1;
                while (true) {
                    String newPath = path + "[" + i + "]";
                    returnFlag = itr(msg, packMessage, cdataPathList, newPath, newTailList);
                    if (!returnFlag) {
                        break;
                    }
                    i ++;
                }
                if(i>1) {
                    //has processed some child nodes
                    return true;
                } else {
                    return returnFlag;
                }
            } else {
                return itr(msg, packMessage, cdataPathList, path, newTailList);
            }
        }
    }

    /**
     * 描述格式定义文件中的一个界面
     */
    class NodeDesc {
        public NodeDesc(String name, boolean iterance) {
            this.name = name;
            this.iterance = iterance;
        }

        public String getName() {
            return name;
        }

        public boolean isIterance() {
            return iterance;
        }

        public String toString() {
            return name + "(" + iterance + ")";
        }

        private String name;//节点名
        private boolean iterance;//是否可重复
    }
}
