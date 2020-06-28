package resoft.basLink.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * <p>�ļ�����</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 23:30:35
 */
public class FilesCache extends AbstractCacheTemplate{
    private static FilesCache ourInstance = new FilesCache();

    public static FilesCache getInstance() {
        return ourInstance;
    }
    private FilesCache() {
    }
    /**
     * �õ���ʽ�����ļ����ݡ��������д��ڣ���ֱ��ȡ�Ի��棻�����ȡ�ļ������û���
     * */
    public String getCfxFmtFile(String filePath) throws IOException {
        return (String) get(filePath);
    }
    /**
     * �����ļ�
     * */
    protected Object load(String filePath) throws Exception{
        StringBuffer temp = new StringBuffer();
        //��ȡ�ļ�����
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(filePath);
        if(is==null) {
            throw new IOException("δ�ҵ��ļ�:" + filePath);
        }
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
        String line = lnr.readLine();
        while(line!=null) {
            temp.append(line);
            line = lnr.readLine();
        }
        is.close();
        return temp.toString();
    }
}
