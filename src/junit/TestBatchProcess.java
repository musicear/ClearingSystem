package junit;

import java.sql.Connection;

import junit.framework.TestCase;
import resoft.basLink.util.DBUtil;
import resoft.tips.action.ProcessBatchFile;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.comm.impl.GenericXmlMessage;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-6-11
 * Time: 2:25:26
 */
public class TestBatchProcess extends TestCase {
    public void test() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        Message msg = new GenericXmlMessage();
        msg.set("�����ļ�","c:\\temp\\20070608020517000.xml");
        msg.set("��Ѻ���", Boolean.TRUE);
        Action action = new ProcessBatchFile();
        action.execute(msg);
    }
    public void test3102Pooling() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        Controller controller = new Controller();
        Message msg = new DefaultMessage();
        msg.set("������","3102Pooling");
        controller.setNameOfTransCode("������");
        controller.execute(msg);
    }
    public void test2102() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        Controller controller = new Controller();
        Message msg = new DefaultMessage();
        msg.set("������","2102");
        controller.setNameOfTransCode("������");
        controller.execute(msg);
    }
    public void test3111() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        Message msg = new GenericXmlMessage();
        msg.set("�����ļ�","C:\\myWork\\development\\TIPSǰ��\\a\\������\\03-���\\���Ա���\\������˰Ʊ��ϸ����֪ͨ(3111).xml");
        msg.set("��Ѻ���", Boolean.TRUE);
        Action action = new ProcessBatchFile();
        action.execute(msg);
    }
    public void test9100() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        Message msg = new GenericXmlMessage();
        msg.set("�����ļ�","C:\\myWork\\development\\TIPSǰ��\\a\\������\\03-���\\���Ա���\\�������ݸ���֪ͨ(9100).xml");
        msg.set("��Ѻ���", Boolean.TRUE);
        Action action = new ProcessBatchFile();
        action.execute(msg);
    }
}
