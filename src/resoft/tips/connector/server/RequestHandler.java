package resoft.tips.connector.server;

import resoft.tips.connector.message.ReturnMessage;

public interface RequestHandler
{

	public abstract ReturnMessage txHandleRequest(String s, ReturnMessage returnmessage)
		throws Exception;
}