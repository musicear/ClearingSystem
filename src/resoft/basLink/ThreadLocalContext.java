package resoft.basLink;

/**
 * function:�ֲ߳̾�����,�洢��ǰ�̵߳������Ļ���
 * User: albert lee
 * Date: 2005-10-12
 * Time: 15:46:22
 */
public class ThreadLocalContext {
    private ThreadLocalContext() {
    }
    /**
     * �õ�Ψһʵ��
     * */
    public static ThreadLocalContext getInstance() {
        return instance;
    }
    /**
     * ���ñ�����ID
     * */
    public void setRequestId(String id) {
        Context ctx = getContext();
        ctx.setRequestId(id);
        setContext(ctx);
    }
    /**
     * �õ�������ID
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
     * ���ñ��߳������Ļ���
     * */
    public void setContext(Context context) {
        threadLocal.set(context);
    }
    /**
     * �õ����߳������Ļ���
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
