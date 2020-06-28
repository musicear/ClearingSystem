package resoft.xlink.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import resoft.xlink.core.Message;

/**
 * <p>默认Message实现</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:13:06
 */
public class DefaultMessage implements Message {
    private static final String DefaultCategory = "default";
    public DefaultMessage() {
        categories.put(DefaultCategory,new LinkedMap());
    }
    /**
     * 得到默认类别的键值
     *
     * @param key String
     * @return Object
     */
    public Object get(String key) {
        return get(DefaultCategory,key);
    }

    /**
     * 得到默认类别的键值
     *
     * @param key String
     * @return Object
     */
    public String getString(String key) {
        return (String) get(DefaultCategory,key);
    }
    /**
     * 得到某类别的键值
     */
    public Object get(String category, String key) {
        Map dataMap = (Map) categories.get(category);
        if(dataMap==null) {
            return null;
        }
        return dataMap.get(key);
    }

    /**
     * 得到某类别的键值
     */
    public String getString(String category, String key) {
        Map dataMap = (Map) categories.get(category);
        if(dataMap==null) {
            return null;
        }
        return (String) dataMap.get(key);
    }
    /**
     * 设置键值
     */
    public void set(String key, Object value) {
        set(DefaultCategory,key,value);
    }

    /**
     * 设置键值
     */
    public void set(String category, String key, Object value) {
        Map dataMap = (Map) categories.get(category);
        if(dataMap ==null) {
            //dataMap = new HashMap();
            dataMap = new LinkedMap();
            categories.put(category,dataMap);
        }
        dataMap.put(key,value);
    }

    /**
     * 清除所有信息
     * */
    public void clear() {
        categories.clear();
    }

    /**
     * 得到所有的category
     */
    public Collection findAllCategories() {
        return categories.keySet();
    }

    /**
     * 得到某一个category的所有值
     */
    public Collection findAllKeysByCategory(String category) {
        Map dataMap = (Map) categories.get(category);
        if(dataMap==null) {
            return new ArrayList();
        } else {
            return dataMap.keySet();
        }
    }

    private Map categories = new HashMap();

}
///**
// * 按放入顺序的Map
// * */
//class SequenceMap implements Map {
//
//    public int size() {
//        return keys.size();
//    }
//
//    public void clear() {
//        keys.clear();
//        values.clear();
//    }
//
//    public boolean isEmpty() {
//        return keys.isEmpty();
//    }
//
//    public boolean containsKey(Object key) {
//        return keys.contains(key);
//    }
//
//    public boolean containsValue(Object value) {
//        return values.contains(value);
//    }
//
//    public Collection values() {
//        return values;
//    }
//
//    public void putAll(Map t) {
//        throw new java.lang.NoSuchMethodError();
//    }
//
//    public Set entrySet() {
//        throw new java.lang.NoSuchMethodError();
//    }
//
//    public Set keySet() {
//        Set keySet = new SequenceSet();
//        keySet.addAll(keys);
//        return keySet;
//    }
//
//    public Object get(Object key) {
//        for(int i = 0;i<keys.size();i++) {
//            if(keys.get(i).equals(key)) {
//                return values.get(i);
//            }
//        }
//        return null;
//    }
//
//    public Object remove(Object key) {
//        for(int i = 0;i<keys.size();i++) {
//            if(keys.get(i).equals(key)) {
//                keys.remove(key);
//                values.remove(i);
//            }
//        }
//        return key;
//    }
//
//    public Object put(Object key, Object value) {
//        if(keys.contains(key)) {
//            int i = values.indexOf(value);
//            values.set(i,value);
//        } else {
//            keys.add(key);
//            values.add(value);
//        }
//        return key;
//    }
//    private List keys = new ArrayList();
//    private List values = new ArrayList();
//}
//class SequenceSet implements Set {
//
//    public int size() {
//        return list.size();
//    }
//
//    public void clear() {
//        list.clear();
//    }
//
//    public boolean isEmpty() {
//        return list.isEmpty();
//    }
//
//    public Object[] toArray() {
//        return list.toArray();
//    }
//
//    public boolean add(Object o) {
//        return list.add(o);
//    }
//
//    public boolean contains(Object o) {
//        return list.contains(o);
//    }
//
//    public boolean remove(Object o) {
//        return list.remove(o);
//    }
//
//    public boolean addAll(Collection c) {
//        return list.addAll(c);
//    }
//
//    public boolean containsAll(Collection c) {
//        return list.containsAll(c);
//    }
//
//    public boolean removeAll(Collection c) {
//        return list.removeAll(c);
//    }
//
//    public boolean retainAll(Collection c) {
//        return list.retainAll(c);
//    }
//
//    public Iterator iterator() {
//        return list.iterator();
//    }
//
//    public Object[] toArray(Object a[]) {
//        return list.toArray(a);
//    }
//    private List list = new ArrayList();
//}
