package resoft.xlink.transaction;

/**
 * <p>交易不存在的异常</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 5:15:30
 */
public class TransactionNotExistsException extends Exception{
    public TransactionNotExistsException(String transCode) {
        this.transCode = transCode;
    }
    public String getMessage() {
        return transCode;
    }
    private String transCode;
}
