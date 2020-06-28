package configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jconfig.ConfigurationManager;

public class PackageInterface
{

    public static PackageInterface getInstance(String configName)
    {
        org.jconfig.Configuration conf;
        if(confMap.containsKey(configName))
            conf = (org.jconfig.Configuration)confMap.get(configName);
        else
            synchronized(confMap)
            {
                conf = ConfigurationManager.getConfiguration(configName);
                confMap.put(configName, conf);
            }
        if(conf == null)
            throw new RuntimeException("\u914D\u7F6E\u6587\u4EF6" + configName + "\u672A\u627E\u5230");
        else
            return new PackageInterface(conf);
    }

    private PackageInterface(org.jconfig.Configuration conf)
    {
        this.conf = null;
        this.conf = conf;
    }

    public String getProperty(String categoryName, String propertyName)
    {
        return conf.getProperty(propertyName, "", categoryName);
    }

    public Collection listAllProperties(String categoryName)
    {
        String properties[] = conf.getPropertyNames(categoryName);
        if(properties == null)
            return new ArrayList();
        else
            return Arrays.asList(properties);
    }

    public Collection listAllCategories()
    {
        String categories[] = conf.getCategoryNames();
        return ((Collection) (categories != null ? Arrays.asList(categories) : new ArrayList()));
    }

    private org.jconfig.Configuration conf;
    private static final Map confMap = new HashMap();

}
