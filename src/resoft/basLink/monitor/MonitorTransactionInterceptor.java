package resoft.basLink.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import resoft.basLink.Message;
import resoft.basLink.TransactionInterceptor;

/**
 * function: 监控Module
 * User: albert lee
 * Date: 2005-9-14
 * Time: 10:53:06
 */
public class MonitorTransactionInterceptor implements TransactionInterceptor{

    public boolean before(Message msg) {
        return true;
    }

    public void after(Message msg) {
        //设置交易操作时间，取当前时间。格式为yyyy/mm/dd hh:mm:ss
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        msg.setValue("当前交易时间",df.format(now));
        Monitor.getInstance().putMessage(msg);
    }
}
