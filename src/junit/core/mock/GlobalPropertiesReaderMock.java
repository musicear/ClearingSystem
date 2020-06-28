package junit.core.mock;

import java.util.HashMap;
import java.util.Map;

import resoft.xlink.comm.GlobalPropertiesReader;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-14
 * Time: 20:49:30
 */
public class GlobalPropertiesReaderMock implements GlobalPropertiesReader {
    public Map getAllProperties() {
        Map map = new HashMap();
        map.put("name","Albert Li");
        return map;
    }
}
