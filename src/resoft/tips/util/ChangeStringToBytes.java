package resoft.tips.util;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>��string���͵�����ת��Ϊ��������ĸ�ʽ,��Ͽ����</p>
 */
public class ChangeStringToBytes {
	///��������ת��
	private static final Log logger = LogFactory.getLog(ChangeStringToBytes.class);
	public byte[] GetBytes(String sendMsg,String SendType)throws SQLException
	{
		int len = sendMsg.getBytes().length+2;
    	String lenStr = Integer.toString(len);
    	
    	byte[] bt=new byte[4];
    	
    	String str=Integer.toHexString(len).toString();
    	for(int i=0;i<4;i++){
            bt[i]=(byte)(len>>>(24-i*8));
		}

    	
    	for(int i=lenStr.length();i<10;i++)
    	{
    		lenStr = lenStr + " ";
    	}
    	logger.info("ǰʮλ����Ϊ��"+lenStr.trim());
    	logger.info("��ǰ��ѯ״̬Ϊ��"+SendType);
    	byte[] typeTobyte=SendType.getBytes();
    	
    	
    	byte[] sendbt=new byte[len+lenStr.getBytes().length+1];
    	for(int i=0;i<sendbt.length;i++)
    	{
    		if(i<10)
    		{
    			sendbt[i]=lenStr.getBytes()[i];
    			
    		}
    		else if(i==10)
    		{
    			sendbt[10]=typeTobyte[0];
    		}
    		else if(i<13)
    		{
    			sendbt[11]=bt[2];
    			sendbt[12]=bt[3];
    		}
    		else
    		{
    			sendbt[i]=sendMsg.getBytes()[i-13];	
    		}
    		
    	}
    	logger.info("���鳤��Ϊ��"+sendbt.length);
    		return sendbt;
		
		
	}
	public byte[] GetBytes(String sendMsg,String SendType,String OriString)throws SQLException
	{
		
		int len = sendMsg.getBytes().length+2;
    	String lenStr = Integer.toString(len);
    	
    	byte[] bt=new byte[4];
    	
    	String str=Integer.toHexString(len).toString();
    	for(int i=0;i<4;i++){
            bt[i]=(byte)(len>>>(24-i*8));
		}

    	
    	for(int i=lenStr.length();i<10;i++)
    	{
    		lenStr = lenStr + " ";
    	}
    	for(int i=OriString.length();i<30;i++)
    	{
    		OriString = OriString + " ";
    	}
    	
    	byte[] typeTobyte=SendType.getBytes();
    	logger.info("ǰʮλ����Ϊ��"+lenStr.trim());
    	
    	byte[] oribytes=OriString.getBytes();
    	
    	byte[] sendbt=new byte[len+lenStr.getBytes().length+1+oribytes.length];
    	for(int i=0;i<sendbt.length;i++)
    	{
    		if(i<10)
    		{
    			sendbt[i]=lenStr.getBytes()[i];
    			
    		}
    		else if(i==10)
    		{
    			sendbt[10]=typeTobyte[0];
    		}
    		else if(i<41)
    		{
    			sendbt[i]=oribytes[i-11];
    			
    		}
    		else if(i<43)
    		{
    			sendbt[41]=bt[2];
    			sendbt[42]=bt[3];
    		}
    		else
    		{
    			sendbt[i]=sendMsg.getBytes()[i-43];	
    		}
    		
    	}
    	String SendStr=new String(sendbt);
    	logger.info("���鳤��Ϊ��"+sendbt.length);
    	logger.info("���͵�����λ"+SendStr);
    		return sendbt;
	}
}
