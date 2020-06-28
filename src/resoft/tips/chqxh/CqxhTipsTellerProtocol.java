package resoft.tips.chqxh;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.CfxPackager;
import resoft.xlink.comm.Protocol;
import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * <p>重庆信合柜面系统交互</p>
 * Author: liwei
 * Date: 2007-07-29
 * Time: 14:51:06
 */
public class CqxhTipsTellerProtocol implements Protocol {
    
	private static Log logger = LogFactory.getLog(CqxhTipsTellerProtocol.class);
    
    public boolean isKeepAlive() {
        return false;
    }

    public byte[] read(InputStream inputStream) throws IOException {    	
        DataInputStream input = new DataInputStream(inputStream);
        //读取长度
        byte[] lenBytes=new byte[6];   				    
        input.read(lenBytes,0,6);
        logger.info("读取ACE报文长度："+new String(lenBytes));
        int length=Integer.parseInt(new String(lenBytes))-6;
        //读取报文
        byte[] reqBytes = new byte[length];			        
        input.read(reqBytes,0,length);               
        reqBytes=(new String(lenBytes)+new String(reqBytes)).getBytes();               
        //打印日志
        String reqStr = new String(reqBytes);
        ThreadLocalContext.getInstance().getContext().setPacker(new CfxPackager());//@todo 如何用多个Packager
        logger.info("接收ACE客户端内容:" + reqStr);
        
        ACEPackager acePack=new ACEPackager(reqStr);       
        return ACEPackUtil.setTransCode(acePack);
    }

    public void write(OutputStream output, byte[] bytes) throws IOException {    	      
        output.write(ACEPackUtil.backACEPack(bytes));
        output.flush();
        output.close();        
        logger.info("返回ACE客户端报文:" + new String(ACEPackUtil.backACEPack(bytes)));
    }
}
