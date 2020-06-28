package resoft.basLink.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function:���������
 * User: albert lee
 * Date: 2005-10-24
 * Time: 23:23:49
 */
public abstract class AbstractCacheTemplate {
    private static Log logger = LogFactory.getLog(AbstractCacheTemplate.class);
    /**
     * �õ�����
     * */
    public Object get(String key) {
        Object value = null;
        if(map.containsKey(key)) {
            return map.get(key);
        } else {
            synchronized(map) {
                try {
                    value = load(key);
                } catch (Exception e) {
                    logger.error("����",e);
                }
                map.put(key,value);
            }
        }
        return value;
    }
    protected abstract Object load(String key) throws Exception;
    private final Map map = new HashMap();
}
