// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ThreadLocalContext.java

package test;


// Referenced classes of package resoft.xlink.comm.helper:
//            Context

public class ThreadLocalContext
{

    private ThreadLocalContext()
    {
        threadLocal = new ThreadLocal();
    }

    public static ThreadLocalContext getInstance()
    {
        return instance;
    }

    public void setRequestId(String id)
    {
        Context ctx = getContext();
        ctx.setRequestId(id);
        setContext(ctx);
    }

    public String getRequestId()
    {
        Context ctx = getContext();
        if(ctx == null)
            return null;
        else
            return ctx.getRequestId();
    }

    public void setContext(Context context)
    {
        threadLocal.set(context);
    }

    public Context getContext()
    {
        Context ctx = (Context)threadLocal.get();
        if(ctx == null)
        {
            ctx = new Context();
            setContext(ctx);
        }
        return ctx;
    }

    private ThreadLocal threadLocal;
    private static ThreadLocalContext instance = new ThreadLocalContext();

}
