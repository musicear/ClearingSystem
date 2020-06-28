// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DatetimeTypeConverter.java

package resoft.tips.connector.codec.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import resoft.tips.connector.codec.config.MessageElement;

// Referenced classes of package resoft.tips.connector.codec.converter:
//			AbstractTypeConverter

public class DatetimeTypeConverter extends AbstractTypeConverter
{

	public DatetimeTypeConverter()
	{
	}

	public String toString(Object value, MessageElement element)
	{
		Object fieldValue = getFieldValue(value, element);
		String sText = "";
		if (fieldValue != null)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(element.getFormat());
			try
			{
				sText = formatter.format(fieldValue);
			}
			catch (Exception e)
			{
				throw new RuntimeException("解析日期字段失败！[value=" + fieldValue.toString() + "]");
			}
		}
		return sText;
	}

	public Object valueOf(String value, MessageElement element)
	{
		try{
		String sText;
		SimpleDateFormat formatter;
		if (value == null)
			return null;
		sText = value.trim();
		if (sText.length() == 0)
			return null;
		formatter = new SimpleDateFormat(element.getFormat());
		return formatter.parse(sText);
		}catch(ParseException e){
			throw new RuntimeException("解析日期字段失败![value=" + value + ",format=" + element.getFormat() + "]");
		}
		
	}
}