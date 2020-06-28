// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IntTypeConverter.java

package resoft.tips.connector.codec.converter;

import resoft.tips.connector.codec.config.MessageElement;

// Referenced classes of package resoft.tips.connector.codec.converter:
//			AbstractTypeConverter

public class IntTypeConverter extends AbstractTypeConverter
{

	public IntTypeConverter()
	{
	}

	public String toString(Object value, MessageElement element)
	{
		Object fieldValue = getFieldValue(value, element);
		String sText = fieldValue != null ? fieldValue.toString() : "";
		return sText;
	}

	public Object valueOf(String value, MessageElement element)
	{
		if (value == null)
			return null;
		String sText = value.replaceAll("^0+", "");
		if (sText.length() == 0)
			return new Integer(0);
		else
			return Integer.valueOf(sText);
	}
}