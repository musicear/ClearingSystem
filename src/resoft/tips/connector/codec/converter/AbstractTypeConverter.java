// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractTypeConverter.java

package resoft.tips.connector.codec.converter;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;

import ognl.Ognl;
import ognl.OgnlException;
import resoft.tips.connector.codec.config.MessageElement;
import resoft.tips.connector.codec.util.StringUtil;

// Referenced classes of package resoft.tips.connector.codec.converter:
//			TypeConverter

public abstract class AbstractTypeConverter
	implements TypeConverter
{

	private static final Logger logger;

	public AbstractTypeConverter()
	{
	}

	protected Object getPropertyValue(Object obj, String fieldName)
	{
		try{
		Class clazz;
		String getMethodName;
		Method getMethod;
		clazz = obj.getClass();
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		getMethodName = "get" + firstLetter + fieldName.substring(1);
		getMethod = null;
		getMethod = clazz.getMethod(getMethodName, new Class[0]);
		return getMethod.invoke(obj, new Object[0]);
		}catch(Exception e){
			return null;
		}	
	}

	protected Object getFieldValue(Object value, MessageElement element)
	{
		Object fieldValue = null;
		if (element.getDataSource().trim().equals("xml"))
		{
			fieldValue = element.getValue();
		} else
		{
			String names[] = element.getName().split(",");
			for (int i = 0; i < names.length; i++)
			{
				if (element.getDataSource().trim().equals("map"))
					fieldValue = ((Map)value).get(names[i]);
				else
				if (element.getDataSource().trim().equals("bean"))
					try
					{
						fieldValue = Ognl.getValue(names[i], value);
					}
					catch (OgnlException e)
					{
						if (i == names.length - 1)
							logger.error("获取类" + value.getClass().getName() + "的" + names[i] + "属性值失败!", e);
					}
				if (fieldValue != null)
					break;
			}

		}
		return fieldValue;
	}

	protected String rightPad(String oldStr, int toLength, char fillChar)
	{
		return StringUtil.rightPad(oldStr, toLength, fillChar);
	}

	protected static String leftPad(String oldStr, int toLength, char fillChar)
	{
		return StringUtil.leftPad(oldStr, toLength, fillChar);
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.codec.converter.AbstractTypeConverter.class);
	}
}