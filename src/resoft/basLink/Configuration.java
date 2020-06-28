package resoft.basLink;

import java.util.Collection;
import java.util.Map;

import resoft.basLink.util.XmlConfiguration;

/**
 * function:配置文件读取
 * User: albert lee
 * Date: 2005-8-29
 * Time: 13:35:00
 */

public class Configuration {
    /**
     * 得到默认配置文件
     * */
    public static Configuration getInstance() {
        return getInstance("resoft");
    }
    /**
     * 得到其他命名的配置文件
     * */
    public static Configuration getInstance(String configName) {
        return new Configuration(configName);
    }

    /**
     * 构造函数
     * */
    private Configuration(String configName) {
        try {
            conf = new XmlConfiguration(configName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 得到属性
     * */
    public String getProperty(String categoryName,String propertyName) {
        return conf.getProperty(categoryName,propertyName);
    }
    /**
     * 列出某一Category下所有属性
     * */
    public Collection listAllProperties(String categoryName) {
        return conf.listAllProperties(categoryName);
    }
    /**
     * 得到一个category下的属性map
     * */
    public Map getPropertiesMap(String categoryName) {
        return conf.getPropertiesMap(categoryName);
    }
    /**
     * 列出所有的category
     * */
    public Collection listAllCategories() {
        return conf.listAllCategories();
    }

    private XmlConfiguration conf = null;


}
