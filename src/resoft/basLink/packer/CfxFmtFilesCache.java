package resoft.basLink.packer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import resoft.basLink.util.AbstractCacheTemplate;

/**
 * function: 格式定义文件缓存
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
     * 得到格式定义文件内容。若缓存中存在，则直接取自缓存；否则读取文件并放置缓存
     * */
    public String getCfxFmtFile(String filePath) throws IOException{
        return (String) get(filePath);
    }
    /**
     * 加载文件
     * */
    protected Object load(String filePath) throws Exception{
        StringBuffer temp = new StringBuffer();
        //读取文件内容
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(filePath);
        if(is==null) {
            throw new IOException("未找到文件:" + filePath);
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
