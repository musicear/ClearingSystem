package resoft.tips.action2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelSftp.LsEntry;

import resoft.basLink.Configuration;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class CheckSftp implements Action{
	
	private static final Log logger = LogFactory.getLog(CheckSftp.class);
	Configuration conf = Configuration.getInstance();
	
	private String host = null;
	private String username = null;
	private String password = null;
	private int port = 22;
	
	private String directory = null;
	private String downloaddir = null;
	public String getDownloaddir() {
		return downloaddir;
	}
	
	public void setDownloaddir(String downloaddir) {
		this.downloaddir = downloaddir;
	}



	private String downloadFile = null;
	private String saveFile = null;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	
	
	public int execute(Message arg0) throws Exception {
		// TODO Auto-generated method stub
		
		
		host=conf.getProperty("SFTPADD", "SftpHost");
		username=conf.getProperty("SFTPADD", "Username");
		password=conf.getProperty("SFTPADD", "Password");
		port =Integer.parseInt(conf.getProperty("SFTPADD", "SftpPort"));
		//directory = conf.getProperty("SFTPADD", "Directory");
		saveFile = conf.getProperty("SFTPADD", "SaveFile");
		downloaddir = conf.getProperty("SFTPADD", "DownloadDir");
		List<String> lstFileNames = new ArrayList<String>();
		
		FtpImpl sftpImpl = new FtpImpl();
		sftpImpl.connect(host, username, password, port);
		//boolean isdepth = Boolean.valueOf(conf.getProperty("SFTPADD", "Isdepth")).booleanValue();
		Vector<LsEntry> vecList = sftpImpl.getListFiles(downloaddir);
		//Vector vec = sftp.ls(directory);
		
		if(vecList.size() <= 0){
			sftpImpl.disconnect();
			return -1;			
		}else{
			for(LsEntry file : vecList){
				sftpImpl.disconnect();
				sftpImpl.connect(host, username, password, port);
				String downloadFile = file.getFilename().toString().trim();
				logger.info("filename is: "+ downloadFile);
				//判断文件是否已存在，如存在直接sftp删除该文件（待写）
				
				sftpImpl.download(downloaddir, downloadFile, saveFile);
				
				sftpImpl.delete(downloaddir, downloadFile);
				
			}
		
		
		}
		sftpImpl.disconnect();
		//boolean test = sftpImpl.Get(directory, saveFile, sftp);
		//lstFileNames = sftpImpl.getFileEntryList(directory);
		return 0;
	}
	
	
}
