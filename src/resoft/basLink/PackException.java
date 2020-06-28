package resoft.basLink;

/**
 * function: 打包解包异常
 * User: albert lee
 * Date: 2005-8-25
 * Time: 10:04:39
 */
public class PackException extends RuntimeException{
    public PackException() {
        super();
    }
    public PackException(String message) {
        super(message);
    }
    public PackException(String str,Exception cause) {
        super(str,cause);
    }
}
