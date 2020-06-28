package resoft.basLink;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>function: workFlow����</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-8</p>
 * <p>Time: 15:24:46</p>
 */
public class Activity {
    public Activity(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return moduleName;
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

    private String moduleName;
    private Map transitions = new HashMap();
}
