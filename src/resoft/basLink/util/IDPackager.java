package resoft.basLink.util;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import resoft.xlink.comm.PackException;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 *
 * @Author: liguoyin
 * Date: 2007-5-20
 * Time: 16:28:06
 */
public class IDPackager implements Packager {
    public Message unpack(byte[] bytes) throws PackException {
        String xml = new String(bytes);
        Message msg = new DefaultMessage();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element msgElement = (Element) doc.getRootElement().selectSingleNode("//CFX/MSG");
            for(Iterator itr = msgElement.elements().iterator();itr.hasNext();) {
                Element element = (Element) itr.next();
                msg.set(element.getName(),element.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return msg;
    }

    public byte[] pack(Message msg) {
        StringBuffer xml = new StringBuffer("<CFX><MSG>");
        for(Iterator itr = msg.findAllKeysByCategory(Message.DefaultCategory).iterator();itr.hasNext();) {
            String key = (String) itr.next();
            String value = (String) msg.get(key);
            xml.append("<").append(key).append(">");
            xml.append(value);
            xml.append("</").append(key).append(">");
        }
        xml.append("</MSG></CFX>");
        return xml.toString().getBytes();
    }
}
