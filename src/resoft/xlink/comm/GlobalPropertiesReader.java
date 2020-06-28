package resoft.xlink.comm;

import java.util.Map;

/**
 * <p>全局参数读取。可用来在流程描述文件中用${name}方式设置属性</p>
 * Author: liguoyin
 * Date: 2007-8-14
 * Time: 18:42:40
 */
public interface GlobalPropertiesReader {
    public Map getAllProperties();
}
