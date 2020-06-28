package resoft.xlink.core;

/**
 * <p>Actionִ��������</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:51:14
 */
public interface ActionInterceptor {
    /**
     * ִ��ÿ��module֮ǰ
     * @return boolean �Ƿ����ִ�б�־��trueΪ����ִ�У�falseΪִֹͣ��
     * */
    public boolean beforeAction(Action action,Message msg);
    /**
     * ִ��ÿ��module֮��
     * @param Action action ģ����
     * @param msg Message
     * @param result String  ִ�н��
     * */
    public void afterAction(Action action,Message msg,int result);
}
