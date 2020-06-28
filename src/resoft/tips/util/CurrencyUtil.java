package resoft.tips.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <p>½ð¶î¸ñÊ½</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 19:09:59
 */
public class CurrencyUtil {
    public static String getCurrencyFormat(String strAmt) {
        double amt = Double.parseDouble(strAmt);
        NumberFormat nf = new DecimalFormat("0.00");
        return nf.format(amt);
    }
}
