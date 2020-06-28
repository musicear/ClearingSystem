package resoft.basLink.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Service;

/**
 * function: 提供Socket通讯服务
 * User: albert lee
 * Date: 2005-9-22
 * Time: 8:56:31
 */
public class NioCommService implements Service {
    private static Log logger = LogFactory.getLog(NioCommService.class);
    public void start() throws Exception {
        ServerSocket serverSocket = null;
        Selector selector = null;
        try {
            //create a server socket
            ServerSocketChannel ssc = ServerSocketChannel.open();
            serverSocket = ssc.socket();
            //Get a selector object
            selector = Selector.open();
            //Bind the server socket to the port
            serverSocket.bind(new InetSocketAddress(1211));

            ssc.configureBlocking(false);
            //register
            ssc.register(selector,SelectionKey.OP_ACCEPT);
        } catch(IOException e) {
            logger.error("启动CommServer失败",e);
            throw e;
        }
        //begin listen
        int n = 0;
        while(active) {
            try {
                n = selector.select();
            } catch(Exception e) {
                if(selector.isOpen()) {
                    continue;
                } else {
                    logger.error("通讯错误",e);
                }
            }

            if(n==0) {
                continue;
            }
            //we have some ready sockets,so iterate them
            for(Iterator itr = selector.selectedKeys().iterator();itr.hasNext();) {
                SelectionKey key = (SelectionKey) itr.next();
                if(!key.isValid()) {
                    itr.remove();
                    continue;
                }
                try {
                    if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        //so accept,get the client socket
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        logger.info("remote socket from :" + clientChannel.socket().getLocalAddress().getHostAddress());
                        //register read
                        clientChannel.register(selector,SelectionKey.OP_READ);
                    } else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
//                        logger.info("ready for read");
//                        byte[] result = readMessage(key);
//                        logger.info(new String(result));
                        SocketChannel channel = (SocketChannel)key.channel();

                        //Pipeline.getInstance().offerRequest(channel.socket(),"ABC".getBytes());

                    }
                } catch(Exception e) {
                    logger.error("通讯错误",e);
                    key.cancel();
                }

                //remove key
                itr.remove();

            }
        }
    }

    private byte[] readMessage(SelectionKey key) {

		SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
		try
		{
            channel.read(buf);
			buf.flip();
            //Pipeline.getInstance().offerRequest(channel.socket(),buf.array());
//			Charset charset = Charset.forName("us-ascii");
//			CharsetDecoder decoder = charset.newDecoder();
//			CharBuffer charBuffer = decoder.decode(buf);
//			result = charBuffer.toString();

        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return buf.array();
    }

    public void stop() throws Exception {

    }

    public boolean isRunning() {
        return false;
    }

    private boolean active = true;
}
