package resoft.tips.bankImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message;

/**
 * <p>Message中的键值转换。用于MessageSender中。将公共键值转换为各银行专用键值</p>
 * Author: liguoyin
 * Date: 2007-8-16
 * Time: 14:58:35
 */
public class KeyMapper {
    private static Configuration conf = Configuration.getInstance("keyMapper");

    public void process(Message msg) {
        String transCode = msg.getString("交易码");
        //将构造的值放入Map中
        Map newMap = new HashMap();
        for (Iterator itr = conf.listAllProperties(transCode).iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            String value = conf.getProperty(transCode, key);
            if(value.startsWith("${") && value.endsWith("}")) {
                //表达式
                String keyInMsg = value.substring(2,value.length() - 1);
                newMap.put(key,msg.getString(keyInMsg));
                //msg.set(keyInMsg,"");
            } else {
                newMap.put(key,value);
            }
        }
        //删除原有所有键值
        msg.clear();
        //加入构造好的键值
        for(Iterator itr = newMap.keySet().iterator();itr.hasNext();) {
            String key = (String) itr.next();
            String value = (String) newMap.get(key);
            msg.set(key,value);
        }
    }
}
