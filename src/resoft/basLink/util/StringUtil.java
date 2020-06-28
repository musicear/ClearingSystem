package resoft.basLink.util;

/**
 * function: 工具类
 * User: albert lee
 * Date: 2005-9-28
 * Time: 13:45:41
 */
public class StringUtil {
    /**
     * 得到字符串后的数字.比如"预算科目1",则得到1
     * */
    public static int getSuffixAsNumber(String str) {
        int numBegin = str.length() - 1;
        while(numBegin>0) {
            char c = str.charAt(numBegin);
            if(c>='0' && c<='9') {
                numBegin --;
            } else {
                break;
            }
        }
        if(numBegin + 1 >=str.length() ) {
            return 0;
        } else {
            return Integer.parseInt(str.substring(numBegin + 1,str.length()));
        }        
    }
}
