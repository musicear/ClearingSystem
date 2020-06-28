// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractServer.java

package resoft.tips.connector.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import resoft.tips.connector.codec.MessageCodecServerService;
import resoft.tips.connector.message.ReturnMessage;

// Referenced classes of package resoft.tips.connector.server:
//			Server

public abstract class AbstractServer
	implements Server
{

	private static final Logger logger;
	private String serverName;
	private MessageCodecServerService messageCodecServerService;
	private Map handlers;
	private Map txResponseCode;
	private int port;

	public AbstractServer()
	{
	}

	public void setMessageCodecServerService(MessageCodecServerService messageCodecServerService)
	{
		this.messageCodecServerService = messageCodecServerService;
	}

	public MessageCodecServerService getMessageCodecServerService()
	{
		return messageCodecServerService;
	}

	public Map getHandlers()
	{
		return handlers;
	}

	public void setHandlers(Map handlers)
	{
		this.handlers = handlers;
	}

	public String getSuccessCode()
	{
		return (String)txResponseCode.get("TX_SUCCESS");
	}

	public String getFailCode()
	{
		return (String)txResponseCode.get("TX_FAIL");
	}

	public String getFailCodeDecode()
	{
		return (String)txResponseCode.get("TX_FAIL_DECODE");
	}

	public String getFailCodeNotSupportedTx()
	{
		return (String)txResponseCode.get("TX_FAIL_NOTSUPPORTED_TX");
	}

	public Map getTxResponseCode()
	{
		return txResponseCode;
	}

	public void setTxResponseCode(Map txResponseCode)
	{
		this.txResponseCode = txResponseCode;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String messageReceived(String message)
		throws Exception
	{
		String code = "";
		String responseCode = getSuccessCode();
		ReturnMessage request = null;
		ReturnMessage response = null;
		ReturnMessage txResponse = null;
		logger.info("Sever Receved MSG:" + message);
		try
		{
			request = messageCodecServerService.decodeMessage(null, message);
		}
		catch (Exception e)
		{
			logger.error("解码请求报文失败!", e);
			responseCode = getFailCodeDecode();
		}
		if (txResponse != null)
		{
			response = txResponse;
		} else
		{
			response = new ReturnMessage();
			response.setResponseCode(responseCode);
			Map head = new HashMap();
			head.put("交易码", request.getCode());
			head.put("RET_STATUS", request.getHead().get("RET_STATUS"));
			head.put("RET_MSG", request.getHead().get("RET_MSG"));
			response.setHead(head);
			response.setDetails(request.getDetails());
		}
		byte bMessage[] = messageCodecServerService.encodeMessage(request.getCode(), response).getBytes();
		String resposeMsg = new String(bMessage);
		logger.info("Sever Send MSG:" + resposeMsg);
		return resposeMsg;
	}

	public String messageReceived2(Document doc)
		throws Exception
	{
		ReturnMessage request = null;
		ReturnMessage response = null;
		ReturnMessage txResponse = null;
		try
		{
			request = messageCodecServerService.decodeMessage(null, doc);
		}
		catch (Exception e)
		{
			logger.error("解码请求报文失败!", e);
		}
		if (txResponse != null)
		{
			response = txResponse;
		} else
		{
			response = new ReturnMessage();
			response.setDetails(request.getDetails());
		}
		byte bMessage[] = messageCodecServerService.encodeMessage(request.getCode(), response).getBytes();
		String resposeMsg = new String(bMessage);
		logger.info("Sever Send MSG:" + resposeMsg);
		return resposeMsg;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.server.AbstractServer.class);
	}
}