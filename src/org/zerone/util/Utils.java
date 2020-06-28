// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Utils.java

package org.zerone.util;


public class Utils
{

    public Utils()
    {
    }

    public static Object isNullThen(Object first, Object second)
    {
        return first != null && !first.equals("") ? first : second;
    }
}
