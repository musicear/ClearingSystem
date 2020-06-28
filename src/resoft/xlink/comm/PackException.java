package resoft.xlink.comm;

/**
 * <p>打包解包异常</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:39:11
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
