package resoft.xlink.comm;

/**
 * <p>����</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:41:56
 */
public interface Service {
    /**
     * ��������
     * */
    public void start() throws Exception;
    /**
     * ֹͣ����
     * */
    public void stop() throws Exception;
    /**
     * �����Ƿ���������
     * */
    public boolean isRunning();
    /**
     * ��������
     * */
    public void setProperty(String name,String value);
}
