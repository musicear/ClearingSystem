package resoft.tips.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>���ڸ�����</p>
 * Author: liguoyin
 * Date: 2007-6-13
 * Time: 16:13:17
 */
public class DateTimeUtil {
    /**
     * ���ص������ڡ���ʽΪyyyyMMdd
     * */
    public static String getDateString() {
        return getTimeByFormat("yyyyMMdd");
    }
    /**
     * ���ص�������ʱ�䡣��ʽΪyyyy-MM-dd hh:mm:ss
     * */
    public static String getDateTimeString() {
        return getTimeByFormat("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * �����ƶ���ʽ���ص�ǰ����ʱ��
     * */
    public static String getTimeByFormat(String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
