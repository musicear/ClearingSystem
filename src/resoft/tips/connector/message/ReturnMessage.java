package resoft.tips.connector.message;

import java.util.HashMap;
import java.util.Map;

import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

// Referenced classes of package resoft.tips.connector.message:
//			AbstractMessage

public class ReturnMessage extends AbstractMessage
{

	private static final long serialVersionUID = 0xf2f17cb5bb6ffe9bL;
	public static final String TX_CODE = "TXCODE";
	public static final String RESPONSE_CODE = "RESPONSECODE";
	public static final String RESPONSE_RMK = "RESPONSERMK";
	private boolean txSuccess;
	private Map head;
	private Message details;

	public ReturnMessage()
	{
		head = new HashMap();
		details = new DefaultMessage();
	}

	public Map getHead()
	{
		return head;
	}

	public void setHead(Map head)
	{
		this.head.putAll(head);
	}

	public String getCode()
	{
		return (String)head.get("TXCODE");
	}

	public void setCode(String code)
	{
		head.put("TXCODE", code);
	}

	public String getResponseCode()
	{
		return (String)head.get("RESPONSECODE");
	}

	public void setResponseCode(String responseCode)
	{
		head.put("RESPONSECODE", responseCode);
	}

	public String getResponseRmk()
	{
		return (String)head.get("RESPONSERMK");
	}

	public void setResponseRmk(String responseRmk)
	{
		head.put("RESPONSERMK", responseRmk);
	}

	public void putHead(String key, String value)
	{
		head.put(key, value);
	}

	public void putHead(String key, Object value)
	{
		head.put(key, value);
	}

	public boolean isTxSuccess()
	{
		return txSuccess;
	}

	public void setTxSuccess(boolean txSuccess)
	{
		this.txSuccess = txSuccess;
	}

	public String toString()
	{
		String msg = "Head:" + head + "\n" + "detail:" + details;
		return msg;
	}

	public Message getDetails()
	{
		return details;
	}

	public void setDetails(Message details)
	{
		this.details = details;
	}
}
