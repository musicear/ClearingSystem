package resoft.tips.action;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * <p>汇总、明细形式报文处理类</p>
 * Author: liguoyin
 * Date: 2007-6-14
 * Time: 17:23:28
 */
public class BatchXmlProcessor {
    public BatchXmlProcessor(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public void setHandler(BatchXmlHandler handler) {
        this.handler = handler;
    }

    public void execute() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(xmlPath), "UTF-8");
        Collection tags = handler.getTags();
        String localName = "";
        boolean inSection = false;
        Map values = new HashMap();
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    if(tags.contains(localName)) {
                        inSection = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("") && inSection) {
                        values.put(localName, text);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    if(tags.contains(localName)) {
                        handler.process(localName,values);
                        inSection = false;
                        values.clear();
                    }
                    break;
            }
        }
        handler.end();
    }

    private BatchXmlHandler handler;
    private String xmlPath;
}
