package resoft.tips.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.action2.FtpImpl;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新相关回执的处理状态</p>
 */
public class UpdateProcFlagAsReturnSucc implements Action {
	private static final Log logger = LogFactory.getLog(UpdateProcFlagAsReturnSucc.class);
	private static Configuration conf = Configuration.getInstance();
	public int execute(Message msg) throws Exception {
        //批量扣税回执
    	if ("2102".equals(msg.getString("OriMsgNo"))) {
        	//String workDate = msg.getString("//CFX/HEAD/WorkDate");
            String result = msg.getString("Result");
            if (result.equals("90000") || result.equals("94053")) {
                //回执成功
                String oriEntrustDate = msg.getString("OriEntrustDate");
                String oriRequestNo = msg.getString("OriRequestNo");
                String sql = "update BatchPackage set procFlag='4' where returnDate='" + oriEntrustDate + "' and returnPackNo='" + oriRequestNo + "'";
                DBUtil.executeUpdate(sql);
                
            }
            
        }
    	
    	//集中支付划款申请回执
        if ("2201".equals(msg.getString("OriMsgNo"))) { 
        	
        	String workDate = msg.getString("//CFX/HEAD/WorkDate");
            String result = msg.getString("Result");
            String oriEntrustDate = msg.getString("OriEntrustDate");
            String oriRequestNo = msg.getString("OriRequestNo");
            if (result.equals("90000") || result.equals("94053") || result.equals("94051")||result.equals("94052")) {
                //回执成功
                
                String sql = "update PayOrderPack set returnStatus='1'  where entrustDate='" + oriEntrustDate + "' and packNo='" + oriRequestNo + "'";
                DBUtil.executeUpdate(sql);
                if(!result.equals("90000"))
                	makeReceiveFile(oriEntrustDate,oriRequestNo,"A","1"+result);
            }else{
            	makeReceiveFile(oriEntrustDate,oriRequestNo,"A","1"+result);
            }
            
        }
        
        //集中支付退款申请回执
        if ("2202".equals(msg.getString("OriMsgNo"))) {
        	String workDate = msg.getString("//CFX/HEAD/WorkDate");
            String result = msg.getString("Result");
            String oriEntrustDate = msg.getString("OriEntrustDate");
            String oriRequestNo = msg.getString("OriRequestNo");
            if (result.equals("90000") || result.equals("94053") || result.equals("94051") ||result.equals("94052")) {
                //回执成功
                
                String sql = "update ReFundPack set returnStatus ='1'  where entrustDate='" + oriEntrustDate + "'  and packNo='" + oriRequestNo + "'";
                DBUtil.executeUpdate(sql);
                if(!result.equals("90000"))
                	makeReceiveFile(oriEntrustDate,oriRequestNo,"B","1"+result);
            }else{
            	makeReceiveFile(oriEntrustDate,oriRequestNo,"B","1"+result);
            }
        }
        
        return SUCCESS;
    }
	
	private void makeReceiveFile(String oriEntrustDate ,String oriRequestNo,String type,  String checkStatus) throws SftpException{
		
		String path = conf.getProperty("SFTPADD", "ReceiveFile");
	
		File f = new File(path);
		if(!f.exists()){
		  f.mkdirs();
		} 
		
		String fileName = oriEntrustDate + oriRequestNo + type + checkStatus;
		File file = new File(f,fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//sendToSftp(fileName);
		
	}
	
	private void sendToSftp(String fileName) {
		 String host = null;
		 String username = null;
		 String password = null;
		 int port = 22;
		 String localPath = conf.getProperty("SFTPADD", "ReceiveFile");
		 String uploadPath = conf.getProperty("SFTPADD", "UploadDir");
		
		 host=conf.getProperty("SFTPADD", "SftpHost");
		 username=conf.getProperty("SFTPADD", "Username");
		 password=conf.getProperty("SFTPADD", "Password");
		 port =Integer.parseInt(conf.getProperty("SFTPADD", "SftpPort"));
		 
		 FtpImpl sftpImpl = new FtpImpl();
		 sftpImpl.connect(host, username, password, port);
		
		 try {
			sftpImpl.uploadFile(fileName, localPath, uploadPath);
			sftpImpl.disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
	}
}
