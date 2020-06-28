package resoft.xlink.transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>交易调度中的一个活动</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:32:37
 */
public class Activity {
    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 添加属性
     * */
    public void addProperty(String name,String value) {
        properties.put(name,value);
    }

    /**
     * 列出所有属性
     * */
    public Map listAllProperties() {
        return properties;
    }

    /**
     * 增加本Activity的跳转
     * */
    public void addTransition(String on,String to) {
        transitions.put(on,to);
    }
    /**
     * 得到跳转方向
     * */
    public String getTransitionTo(String on) {
        String to = (String) transitions.get(on);
        return to==null? "":to;
    }

    private String name;
    private Map transitions = new HashMap();
    private Map properties = new HashMap();
}
