package resoft.xlink.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionManager;

/**
 * <p>Action管理器</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:49:49
 */
public class DefaultActionManager implements ActionManager {
    private static final Log logger = LogFactory.getLog(DefaultActionManager.class);

    public Action createAction(String name, Map properties) {
        //只支持简单赋值操作
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;

        Class clazz;
        Action action;
        //加载类
        try {
            clazz = Class.forName(name);
            action = (Action) clazz.newInstance();
        } catch (Exception e) {
            logger.error("加载类" + name + "失败", e);
            throw new RuntimeException("加载类" + name + "失败", e);
        }
        //设置属性
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
                logger.error("处理类" + name + "的属性" + key + "失败",e);
            }

        }
        return action;
    }

    private void tryInjectInstance(String key, String value) {

    }
}
