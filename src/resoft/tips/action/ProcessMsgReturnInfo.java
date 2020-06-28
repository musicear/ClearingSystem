package resoft.tips.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>������Ҫ�ء�����9121���ġ�9122���ĵȡ����������������</p>
 * Author: liguoyin
 * Date: 2007-8-13
 * Time: 0:17:48
 */
public class ProcessMsgReturnInfo implements Action {
    private static final Log logger = LogFactory.getLog(ProcessMsgReturnInfo.class);
    public int execute(final Message msg) throws Exception {
        String filePath = msg.getString("�����ļ�");
        BatchXmlProcessor processor = new BatchXmlProcessor(filePath);
        processor.setHandler(new BatchXmlHandler() {

            public Collection getTags() {
                Collection tags = new ArrayList();
                tags.add("MsgReturn9121");
                tags.add("MsgReturn9122");
                tags.add("HEAD");
                return tags;
            }

            public void process(String tagName, Map children) throws Exception {
                for(Iterator itr = children.keySet().iterator();itr.hasNext();) {
                    String key = (String) itr.next();
                    String value = (String) children.get(key);
                    msg.set(key,value);
                }
            }

            public void end() {

            }
        });
        processor.execute();
        logger.info("OriMsgNo:" + msg.get("OriMsgNo"));
        logger.info("AddWord:" + msg.get("AddWord"));
        return SUCCESS;
    }

}
