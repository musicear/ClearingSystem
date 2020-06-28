package resoft.tips.util;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.MessageSender;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-7-19
 * Time: 16:31:28
 */
public class MessageSenderUtil {
    private static final Log logger = LogFactory.getLog(MessageSenderUtil.class);
    private static Configuration conf = Configuration.getInstance();
    public static MessageSender getMessageSender() {
        String className = conf.getProperty("bankImpl","className");
        MessageSender sender = null;
        try {
            sender = (MessageSender) Class.forName(className).newInstance();
        } catch(Exception e) {
            logger.error("加载类：" + className + "失败",e);
        }
        //设置各种属性
        if(sender!=null) {
            for(Iterator itr = conf.listAllProperties("bankImpl").iterator();itr.hasNext();) {
                String key = (String) itr.next();
                if(key.equals("bankImpl")) {
                    continue;
                }
                String value = conf.getProperty("bankImpl",key);
                sender.setProperty(key,value);
            }
        }
        return sender;
    }
}
