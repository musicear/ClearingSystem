package resoft.tips.util;

/**
 * <p>Êý×ÖÇ©Ãû´íÎó</p>
 * Author: liguoyin
 * Date: 2007-8-11
 * Time: 18:20:14
 */
public class SignException extends RuntimeException{
    public SignException(String errCode,String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getMessage() {
        return errMsg;
    }

    private String errCode;
    private String errMsg;
}
