package resoft.basLink.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.collections.SequencedHashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>读取xml配置文件。各键值按顺序获得</p>
 * Author: liguoyin
 * Date: 2007-9-23
 * Time: 15:29:55
 */
public class XmlConfiguration {
    public XmlConfiguration(String configName) throws ParserConfigurationException, SAXException, IOException {
        InputStream is = XmlConfiguration.class.getResourceAsStream("/" + configName + "_config.xml");
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(is, new ConfigDefaultHandler(categories));
    }

    private class ConfigDefaultHandler extends DefaultHandler {
        ConfigDefaultHandler(Map categories) {
            this.categories = categories;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("category")) {
                currentValues = new HashMap<String,String>();
                categories.put(attributes.getValue("name"), currentValues);
            } else if (qName.equals("property")) {
                String name = attributes.getValue("name");
                String value = attributes.getValue("value");
                currentValues.put(name, value);
            }
        }

        private Map categories;
        private Map currentValues;
    }

    /**
     * get property
     */
    public String getProperty(String category, String propName) {
        Map values = (Map) categories.get(category);
        if (values == null) {
            return "";
        } else {
            Object value = values.get(propName);
            return (value == null) ? "" : (String) value;
        }

    }

    /**
     * list all category names
     */
    public Collection listAllCategories() {
        return getListBySet(categories.keySet());
    }

    /**
     * list all property names in a cateogy
     */
    public Collection listAllProperties(String categoryName) {
        Map values = (Map) categories.get(categoryName);
        if (values == null) {
            return new ArrayList();
        } else {
            return getListBySet(values.keySet());
        }
    }

    /**
     * list properties map in a category
     */
    public Map getPropertiesMap(String categoryName) {
        return (Map) categories.get(categoryName);
    }

    /**
     * set to list
     */
    private List getListBySet(Set set) {
        List list = new ArrayList();
        for (Iterator itr = set.iterator(); itr.hasNext();) {
            Object o = itr.next();
            list.add(o);
        }
        return list;
    }

    private Map categories = new org.apache.commons.collections.SequencedHashMap();
}
