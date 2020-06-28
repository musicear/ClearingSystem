package junit;

import junit.framework.TestCase;
import resoft.tips.util.CurrencyUtil;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 19:14:18
 */
public class TestCurrencyUtil extends TestCase {
    public void testGetFormat() {
        assertEquals("12.00", CurrencyUtil.getCurrencyFormat("12"));
    }
}
