package resoft.basLink;

/**
 * function: ��������������ÿ����ִ��ǰ��ִ��
 * User: albert lee
 * Date: 2005-11-1
 * Time: 9:37:29
 */
public interface TransactionInterceptor {
    /**
     * ����ǰִ��
     * @return boolean �Ƿ��������ִ�С�true:������false:ֹͣ
     * */
    public boolean before(Message msg);
    /**
     * ���׺�ִ��
     * //@todo ��ת��Ϊ��ϵͳ��ֵ���ֿɷ��ڴ˴�������finally
     * */
    public void after(Message msg);
}
