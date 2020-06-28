package resoft.basLink.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-11
 * Time: 16:51:29
 */
public class BasLinkLogFactory extends LogFactory{
    public Object getAttribute(String s) {
        return null;
    }

    public String[] getAttributeNames() {
        return new String[0];
    }

    public Log getInstance(Class aClass) throws LogConfigurationException {
        return new BasLinkLog(aClass.getName());
    }

    public Log getInstance(String s) throws LogConfigurationException {
        return new BasLinkLog(s);
    }

    public void release() {

    }

    public void removeAttribute(String s) {

    }

    public void setAttribute(String s, Object o) {

    }
}
