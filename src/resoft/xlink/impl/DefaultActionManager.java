package resoft.xlink.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionManager;

/**
 * <p>Action������</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:49:49
 */
public class DefaultActionManager implements ActionManager {
    private static final Log logger = LogFactory.getLog(DefaultActionManager.class);

    public Action createAction(String name, Map properties) {
        //ֻ֧�ּ򵥸�ֵ����
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;

        Class clazz;
        Action action;
        //������
        try {
            clazz = Class.forName(name);
            action = (Action) clazz.newInstance();
        } catch (Exception e) {
            logger.error("������" + name + "ʧ��", e);
            throw new RuntimeException("������" + name + "ʧ��", e);
        }
        //��������
        for (Iterator itr = properties.keySet().iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            String value = (String) properties.get(key);
            String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
            Method method;
            try {
                method = clazz.getMethod(methodName, parameterTypes);
                if(method==null) {
                    tryInjectInstance(key,value);
                } else {
                    method.invoke(action, new String[]{value});
                }
            } catch (Exception e) {
                logger.error("������" + name + "������" + key + "ʧ��",e);
            }

        }
        return action;
    }

    private void tryInjectInstance(String key, String value) {

    }
}
