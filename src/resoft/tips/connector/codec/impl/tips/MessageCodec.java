// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessageCodec.java

package resoft.tips.connector.codec.impl.tips;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import resoft.tips.connector.codec.StandardMessageCodec;

public class MessageCodec extends StandardMessageCodec
{

	private static final Logger logger;

	public MessageCodec()
	{
		Map txCodeNames = new HashMap();
		txCodeNames.put("PP003", "PP003");
		txCodeNames.put("TXCODE", "TXCODE");
		txCodeNames.put("tranNo", "tranNo");
		txCodeNames.put("CZCODE", "CZCODE");
		txCodeNames.put("CODE", "CODE");
		setTxCodeName(txCodeNames);
	}

	protected void buildFailureResponse(StringBuffer sb, String responseCode)
	{
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<bob>");
		sb.append("<pub><key><name>PP039</name>");
		sb.append("<value>" + responseCode + "</value></key></pub>");
		sb.append("</bob>");
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.codec.impl.tips.MessageCodec.class);
	}
}