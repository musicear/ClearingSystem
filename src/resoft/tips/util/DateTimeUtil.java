package resoft.tips.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>日期辅助类</p>
 * Author: liguoyin
 * Date: 2007-6-13
 * Time: 16:13:17
 */
public class DateTimeUtil {
    /**
     * 返回当日日期。格式为yyyyMMdd
     * */
    public static String getDateString() {
        return getTimeByFormat("yyyyMMdd");
    }
    /**
     * 返回当日日期时间。格式为yyyy-MM-dd hh:mm:ss
     * */
    public static String getDateTimeString() {
        return getTimeByFormat("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 根据制定格式返回当前日期时间
     * */
    public static String getTimeByFormat(String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
