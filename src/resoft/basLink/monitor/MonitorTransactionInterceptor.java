package resoft.basLink.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import resoft.basLink.Message;
import resoft.basLink.TransactionInterceptor;

/**
 * function: ���Module
 * User: albert lee
 * Date: 2005-9-14
 * Time: 10:53:06
 */
public class MonitorTransactionInterceptor implements TransactionInterceptor{

    public boolean before(Message msg) {
        return true;
    }

    public void after(Message msg) {
        //���ý��ײ���ʱ�䣬ȡ��ǰʱ�䡣��ʽΪyyyy/mm/dd hh:mm:ss
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        msg.setValue("��ǰ����ʱ��",df.format(now));
        Monitor.getInstance().putMessage(msg);
    }
}
