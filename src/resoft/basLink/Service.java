package resoft.basLink;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-22
 * Time: 8:52:12
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
}
