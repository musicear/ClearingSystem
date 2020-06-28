package resoft.basLink.packer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import resoft.basLink.util.AbstractCacheTemplate;

/**
 * function: ��ʽ�����ļ�����
 * User: albert lee
 * Date: 2005-9-21
 * Time: 14:27:39
 */

public class CfxFmtFilesCache extends AbstractCacheTemplate{
    private static CfxFmtFilesCache ourInstance = new CfxFmtFilesCache();

    public static CfxFmtFilesCache getInstance() {
        return ourInstance;
    }
    private CfxFmtFilesCache() {
    }
    /**
     * �õ���ʽ�����ļ����ݡ��������д��ڣ���ֱ��ȡ�Ի��棻�����ȡ�ļ������û���
     * */
    public String getCfxFmtFile(String filePath) throws IOException{
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
