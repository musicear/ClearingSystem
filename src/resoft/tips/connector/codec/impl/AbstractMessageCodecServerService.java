// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractMessageCodecServerService.java

package resoft.tips.connector.codec.impl;

import resoft.tips.connector.codec.MessageCodecServerService;
import resoft.tips.connector.security.SecurityService;

public abstract class AbstractMessageCodecServerService
	implements MessageCodecServerService
{

	private SecurityService securityService;

	public AbstractMessageCodecServerService()
	{
	}

	public SecurityService getSecurityService()
	{
		return securityService;
	}

	public void setSecurityService(SecurityService securityService)
	{
		this.securityService = securityService;
	}
}