package resoft.tips.web.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p/>
 * ��TIPS���Ӳ���:����9005����
 * </p>
 * Author: chenjianping Date: 2007-8-09 Time: 15:19:58
 */
public class TipsConTest extends AbstractAction {
    private static final Log logger = LogFactory.getLog(TipsConTest.class);

    public String execute() throws SQLException {
        Message msg = new Message();
        Message returnData;
        
        System.out.println("insert TipsConTest....");
        try {
            returnData = TransCommUtil.send("send9005", msg);
        } catch (IOException e) {
            setMessage("���Ӻ�̨����ϵͳʧ��");
            logger.error("���Ӻ�̨����ϵͳʧ��", e);
            return ERROR;
        }
        String result = returnData.getValue("Result");
        if (result.equals("Y")) {
            result = "���Ӳ��Գɹ�";
        } else if (result.equals("N")) {
            result = "���Ӳ���ʧ��";
        }
        setMessage(result);
        return SUCCESS;
    }


}
