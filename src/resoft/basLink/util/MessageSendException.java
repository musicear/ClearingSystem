package resoft.basLink.util;

/**
 * function:���������쳣
 * User: albert lee
 * Date: 2005-10-27
 * Time: 17:20:48
 */
public class MessageSendException extends Exception {
    public MessageSendException(Throwable cause) {
        super(cause);
        errorMessage = cause.getMessage();
    }

    public MessageSendException(String errCode, String errorMessage) {
        this.errCode = errCode;
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    private String errCode;  //�������
    private String errorMessage;   //��������
}
