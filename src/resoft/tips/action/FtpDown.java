package resoft.tips.action;

import java.net.URL;

import resoft.tips.util.FTPUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * 从FTP上读取文件:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007.9.11
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zhuchangwu
 */

public class FtpDown {

	public FtpDown() {

	}

	public String getFileFromServer(String url, String username, String password, String filePath, String fileName) {
		String downPath = "";

		FTPUtil ftputil = new FTPUtil();
		ftputil.setServer(url);
		ftputil.setUser(username);
		ftputil.setPassword(password);
		ftputil.setPath(filePath);

		ClassLoader loader = getClass().getClassLoader();
		URL urlpath = loader.getResource("");
		downPath = urlpath.getFile() + "filedown/";
		downPath = downPath.substring(1, downPath.length()) + fileName;
		try {
			ftputil.download(fileName, downPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return downPath;
	}

	public static void main(String[] args) {

	}
}
