package resoft.basLink;

/**
 * function: ����ִ��������
 * User: albert lee
 * Date: 2005-10-15
 * Time: 20:04:31
 */
public interface ModuleInterceptor {
    /**
     * ִ��ÿ��module֮ǰ
     * */
    public boolean beforeModule(ModuleBase module,Message msg);
    /**
     * ִ��ÿ��module֮��
     * @param module ModuleBase ģ����
     * @param msg Message
     * @param result String  ִ�н��
     * */
    public void afterModule(ModuleBase module,Message msg,int result);
}
