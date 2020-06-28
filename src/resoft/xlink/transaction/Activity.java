package resoft.xlink.transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>���׵����е�һ���</p>
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
     * �������
     * */
    public void addProperty(String name,String value) {
        properties.put(name,value);
    }

    /**
     * �г���������
     * */
    public Map listAllProperties() {
        return properties;
    }

    /**
     * ���ӱ�Activity����ת
     * */
    public void addTransition(String on,String to) {
        transitions.put(on,to);
    }
    /**
     * �õ���ת����
     * */
    public String getTransitionTo(String on) {
        String to = (String) transitions.get(on);
        return to==null? "":to;
    }

    private String name;
    private Map transitions = new HashMap();
    private Map properties = new HashMap();
}
