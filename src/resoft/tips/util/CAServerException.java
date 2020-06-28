// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   CAServerException.java

package resoft.tips.util;


public class CAServerException extends RuntimeException
{

    public static final long CAUSE_UNKNOWN = 0L;
    public static final long CAUSE_DESTROY_CERT = -10010L;
    private long errorCode;

    public CAServerException(String p_message)
    {
        super(p_message);
    }

    public CAServerException(String p_message, long _errorCode)
    {
        super(p_message);
        errorCode = _errorCode;
    }

    public String getDescription()
    {
        return "调用CA服务器端接口（Java）错误：";
    }

    public long getErrorCode()
    {
        return errorCode;
    }
}
