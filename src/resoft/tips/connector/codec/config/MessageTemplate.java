// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessageTemplate.java

package resoft.tips.connector.codec.config;

import java.util.List;

public class MessageTemplate
{

	private List mainItems;
	private List detailItems;

	public MessageTemplate()
	{
	}

	public List getMainItems()
	{
		return mainItems;
	}

	public void setMainItems(List mainItems)
	{
		this.mainItems = mainItems;
	}

	public List getDetailItems()
	{
		return detailItems;
	}

	public void setDetailItems(List detailItems)
	{
		this.detailItems = detailItems;
	}
}