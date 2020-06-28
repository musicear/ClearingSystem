// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VarStringTypeConverter.java

package resoft.tips.connector.codec.converter;

import resoft.tips.connector.codec.config.MessageElement;

// Referenced classes of package resoft.tips.connector.codec.converter:
//			AbstractTypeConverter

public class VarStringTypeConverter extends AbstractTypeConverter
{

	public VarStringTypeConverter()
	{
	}

	public String toString(Object value, MessageElement element)
	{
		Object fieldValue = getFieldValue(value, element);
		int format = Integer.parseInt(element.getFormat());
		String sText = fieldValue != null ? leftPad(fieldValue.toString(), format + fieldValue.toString().getBytes().length, ' ') : "";
		return rightPad(sText, element.getLength(), ' ');
	}

	public Object valueOf(String value, MessageElement element)
	{
		throw new RuntimeException("varstring不支持解码!");
	}
}