package resoft.xlink.transaction;

import java.util.Map;

/**
 * <p>���׹����������ص�</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:38:22
 */
public interface TransactionManager {
    /**
     * ���ݽ�����õ���������
     * */
    public Transaction getTransaction(String transCode);
    /**
     * ���ý��׵�ȫ�ֱ���
     * */
    public void setGlobalProperties(Map properties);
}
