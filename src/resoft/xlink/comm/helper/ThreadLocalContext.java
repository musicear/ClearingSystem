package resoft.xlink.comm.helper;

/**
 * <p>线程局部变量,存储当前线程的上下文环境</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:54:45
 */
public class ThreadLocalContext {
    private ThreadLocalContext() {
    }
    /**
     * 得到唯一实例
     * */
    public static ThreadLocalContext getInstance() {
        return instance;
    }
    /**
     * 设置本请求ID
     * */
    public void setRequestId(String id) {
        Context ctx = getContext();
        ctx.setRequestId(id);
        setContext(ctx);
    }
    /**
     * 得到本请求ID
     * */
    public String getRequestId() {
        Context ctx = getContext();
        if(ctx==null) {
            return null;
        } else {
            return ctx.getRequestId();
        }
    }
    /**
     * 设置本线程上下文环境
     * */
    public void setContext(Context context) {
        threadLocal.set(context);
    }
    /**
     * 得到本线程上下文环境
     * */
    public Context getContext() {
        Context ctx = (Context) threadLocal.get();
        if(ctx==null) {
            ctx = new Context();
            setContext(ctx);
        }
        return ctx;
    }

    private ThreadLocal threadLocal = new ThreadLocal();

    private static ThreadLocalContext instance = new ThreadLocalContext();
}
