package resoft.tips.action;

import java.util.Collection;
import java.util.Map;

/**
 * <p>����1:n��ʽ��xml���ġ�����ˡ�������˰���������˶Ե�</p>
 * Author: liguoyin
 * Date: 2007-6-14
 * Time: 17:15:57
 */
public interface BatchXmlHandler {
    /**
     * �õ�Ҫ����Ľڵ��б�
     * */
    public Collection getTags();
    /**
     * ����
     * */
    public void process(String tagName,Map children) throws Exception;
    /**
     * �������
     * */
    public void end();
}
