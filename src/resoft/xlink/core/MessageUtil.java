package resoft.xlink.core;

import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;



/**
 * <p>Message∏®÷˙¿‡</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 22:45:43
 */
public class MessageUtil {
    public static void message2Object(Message msg,Object obj) {
        for(Iterator itr = msg.findAllKeysByCategory(Message.DefaultCategory).iterator();itr.hasNext();) {
            String key = (String) itr.next();
            Object value = msg.get(Message.DefaultCategory,key);
            //noinspection EmptyCatchBlock
            try {
                BeanUtils.setProperty(obj,key,value);
            } catch (Exception e) {
            }
        }
    }

    public static void object2Message(Object obj, Message msg) {
        
    }
}
