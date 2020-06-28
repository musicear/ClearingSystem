package resoft.basLink.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * <p>文件缓存</p>
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
     * 得到格式定义文件内容。若缓存中存在，则直接取自缓存；否则读取文件并放置缓存
     * */
    public String getCfxFmtFile(String filePath) throws IOException {
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
