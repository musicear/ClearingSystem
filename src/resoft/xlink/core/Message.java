package resoft.xlink.core;

import java.util.Collection;

/**
 * <p>Message�ӿڣ����ڸ����׼䴫������</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 1:49:16
 */
public interface Message {
    public static final String DefaultCategory = "default";
    /**
     * �õ�Ĭ�����ļ�ֵ
     * @param key String
     * @return Object
     * */
    public Object get(String key);
    /**
     * �õ�Ĭ�����ļ�ֵ
     * @param key String
     * @return Object
     * */
    public String getString(String key);
    /**
     * �õ�ĳ���ļ�ֵ
     * */
    public Object get(String category,String key);
    /**
     * �õ�ĳ���ļ�ֵ
     * */
    public String getString(String category,String key);
    /**
     * ���ü�ֵ
     * */
    public void set(String key,Object value);
    /**
     * ���ü�ֵ
     * */
    public void set(String category,String key,Object value);

    /**
     * ������м�ֵ
     * */
    public void clear();

    /**
     * �õ����е�category
     * */
    public Collection findAllCategories();
    /**
     * �õ�ĳһ��category������ֵ
     * */
    public Collection findAllKeysByCategory(String category);
}
