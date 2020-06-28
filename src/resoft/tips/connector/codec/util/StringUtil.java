// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package resoft.tips.connector.codec.util;


public class StringUtil
{

	public StringUtil()
	{
	}

	public static String rightPad(String oldStr, int toLength, char fillChar)
	{
		int length = oldStr.getBytes().length;
		if (length == toLength)
			return oldStr;
		if (length < toLength)
		{
			for (int i = length; i < toLength; i++)
				oldStr = oldStr + fillChar;

			return oldStr;
		} else
		{
			throw new RuntimeException("字符串" + oldStr + "长度超出指定长度" + toLength);
		}
	}

	public static String leftPad(String oldStr, int toLength, char fillChar)
	{
		int length = oldStr.getBytes().length;
		if (length == toLength)
			return oldStr;
		if (length < toLength)
		{
			for (int i = length; i < toLength; i++)
				oldStr = fillChar + oldStr;

			return oldStr;
		} else
		{
			throw new RuntimeException("字符串" + oldStr + "长度超出指定长度" + toLength);
		}
	}

	public static boolean isEmpty(String str)
	{
		return str == null || "".equals(str);
	}
}