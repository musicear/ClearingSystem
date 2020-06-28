// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeConverter.java

package resoft.tips.connector.codec.converter;

import resoft.tips.connector.codec.config.MessageElement;

public interface TypeConverter
{

	public abstract String toString(Object obj, MessageElement messageelement);

	public abstract Object valueOf(String s, MessageElement messageelement);
}