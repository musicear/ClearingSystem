package resoft.tips.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ConnectException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import resoft.basLink.Configuration;

/**
 * FTP工具类
 */
public class FTPUtil {

	private String server;
	private int port = 21;
	private String path;
	private String user;
	private String password;
	private String localpath;
	private static final Log logger = LogFactory.getLog(FTPUtil.class);
	
	public FTPUtil() {
		
	}
	
	/**
     * @param ftp
     */
    private void connect(FTPClient ftp) throws Exception{
        //连接服务器
        ftp.connect( server, port);
        int reply = ftp.getReplyCode();
        //是否连接成功
        if ( !FTPReply.isPositiveCompletion( reply ) )         {
            throw new ConnectException( server+" 服务器拒绝连接" );
        }
        //登陆
        if (!ftp.login(user, password)) {
            throw new ConnectException( "用户名或密码错误" );
        }
        
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

	/**
	 * 从FTP服务器上下载文件
	 * @param filename  
	 * @throws Exception 
	 */
	public void download(String filename, String localfilename) throws Exception {
		logger.info("从服务器" + server + "上FTP下载文件: " + filename);
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;
	
		try {
		connect(ftpClient);
		if (path.length() != 0)
			ftpClient.changeWorkingDirectory(path);
		
		ftpClient.setBufferSize(1024);
		fos = new FileOutputStream(localpath + localfilename);
		ftpClient.retrieveFile(filename, fos);
		}
		finally {
			IOUtils.close(fos);
			ftpClient.disconnect();
		}
		logger.info("文件" + filename+"下载成功,保存本地文件名:" + localfilename);
	}
	
	/**
	 * 向FTP服务器上传文件
	 * @param filename
	 * @throws Exception 
	 */
	public void upload(String filename) throws Exception {
		logger.info("将文件"+ filename + "FTP上传至服务器");
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		
		try {
		connect(ftpClient);
		if (path.length() != 0) {
			ftpClient.makeDirectory(path);
			ftpClient.changeWorkingDirectory(path);
		}
		
		File file_in = new File(localpath + filename);
		fis = new FileInputStream(file_in);
		ftpClient.storeFile(filename, fis);
		}
		finally {
			IOUtils.close(fis);
			ftpClient.disconnect();
		}
		
		logger.info("文件"+ filename + "上传成功！");
	}

	public String getLocalpath() {
		return localpath;
	}

	public void setLocalpath(String localpath) {
		this.localpath = localpath;
		if (!this.localpath.trim().endsWith(File.separator)) {
			this.localpath = this.localpath.trim() + File.separator;
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
	public static void main(String[] args){
		Configuration conf = Configuration.getInstance();
		try {
			FTPUtil ftp = new FTPUtil();
			ftp.setServer(conf.getProperty("bankImpl", "FtpServer"));
			ftp.setPort(Integer.parseInt(conf.getProperty("bankImpl", "FtpPort")));
			ftp.setPath(conf.getProperty("bankImpl", "FtpDuiZhangPath"));
			ftp.setUser(conf.getProperty("bankImpl", "FtpUser"));
			ftp.setPassword(conf.getProperty("bankImpl", "FtpPassword"));
			ftp.setLocalpath(conf.getProperty("bankImpl", "CheckFilePath"));
			String filename="HXLW_20060608.txt";
			String localfilename = "20060708" + "_BANK_"+filename;
			logger.info("对账文件："+filename+",本地文件:"+localfilename);
			ftp.download(filename, localfilename);
		} catch (Exception e) {
			logger.error("FTP下载文件错误:" + e.getMessage());
			e.printStackTrace();

		}
	}
	
}

