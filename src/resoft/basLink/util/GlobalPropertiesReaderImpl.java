package resoft.basLink.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import resoft.basLink.Configuration;
import resoft.xlink.comm.GlobalPropertiesReader;

/**
 * <p>全局属性读取。用于替换流程描述中的${}</p>
 * Author: liguoyin
 * Date: 2007-8-14
 * Time: 22:59:23
 */
public class GlobalPropertiesReaderImpl implements GlobalPropertiesReader {
    private static Configuration conf = Configuration.getInstance();
    public Map getAllProperties() {
        Map props = new HashMap();
        for(Iterator itrCategory = conf.listAllCategories().iterator();itrCategory.hasNext();) {
            String category = (String) itrCategory.next();
            for(Iterator itrProp = conf.listAllProperties(category).iterator();itrProp.hasNext();) {
                String prop = (String) itrProp.next();
                String value = conf.getProperty(category,prop);
                props.put(category + "." + prop,value);
            }
        }
        return props;
    }
}
