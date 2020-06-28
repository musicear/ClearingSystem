package resoft.tips.action;

import java.util.Collection;
import java.util.Map;

/**
 * <p>用于1:n格式的xml报文。如对账、批量扣税、批量包核对等</p>
 * Author: liguoyin
 * Date: 2007-6-14
 * Time: 17:15:57
 */
public interface BatchXmlHandler {
    /**
     * 得到要处理的节点列表
     * */
    public Collection getTags();
    /**
     * 处理
     * */
    public void process(String tagName,Map children) throws Exception;
    /**
     * 处理完毕
     * */
    public void end();
}
