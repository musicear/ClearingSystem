package resoft.xlink.comm;

/**
 * <p>���ݸ��ֲ��Խ��в�ͬ�Ľ�������</p>
 * <p>������ݲ�ͬ�Ŀͻ���IP��ѡ��ͬ�Ľ�������ȷ�����Protocol�����ThreadLocal</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 0:03:39
 */
public interface Dispatcher {
    /**
     * ���д������趨֮Protocol�洢��ThreadLocal
     * */
    public void process();
}
