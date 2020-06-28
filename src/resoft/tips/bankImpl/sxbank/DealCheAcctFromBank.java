package resoft.tips.bankImpl.sxbank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import resoft.tips.action.CheckInfoReader;
/**
 * <p>解析银行对帐文件</p>
 * User: zhuchangwu
 * Date: 2007-8-20
 * Time: 17:44:26
 */
public class DealCheAcctFromBank implements CheckInfoReader{
    private String Line=null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private HashMap lineMap=null;
    public boolean next(){//判断是否存在下一行
    	if(Line!=null){
            lineMap=new HashMap();
            this.putValues(lineMap,Line);
            try {
              Line = br.readLine();
            }
            catch (IOException ex) {
            	return false;
            }
            return true;   		
        }else{
            try{
              br.close(); //关闭BufferedReader对象
              fr.close(); //关闭文件
            }catch (IOException ex) {}
            return false;
          }
    }
    /**
     * 得到本行的指定记录
     * */
    public String getString(String key){
    	return (String)lineMap.get(key);
    }

    //读取文件
    public void setFilePath(String filename) throws IOException{
		try {
			fr = new FileReader(filename);//建立FileReader对象，并实例化为fr 
		    br=new BufferedReader(fr);//建立BufferedReader对象，并实例化为br 
		    Line = br.readLine();//从文件中继续读取一行数据 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	

    }
    private void putValues(HashMap lineMap,String line){//将每行的记录保存在HashMap中
    	StringTokenizer tokens=new StringTokenizer(line.trim(),"|");
    		lineMap.put("BankDate", tokens.nextToken());
    		lineMap.put("BanktransNo", tokens.nextToken());
    		lineMap.put("DebitAcct", tokens.nextToken());
    		lineMap.put("TraAmt", tokens.nextToken());
    		lineMap.put("TraDate", tokens.nextToken());
    		lineMap.put("TransNo", tokens.nextToken());
    		//Map params = new HashMap();
    		//String msgdata=tokens.nextToken();
    		//String addword=tokens.nextToken();
    		
    		
    }
}

