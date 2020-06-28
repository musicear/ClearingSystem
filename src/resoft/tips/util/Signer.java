package resoft.tips.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p/>
 * ǩ����
 * </p>
 * User: tangzhiyu Date: 2007-07-17 Time: 14:23:20
 */
public class Signer {
    private static final Log logger = LogFactory.getLog(Signer.class);

    private static Signer instance = new Signer();


    private Signer() {
    }

    /**
     * �õ�Ψһʵ��
     */
    public static Signer getInstance() {
        return instance;
    }

    /**
     * ��������ԭ�ĵ�����ǩ��
     */
    public String detachSign(String xml) {
        CAServerUtil util = new CAServerUtil();
        int iEnd = xml.lastIndexOf("</CFX>");
        if (iEnd < 0) {
            StringBuffer msg = new StringBuffer();
            msg.append("���ұ��������,End:").append(iEnd);
            logger.error(msg.toString());
            throw new CAServerException(msg.toString());
        }
        String data = xml.substring(0, iEnd + 6);
        String sign;
        sign = util.DetachSign(data);
        return sign;
    }

    /**
     * У��ǩ��
     */
    public boolean verifySign(String xml) {
        CAServerUtil util = new CAServerUtil();
        String signData;
        int dataEndIndex = xml.lastIndexOf("</CFX>");
        int signBeginIndex = xml.lastIndexOf("<!--");
        if (signBeginIndex < 0) {
            //����û��ǩ����Ϣ
            return true;
        }
        int signEndIndex = xml.lastIndexOf("-->");
        signData = xml.substring(signBeginIndex + 4, signEndIndex);

        if (dataEndIndex < 0) {
            StringBuffer msg = new StringBuffer();
            msg.append("���ұ��������,End:").append(dataEndIndex);
            logger.error(msg.toString());
            throw new CAServerException(msg.toString());
        }
        String data = xml.substring(0, dataEndIndex + 6);

        return util.VerifyDetachSign(signData, data);
    }

}
