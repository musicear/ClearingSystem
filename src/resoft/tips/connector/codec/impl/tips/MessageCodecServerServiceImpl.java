// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessageCodecServerServiceImpl.java

package resoft.tips.connector.codec.impl.tips;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import resoft.tips.connector.codec.impl.AbstractMessageCodecServerService;
import resoft.tips.connector.codec.util.StringUtil;
import resoft.tips.connector.message.ReturnMessage;

// Referenced classes of package resoft.tips.connector.codec.impl.tips:
//			MessageCodec

public abstract class MessageCodecServerServiceImpl extends AbstractMessageCodecServerService
{

	private static final Logger logger;
	private MessageCodec messageCodec;

	public MessageCodecServerServiceImpl()
	{
		messageCodec = new MessageCodec();
	}

	public ReturnMessage decodeMessage(String code, String message)
		throws Exception
	{
		String msg = message.substring(8);
		return messageCodec.decodeTipsServerMessage(code, msg);
	}

	public ReturnMessage decodeMessage(String code, Document doc)
		throws Exception
	{
		return messageCodec.decodeTipsServerMessage(code, doc);
	}

	public String encodeMessage(String code, ReturnMessage request)
		throws Exception
	{
		String message = messageCodec.encodeServerMessage(code, request);
		logger.debug(code + " encode:\n" + message.toString());
		String bodyLength = String.valueOf(message.getBytes("utf-8").length);
		String bmsgHead = StringUtil.leftPad(bodyLength, 8, '0');
		message = new String(bmsgHead) + message;
		return message;
	}

	public String encodeMessage2(Document doc, String code, ReturnMessage request)
		throws Exception
	{
		String message = messageCodec.encodeServerMessage2(doc, code, request);
		logger.debug(code + " encode:\n" + message.toString());
		String bodyLength = String.valueOf(message.getBytes().length);
		String bmsgHead = StringUtil.leftPad(bodyLength, 8, '0');
		message = new String(bmsgHead) + message;
		return message;
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.codec.impl.tips.MessageCodecServerServiceImpl.class);
	}
}