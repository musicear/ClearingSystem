package junit;

import junit.framework.TestCase;
import resoft.basLink.util.MessageSendException;
import resoft.basLink.util.MessageSender;
import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-7-19
 * Time: 16:34:20
 */
public class TestMessageSenderUtil extends TestCase {
    public void test() throws MessageSendException {
        MessageSender sender = MessageSenderUtil.getMessageSender();
        assertEquals("resoft.tips.bankImpl.MessageSenderImpl", sender.getClass().getName());
        Message msg = new DefaultMessage();
        msg.set("TransCode", "1300");
        msg.set("TraDate", "20070810");
        Message returnMsg = sender.send(msg);
        assertNotNull(returnMsg);
    }

    public void testSend() throws Exception {
        MessageSender sender = MessageSenderUtil.getMessageSender();
        Message msg = new DefaultMessage();
        msg.set("I060", "108");
        msg.set("_jgm", "9400740101");
        msg.set("_jym", "0805");
        msg.set("_gyh", "11");
        msg.set("SSJY", "2604");
        msg.set("__ZH", "0000000035280011");
        msg.set("__XH", "0");

        msg.set("交易码", "s0805");
        Message returnMsg = sender.send(msg);
        assertNotNull(returnMsg);
        assertEquals("4444", returnMsg.getString("_hostcode"));
        assertEquals("柜员没处于签到状态不能处理业务,请通知中心机房", returnMsg.getString("_errmsg"));
    }

    public void testSuccess() throws Exception {
        MessageSender sender = MessageSenderUtil.getMessageSender();
        Message msg = new DefaultMessage();
        msg.set("_jym", "0825");
        msg.set("_jgm", "9400740001");
        msg.set("_tradeseq", "1");
        msg.set("_hostseqno", "");
        msg.set("_gyh", "24");
        msg.set("_ttyno", "ttyp16");
        msg.set("SQGY", "1");
        msg.set("SQMM", "QATCDnqX");
        msg.set("_SQM", "");
        msg.set("I082","1");
        msg.set("JDRQ", "");
        msg.set("S100", "");
        msg.set("F084", "10.00");
        msg.set("I060", "119");
        msg.set("__XH", "");
        msg.set("__ZH", "0000000035280011");

        msg.set("交易码", "s0805");
        Message returnMsg = sender.send(msg);
        assertNotNull(returnMsg);
        assertEquals("4444", returnMsg.getString("_hostcode"));
        assertEquals("柜员没处于签到状态不能处理业务,请通知中心机房", returnMsg.getString("_errmsg"));
    }

    public void testSendExit() throws Exception {
        MessageSender sender = MessageSenderUtil.getMessageSender();
        Message msg = new DefaultMessage();
        msg.set("交易码", "zqyw");
        msg.set("_jym", "1091");
        msg.set("_gyh", "8003");
        Message returnMsg = sender.send(msg);
        assertNotNull(returnMsg);
        assertEquals("0000", msg.getString("_hostcode"));
        assertEquals("柜员签退成功", msg.get("_errmsg"));
    }


    public void testSend0930() throws Exception {
        MessageSender sender = MessageSenderUtil.getMessageSender();
        Message msg = new DefaultMessage();
        msg.set("交易码", "s0930");
        msg.set("_jym", "0930");
        msg.set("__XH", "0");
        msg.set("__ZH","0000003563220011");
        msg.set("_HBH","1");
        msg.set("SSJY","0801");
        Message returnMsg = sender.send(msg);
        assertNotNull(returnMsg);
        assertEquals("0000", returnMsg.getString("_hostcode"));
        //assertEquals("柜员签退成功", returnMsg.get("_errmsg"));
    }
}
