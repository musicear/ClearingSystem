package resoft.basLink.util;

/**
 * function: ������
 * User: albert lee
 * Date: 2005-9-28
 * Time: 13:45:41
 */
public class StringUtil {
    /**
     * �õ��ַ����������.����"Ԥ���Ŀ1",��õ�1
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
