// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BigDecimalTypeConverter.java

package resoft.tips.connector.codec.converter;

import java.math.BigDecimal;

import resoft.tips.connector.codec.config.MessageElement;

// Referenced classes of package resoft.tips.connector.codec.converter:
//			AbstractTypeConverter

public class BigDecimalTypeConverter extends AbstractTypeConverter
{

	public BigDecimalTypeConverter()
	{
	}

	public String toString(Object value, MessageElement element)
	{
		Object fieldValue = getFieldValue(value, element);
		BigDecimal dFieldValue = null;
		if (fieldValue == null)
			dFieldValue = new BigDecimal(0.0D);
		if (fieldValue != null)
			dFieldValue = ((BigDecimal)fieldValue).setScale(Integer.parseInt(element.getFormat()), 4);
		if (!"mb".equals(element.getType()))
		{
			String sText = dFieldValue != null ? dFieldValue.toString().replaceAll("\\.", "") : "";
			return sText;
		} else
		{
			return dFieldValue.toString();
		}
	}

	public Object valueOf(String value, MessageElement element)
	{
		if (value == null)
			return null;
		String sText = value;
		if (value.indexOf(".") != -1)
		{
			return new BigDecimal(sText);
		} else
		{
			int decimalStartIndex = sText.length() - Integer.parseInt(element.getFormat());
			String decimalPart = sText.substring(decimalStartIndex, sText.length());
			String intPart = sText.substring(0, decimalStartIndex);
			intPart = intPart.replaceAll("^0+", "");
			intPart = intPart.length() != 0 ? intPart : "0";
			return new BigDecimal(intPart + "." + decimalPart);
		}
	}

	public static void main(String args1[])
	{
	}
}