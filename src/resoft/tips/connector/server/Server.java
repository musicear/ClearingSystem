// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Server.java

package resoft.tips.connector.server;

import resoft.tips.connector.codec.MessageCodecServerService;

public interface Server
{

	public abstract void start()
		throws Exception;

	public abstract void stop()
		throws Exception;

	public abstract String messageReceived(String s)
		throws Exception;

	public abstract void setMessageCodecServerService(MessageCodecServerService messagecodecserverservice);

	public abstract MessageCodecServerService getMessageCodecServerService();

	public abstract void setServerName(String s);

	public abstract String getServerName();
}