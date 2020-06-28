package resoft.tips.connector.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.filter.LoggingFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import resoft.tips.connector.codec.MessageCodecServerService;

// Referenced classes of package resoft.tips.connector.server:
//			AbstractServer

public class TipsServer extends AbstractServer
	implements IoHandler
{

	private static final Logger logger;
	private IoAcceptor acceptor;
	private Map handlers;

	public TipsServer()
	{
		acceptor = null;
		handlers = new HashMap();
	}

	public void start()
		throws Exception
	{
		try
		{
			int port = getPort();
			IoAcceptorConfig config = new SocketAcceptorConfig();
			config.setThreadModel(ThreadModel.MANUAL);
			DefaultIoFilterChainBuilder chain = config.getFilterChain();
			chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
			chain.addLast("logger", new LoggingFilter());
			acceptor = new SocketAcceptor(4, Executors.newCachedThreadPool());
			acceptor.bind(new InetSocketAddress(port), this, config);
			logger.info("TIPS服务器开始监听端口:" + port);
			logger.error("启动TIPS服务器成功!");
		}
		catch (IOException e)
		{
			logger.error("启动TIPS服务器失败!", e);
		}
	}

	public void stop()
		throws Exception
	{
		acceptor.unbindAll();
		logger.info("TIPS服务器停止监听端口:" + getPort());
	}

	public void exceptionCaught(IoSession arg0, Throwable arg1)
		throws Exception
	{
		logger.info("TIPS服务器发生异常", arg1);
	}

	public void messageReceived(IoSession session, Object message)
		throws Exception
	{
		logger.info("resiveTime:" + new Date());
		ByteBuffer buffer = (ByteBuffer)session.getAttribute("buffer");
		Integer length = (Integer)session.getAttribute("bodyLength");
		byte head[] = (byte[])session.getAttribute("head");
		if (buffer == null)
		{
			buffer = ByteBuffer.allocate(1024);
			buffer.setAutoExpand(true);
			session.setAttribute("buffer", buffer);
		}
		buffer.put((ByteBuffer)message);
		if (length == null && buffer.limit() > 8)
		{
			buffer.rewind();
			head = new byte[8];
			buffer.get(head);
			buffer.position(buffer.limit());
			String shead = new String(head);
			length = Integer.valueOf(Integer.parseInt((new String(head)).trim()));
			session.setAttribute("head", head);
			session.setAttribute("bodyLength", length);
		}
		if (length != null && buffer.limit() >= 8 + length.intValue())
		{
			buffer.position(8);
			byte body[] = new byte[length.intValue()];
			buffer.get(body);
			session.removeAttribute("buffer");
			session.removeAttribute("bodyLength");
			session.removeAttribute("head");
			String sbody = new String(body, "utf-8");
			logger.info("sbody is: "+ sbody);
			logger.info("doc start:" + new Date());
			Document doc = string2Document(sbody.trim());
			logger.info("doc end:" + new Date());
			String rpsStr2 = messageReceived2(doc);
			byte bMessage[] = rpsStr2.getBytes("utf-8");
			ByteBuffer rBuffer = ByteBuffer.allocate(bMessage.length);
			rBuffer.put(bMessage);
			rBuffer.flip();
			WriteFuture future = session.write(rBuffer);
			future.join();
		}
	}

	public static Document string2Document(String s)
	{
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(s);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return doc;
	}

	public void messageSent(IoSession iosession, Object obj)
		throws Exception
	{
	}

	public void sessionClosed(IoSession iosession)
		throws Exception
	{
	}

	public void sessionCreated(IoSession iosession)
		throws Exception
	{
	}

	public void sessionIdle(IoSession iosession, IdleStatus idlestatus)
		throws Exception
	{
	}

	public void sessionOpened(IoSession iosession)
		throws Exception
	{
	}

	public MessageCodecServerService getMessageCodecServerService()
	{
		return null;
	}

	public Map getHandlers()
	{
		return handlers;
	}

	public void setHandlers(Map handlers)
	{
		this.handlers = handlers;
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.server.TipsServer.class);
	}
}