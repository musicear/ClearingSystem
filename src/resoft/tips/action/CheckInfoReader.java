package resoft.tips.action;

import java.io.IOException;

/**
 * <p>银行对账信息读取器</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 2:52:19
 */
public interface CheckInfoReader {
    /**
     * 是否有下一行，若有则移动到下移行。类似与java.sql.ResultSet
     */
    public boolean next();

    /**
     * 得到本行的指定记录
     */
    public String getString(String key);

    public void setFilePath(String filename) throws IOException;
}
