package resoft.tips.action2;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;



public class FtpImpl{
    
    
    private static final Log logger = LogFactory.getLog(FtpImpl.class);
    private ChannelSftp sftp = null;
    public ChannelSftp getSftp() {
		return sftp;
	}
	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}
	/**
     * connect server via sftp
     */
    public void connect(String host,String username,String password,int port) {
        
    	try {
            if(sftp != null){
                System.out.println("sftp is not null");
            }
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Disconnect with server
     * @throws JSchException 
     */
    public void disconnect() throws JSchException {
      
        if (null != this.sftp) 
        { 
            this.sftp.disconnect(); 

            if (null != this.sftp.getSession()) 
            { 
                this.sftp.getSession().disconnect(); 
            } 
        logger.info("SFTP has closed!");
        }
    }

   
        

	void download(String directory, String downloadFile,String saveFile) {
        try {
            
            this.sftp.cd(directory);
            File file = new File(saveFile +downloadFile);
            this.sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	

    
    /**
     * upload all the files to the server
     */
    public void upload(String fileListPath, String localPath, String seperator, String remotePath, ChannelSftp sftp) {
        List<String> fileList = this.getFileEntryList(fileListPath);
        
        
        
        try {
            if(fileList != null){
                for (String filepath : fileList) {
                    String localFile = localPath + seperator+ filepath;
                    File file = new File(localFile);
                    
                    if(file.isFile()){
                        System.out.println("localFile : " + file.getAbsolutePath());
                        String remoteFile =remotePath + seperator + filepath;
                        System.out.println("remotePath:" + remoteFile);
                        File rfile = new File(remoteFile);
                        String rpath = rfile.getParent();
                        try {
                            createDir(rpath, sftp);
                        } catch (Exception e) {
                            System.out.println("*******create path failed" + rpath);
                        }

                        sftp.put(new FileInputStream(file), file.getName());
                        System.out.println("=========upload down for " + localFile);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
    
    /**
     * upload the file to the server
     * @throws SftpException 
     */
    public void uploadFile(String fileName, String localPath, String remotePath) throws SftpException {

        try {
               this.sftp.cd(remotePath);  
               File file = new File(localPath + fileName);
               this.sftp.put(new FileInputStream(file), file.getName());
                        
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    
    /**
     * create Directory
     * @param filepath
     * @param sftp
     */
    private void createDir(String filepath, ChannelSftp sftp){
        boolean bcreated = false;
        boolean bparent = false;
        File file = new File(filepath);
        String ppath = file.getParent();
        try {
            sftp.cd(ppath);
            bparent = true;
        } catch (SftpException e1) {
            bparent = false;
        }
        try {
            if(bparent){
                try {
                    sftp.cd(filepath);
                    bcreated = true;
                } catch (Exception e) {
                    bcreated = false;
                }
                if(!bcreated){
                    sftp.mkdir(filepath);
                    bcreated = true;
                }
                return;
            }else{
                createDir(ppath,sftp);
                sftp.cd(ppath);
                sftp.mkdir(filepath);
            }
        } catch (SftpException e) {
            System.out.println("mkdir failed :" + filepath);
            e.printStackTrace();
        }
        
        try {
            sftp.cd(filepath);
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("can not cd into :" + filepath);
        }
        
    }
    /**
     * get all the files need to be upload or download
     * @param file
     * @return
     */
     List<String> getFileEntryList(String file){
        ArrayList<String> fileList = new ArrayList<String>();
        InputStream in = null;
        try {
            
            in = new FileInputStream(file);
            InputStreamReader inreader = new InputStreamReader(in);
            
            LineNumberReader linreader = new LineNumberReader(inreader);
            String filepath = linreader.readLine();
            while(filepath != null){
                fileList.add(filepath);
                filepath = linreader.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(in != null){
                in = null;
            }
        }

        return fileList;
    }
    
    /** 
     * @param path 
     *            ÎÄ¼þÂ·¾¶ 
     * @throws SftpException 

 
     */  
    public Vector<LsEntry> getListFiles(String downloaddir) throws SftpException { 
    	
    	
    	Vector<LsEntry> vec = this.sftp.ls(downloaddir);
		return vec;
     }
    

    
	public void delete(String directory, String deleteFile) {
    	try {
    	
    	this.sftp.rm(deleteFile);
    	} catch (Exception e) {
    	e.printStackTrace();
    	}
    }

    

}


