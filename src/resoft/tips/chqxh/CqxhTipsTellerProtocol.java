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
 * <p>�����źϹ���ϵͳ����</p>
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
        //��ȡ����
        byte[] lenBytes=new byte[6];   				    
        input.read(lenBytes,0,6);
        logger.info("��ȡACE���ĳ��ȣ�"+new String(lenBytes));
        int length=Integer.parseInt(new String(lenBytes))-6;
        //��ȡ����
        byte[] reqBytes = new byte[length];			        
        input.read(reqBytes,0,length);               
        reqBytes=(new String(lenBytes)+new String(reqBytes)).getBytes();               
        //��ӡ��־
        String reqStr = new String(reqBytes);
        ThreadLocalContext.getInstance().getContext().setPacker(new CfxPackager());//@todo ����ö��Packager
        logger.info("����ACE�ͻ�������:" + reqStr);
        
        ACEPackager acePack=new ACEPackager(reqStr);       
        return ACEPackUtil.setTransCode(acePack);
    }

    public void write(OutputStream output, byte[] bytes) throws IOException {    	      
        output.write(ACEPackUtil.backACEPack(bytes));
        output.flush();
        output.close();        
        logger.info("����ACE�ͻ��˱���:" + new String(ACEPackUtil.backACEPack(bytes)));
    }
}
