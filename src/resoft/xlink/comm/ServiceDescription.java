package resoft.xlink.comm;

import java.util.Map;

/**
 * <p>��������</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 1:10:27
 */
public class ServiceDescription {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    private String name,className;//������������
    private Map properties;//����
}
