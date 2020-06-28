package junit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Socket;

public class test {

	/**
	 * @param args
	 */
	 
	public static void main(String[] args) {
		
		try {
			Socket sender=null;
			sender=new Socket("127.0.0.1",4272);
			sender.setSoTimeout(30*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			File f=new File("D:\\test.xml");
			byte[] bytes = new byte[(int) f.length()];
			
			int len = bytes.length;
	       String lenStr = Integer.toString(len);
	       for(int i = lenStr.length();i<8;i++){
	       lenStr = "0"+lenStr;
	       }
	        InputStream is = new FileInputStream(f);
	        is.read(bytes);
	        String packageStr = new String(bytes);
	        System.out.println("packageStr is: "+packageStr);
	        String str = lenStr + packageStr;
	        byte[] bytePack = new byte[str.length()];
	        bytes = str.getBytes();
	        out.write(bytes);
	        
			
			
			System.out.println("发送转账业务请求:["+new String(bytes)+"]");
				 
			out.flush();
			byte[] packageLengthbyte=new byte[8];
			in.read(packageLengthbyte,0,8);
			String lengthStr=new String(packageLengthbyte);
			int packetlength=Integer.parseInt(lengthStr);
			byte[] recPack=new byte[packetlength];	
			in.read(recPack,0,packetlength); 
			String rcvChargeMsg = new String(recPack);
			
			System.out.println("receiveMsg is : " +rcvChargeMsg); 
		}
		  catch (IOException e) { 
			  e.printStackTrace();
			  
			  
		} 
		
		
	}

		 static void moveFolder(String src, String dest) {
		  File srcFolder = new File(src);
		  File destFolder = new File(dest);
		  File newFile = new File(destFolder.getAbsoluteFile() + "\\" + srcFolder.getName());
		  srcFolder.renameTo(newFile);
		 }
	
		 public static double add(double v1, double v2) {  
			  BigDecimal b1 = new BigDecimal(Double.toString(v1));  
			  BigDecimal b2 = new BigDecimal(Double.toString(v2));  
			  
			  return b1.add(b2).doubleValue();  
			}  

}
