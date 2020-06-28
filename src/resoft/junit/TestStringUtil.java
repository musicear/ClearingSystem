package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.util.StringUtil;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-28
 * Time: 13:47:15
 */
public class TestStringUtil extends TestCase{
    public void testSuffix() {
        assertEquals(1,StringUtil.getSuffixAsNumber("Ԥ���Ŀ1"));
        assertEquals(0,StringUtil.getSuffixAsNumber("Ԥ���Ŀ"));
        assertEquals(23,StringUtil.getSuffixAsNumber("Ԥ���Ŀ23"));
    }
}
