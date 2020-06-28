package resoft.tips.bankImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message;

/**
 * <p>Message�еļ�ֵת��������MessageSender�С���������ֵת��Ϊ������ר�ü�ֵ</p>
 * Author: liguoyin
 * Date: 2007-8-16
 * Time: 14:58:35
 */
public class KeyMapper {
    private static Configuration conf = Configuration.getInstance("keyMapper");

    public void process(Message msg) {
        String transCode = msg.getString("������");
        //�������ֵ����Map��
        Map newMap = new HashMap();
        for (Iterator itr = conf.listAllProperties(transCode).iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            String value = conf.getProperty(transCode, key);
            if(value.startsWith("${") && value.endsWith("}")) {
                //���ʽ
                String keyInMsg = value.substring(2,value.length() - 1);
                newMap.put(key,msg.getString(keyInMsg));
                //msg.set(keyInMsg,"");
            } else {
                newMap.put(key,value);
            }
        }
        //ɾ��ԭ�����м�ֵ
        msg.clear();
        //���빹��õļ�ֵ
        for(Iterator itr = newMap.keySet().iterator();itr.hasNext();) {
            String key = (String) itr.next();
            String value = (String) newMap.get(key);
            msg.set(key,value);
        }
    }
}
