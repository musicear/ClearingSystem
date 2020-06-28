package resoft.tips.connector.codec;

import resoft.tips.connector.message.ReturnMessage;
import resoft.tips.connector.security.SecurityService;

public interface MessageCodecClientService
{

	public abstract String encodeMessage(String s, ReturnMessage returnmessage)
		throws Exception;

	public abstract ReturnMessage decodeMessage(String s, String s1)
		throws Exception;

	public abstract void setSecurityService(SecurityService securityservice);

	public abstract SecurityService getSecurityService();
}