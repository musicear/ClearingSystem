package junit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class testZH {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader(new File("D:\\test.txt"));
		LineNumberReader lnr = new LineNumberReader(fr);
		String line=lnr.readLine();
		String[] headFormat={"�������","������ȫ��","dss"};
		Map headInfo=new HashMap();
		String[] data=line.split(",",headFormat.length); 
		for(int i=0;i<headFormat.length;i++){
			System.out.println("1:"+data[i]) ;
		}
		
		String[] detailFormat={"Ԥ�㵥λ����","���ܿ�Ŀ����"};
		while ((line=lnr.readLine()) != null) {
			data = line.split(",",detailFormat.length);
			Map detailInfo=new HashMap();
			for(int i=0;i<detailFormat.length;i++){
				System.out.println("2:"+data[i]);
			}
	}
	}
}
