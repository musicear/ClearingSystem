package junit;

import java.io.File;

public class TestFileMove {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String saveFile = "C:\\download\\";
		String name = "test.txt";
		
		moveFolder(saveFile+name, "C:\\myftp\\");
	}
	
	private static void moveFolder(String src, String dest) throws InterruptedException {
		  System.out.println("src is: "+src );
		  System.out.println("dest is: "+dest ); 
		  File srcFolder = new File(src);
		  File destFolder = new File(dest);
		  System.out.println("dest.path is: "+destFolder.getAbsolutePath() );
		  File newFile = new File(dest + srcFolder.getName());
		  boolean fileStatus = srcFolder.renameTo(newFile);
		  while(false == fileStatus){
			  System.gc();
			  //Thread.sleep(10000);
			  fileStatus = srcFolder.renameTo(newFile);
		  }
	}
}
