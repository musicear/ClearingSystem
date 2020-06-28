package resoft.basLink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * function: Э�顣��ȡ��������
 * User: albert lee
 * Date: 2005-10-18
 * Time: 14:14:35
 */
public interface Protocol {
    /**
     * �Ƿ�Ϊ������
     * */
    public boolean isKeepAlive();
    /**
     * ��ͬЭ���ȡ��ʽ��ͬ���е����ս�����е��Ƕ����ģ��е����ڱ���ǰ��һ����
     * */
    public byte[] read(InputStream inputStream) throws IOException;
    /**
     * д��Ӧ�ؿͻ���
     * */
    public void write(OutputStream outputStream,byte[] buffer) throws IOException;
}
