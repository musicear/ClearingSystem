package resoft.xlink.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>ͨѶ��Э�顣��ȡ��д����������</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:52:19
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
