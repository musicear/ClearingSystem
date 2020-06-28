package resoft.basLink;

import java.util.Collection;
import java.util.Map;

import resoft.basLink.util.XmlConfiguration;

/**
 * function:�����ļ���ȡ
 * User: albert lee
 * Date: 2005-8-29
 * Time: 13:35:00
 */

public class Configuration {
    /**
     * �õ�Ĭ�������ļ�
     * */
    public static Configuration getInstance() {
        return getInstance("resoft");
    }
    /**
     * �õ����������������ļ�
     * */
    public static Configuration getInstance(String configName) {
        return new Configuration(configName);
    }

    /**
     * ���캯��
     * */
    private Configuration(String configName) {
        try {
            conf = new XmlConfiguration(configName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * �õ�����
     * */
    public String getProperty(String categoryName,String propertyName) {
        return conf.getProperty(categoryName,propertyName);
    }
    /**
     * �г�ĳһCategory����������
     * */
    public Collection listAllProperties(String categoryName) {
        return conf.listAllProperties(categoryName);
    }
    /**
     * �õ�һ��category�µ�����map
     * */
    public Map getPropertiesMap(String categoryName) {
        return conf.getPropertiesMap(categoryName);
    }
    /**
     * �г����е�category
     * */
    public Collection listAllCategories() {
        return conf.listAllCategories();
    }

    private XmlConfiguration conf = null;


}
