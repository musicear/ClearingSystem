package resoft.tips.chqxh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class testPrint {
	public static void main(String[] args)throws Exception{
		File f = new File("c:\\", "testPrint.txt");    		
		Writer writer = new FileWriter(f);
		String temp="";
		int lineLen=100;
		for (int i=1;i<30;i++) {
			temp=""+i;
			System.out.println(""+(lineLen-temp.length()));
			for (int j=0;j<(lineLen-2);j++) {
				temp+="A";
			}
			temp+=""+temp.getBytes().length+"\n";
			writer.write(temp);
			writer.flush();
		}
		writer.close();
	}
}
