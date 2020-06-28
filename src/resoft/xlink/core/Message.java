package resoft.xlink.core;

import java.util.Collection;

/**
 * <p>Message接口，用于各交易间传递数据</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 1:49:16
 */
public interface Message {
    public static final String DefaultCategory = "default";
    /**
     * 得到默认类别的键值
     * @param key String
     * @return Object
     * */
    public Object get(String key);
    /**
     * 得到默认类别的键值
     * @param key String
     * @return Object
     * */
    public String getString(String key);
    /**
     * 得到某类别的键值
     * */
    public Object get(String category,String key);
    /**
     * 得到某类别的键值
     * */
    public String getString(String category,String key);
    /**
     * 设置键值
     * */
    public void set(String key,Object value);
    /**
     * 设置键值
     * */
    public void set(String category,String key,Object value);

    /**
     * 清除所有键值
     * */
    public void clear();

    /**
     * 得到所有的category
     * */
    public Collection findAllCategories();
    /**
     * 得到某一个category的所有值
     * */
    public Collection findAllKeysByCategory(String category);
}
