// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecurityService.java

package resoft.tips.connector.security;


public interface SecurityService
{

	public abstract byte[] encrypt(String s)
		throws Exception;

	public abstract String decrypt(byte abyte0[])
		throws Exception;

	public abstract String digest(String s)
		throws Exception;
}