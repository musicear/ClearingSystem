package resoft.tips.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p/>
 * 签名类
 * </p>
 * User: tangzhiyu Date: 2007-07-17 Time: 14:23:20
 */
public class Signer {
    private static final Log logger = LogFactory.getLog(Signer.class);

    private static Signer instance = new Signer();


    private Signer() {
    }

    /**
     * 得到唯一实例
     */
    public static Signer getInstance() {
        return instance;
    }

    /**
     * 制作不带原文的数字签名
     */
    public String detachSign(String xml) {
        CAServerUtil util = new CAServerUtil();
        int iEnd = xml.lastIndexOf("</CFX>");
        if (iEnd < 0) {
            StringBuffer msg = new StringBuffer();
            msg.append("查找报文体错误,End:").append(iEnd);
            logger.error(msg.toString());
            throw new CAServerException(msg.toString());
        }
        String data = xml.substring(0, iEnd + 6);
        String sign;
        sign = util.DetachSign(data);
        return sign;
    }

    /**
     * 校验签名
     */
    public boolean verifySign(String xml) {
        CAServerUtil util = new CAServerUtil();
        String signData;
        int dataEndIndex = xml.lastIndexOf("</CFX>");
        int signBeginIndex = xml.lastIndexOf("<!--");
        if (signBeginIndex < 0) {
            //报文没有签名信息
            return true;
        }
        int signEndIndex = xml.lastIndexOf("-->");
        signData = xml.substring(signBeginIndex + 4, signEndIndex);

        if (dataEndIndex < 0) {
            StringBuffer msg = new StringBuffer();
            msg.append("查找报文体错误,End:").append(dataEndIndex);
            logger.error(msg.toString());
            throw new CAServerException(msg.toString());
        }
        String data = xml.substring(0, dataEndIndex + 6);

        return util.VerifyDetachSign(signData, data);
    }

}
