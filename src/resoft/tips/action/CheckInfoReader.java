package resoft.tips.action;

import java.io.IOException;

/**
 * <p>���ж�����Ϣ��ȡ��</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 2:52:19
 */
public interface CheckInfoReader {
    /**
     * �Ƿ�����һ�У��������ƶ��������С�������java.sql.ResultSet
     */
    public boolean next();

    /**
     * �õ����е�ָ����¼
     */
    public String getString(String key);

    public void setFilePath(String filename) throws IOException;
}
