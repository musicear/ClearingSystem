package resoft.basLink.util;

/**
 * <p>function:���ͽӿڡ���ʵ�ַ���Socket��JMS��Ϣ��</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-9</p>
 * <p>Time: 10:30:55</p>
 */
public interface Sender {
    /**
     * ��������
     * */
    public void setProperty(String name,String value) ;
    /**
     * ��������
     * */
    public byte[] send(byte[] b) throws Exception;
}
