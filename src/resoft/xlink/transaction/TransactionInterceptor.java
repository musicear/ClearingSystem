package resoft.xlink.transaction;

import resoft.xlink.core.Message;

/**
 * <p>��������������ÿ����ִ��ǰ��ִ��</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:39:25
 */
public interface TransactionInterceptor {
    /**
     * ����ǰִ��
     * @return boolean �Ƿ��������ִ�С�true:������false:ֹͣ
     * */
    public boolean before(Message msg);
    /**
     * ���׺�ִ��     
     * */
    public void after(Message msg);
}
