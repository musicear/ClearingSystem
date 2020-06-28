package junit;

import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.tips.bankImpl.SyncSocketMessageSender;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-4
 * Time: 12:38:54
 */
public class TestSyncSocketMessageSender {
    public static void main(String[] args) throws MessageSendException {
        MessageSender sender = new SyncSocketMessageSender();
        sender.setProperty("host","127.0.0.1");
        sender.setProperty("port","1500");
        sender.setProperty("packager","resoft.tips.bankImpl.hbbank.HBBankPackager");
        sender.setProperty("lengthAccessor","resoft.tips.bankImpl.RawLengthAccessor");
        Message msg = new DefaultMessage();
        msg.set("½»Ò×Âë","T8001");
        //msg.set("TransCode","2100");
        msg.set("TaxOrgCode","1234");
        Message returnMsg = sender.send(msg);
        System.out.println(returnMsg.getString("ReturnResult"));
    }
}
