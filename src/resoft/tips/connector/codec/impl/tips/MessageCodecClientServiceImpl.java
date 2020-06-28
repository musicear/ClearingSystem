// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessageCodecClientServiceImpl.java

package resoft.tips.connector.codec.impl.tips;

import org.apache.log4j.Logger;

import resoft.tips.connector.codec.impl.AbstractMessageCodecClientService;
import resoft.tips.connector.message.ReturnMessage;

// Referenced classes of package resoft.tips.connector.codec.impl.tips:
//			MessageCodec

public abstract class MessageCodecClientServiceImpl extends AbstractMessageCodecClientService
{

	private MessageCodec messageCodec;
	private static final Logger logger;

	public MessageCodecClientServiceImpl()
	{
		messageCodec = new MessageCodec();
	}

	public ReturnMessage decodeMessage(String code, String message)
		throws Exception
	{
		String msg = message.substring(10);
		return null;
	}

	public String encodeMessage(String code, ReturnMessage request)
		throws Exception
	{
		String message = "<?xml version='1.0' encoding='UTF-8'?><service><sys-header><data name='SYS_HEAD'><struct><data name='BRANCH_ID'><field length='6' scale='0' type='string'>51501</field></data><data name='MESSAGE_CODE'><field length='6' scale='0' type='string'>T1200</field></data><data name='USER_ID'><field length='30' scale='0' type='string'>00095</field></data></struct></data></sys-header><app-header><data name='APP_HEAD'><struct><data name='PAGE_END'><field length='15' scale='0' type='string'>0</field></data></struct></data></app-header><local-header><data name='LOCAL_HEAD'><struct/></data></local-header><body><data name='ContectCode'><field length='60' scale='0' type='string'>00001</field></data><data name='TaxPayCode'><field length='20' scale='0' type='string'>727409461</field></data><data name='AcctNo'><field length='12' scale='0' type='string'>1122334455</field></data></body></service>";
		return message;
	}

	public MessageCodec getMessageCodec()
	{
		return messageCodec;
	}

	public void setMessageCodec(MessageCodec messageCodec)
	{
		this.messageCodec = messageCodec;
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.codec.impl.tips.MessageCodecClientServiceImpl.class);
	}
}