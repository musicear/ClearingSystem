package resoft.tips.connector.codec;

import org.dom4j.Document;

import resoft.tips.connector.message.ReturnMessage;
import resoft.tips.connector.security.SecurityService;

public interface MessageCodecServerService
{

	public abstract String encodeMessage(String s, ReturnMessage returnmessage)
		throws Exception;

	public abstract String encodeMessage2(Document document, String s, ReturnMessage returnmessage)
		throws Exception;

	public abstract ReturnMessage decodeMessage(String s, String s1)
		throws Exception;

	public abstract ReturnMessage decodeMessage(String s, Document document)
		throws Exception;

	public abstract void setSecurityService(SecurityService securityservice);

	public abstract SecurityService getSecurityService();
}