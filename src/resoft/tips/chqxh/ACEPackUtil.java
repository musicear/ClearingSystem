package resoft.tips.chqxh;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import resoft.tips.mq.SyncListener;

/**
 * <p>�����ź�ACE���Ĺ�����</p>
 * User: liwei
 * Date: 2007-07-30
 * Time: 13:21:00
 */
public class ACEPackUtil {
	private static final Log logger = LogFactory.getLog(SyncListener.class);	
	//����ACE���ĵ�TransCode
	public static byte[] setTransCode(ACEPackager ace){
		String retStr="";
		if(ace!=null){
			retStr="<CFX><MSG><������ val='"+ace.transCode+"'/><ACETransHead val=''/><ACETrandBody val=''/><ACEPack val='"+ace.pack+"'/></MSG></CFX>";			
		}
		logger.info("�ַ������з�װ��"+retStr);
		logger.info("transCode:"+ace.transCode);
		
		//CheckUnicodeString(retStr);
				
		return retStr.getBytes();
	}
	
	public static void CheckUnicodeString(String value) {
		//logger.info("111111111111111:"+value.getBytes().length);
		for (int i=0; i < value.length(); ++i) {
			if (value.getBytes()[i] == 0x00) {
				logger.info("Invalid Unicode:"+i);				
			} else if (value.getBytes()[i] < 0x20 && value.getBytes()[i] != '\t' & value.getBytes()[i] != '\n' & value.getBytes()[i] != '\r'){
				logger.info("Invalid Xml Characters:@"+i+"@");
			}
			logger.info("["+(char)value.getBytes()[i]+"]");
		}		
	}
	
	//ACE������xml�����ַ�ƴ��
	public static byte[] backACEPack(byte[] retByte) {
		String backACEStr="";
		String backPackLen="";
		StringReader strRea=new StringReader(new String(retByte));
		SAXReader reader=new SAXReader();
		Document doc=null;
		try{
			doc=reader.read(strRea);			
			Element ACETransHead=(Element)doc.selectSingleNode("//CFX//MSG/ACETransHead");
			backACEStr+="|"+ACETransHead.attributeValue("val");
			Element ACETransBody=(Element)doc.selectSingleNode("//CFX//MSG/ACETrandBody");
			backACEStr+="|"+ACETransBody.attributeValue("val")+"|";									
		}catch(Exception e){
			//logger.info("ACE���ر��Ľ���ƴ���ǲ���δ֪����");
			//e.printStackTrace();
		}
		//logger.info("ACE������xml�����ַ�ƴ�ӣ�"+backACEStr);
		backPackLen=""+(6+1+backACEStr.getBytes().length);
		backPackLen=ACEPackUtil.getACEPackLen(backPackLen);
		return (backPackLen+backACEStr).getBytes();
	}
	
	//���ĳ��Ƚ����Ҷ���
	public static String getACEPackLen(String packLen){		
		NumberFormat nf=new DecimalFormat("000000");		
		return nf.format(Integer.parseInt(packLen));					
	}
	
	//��0���ַ�����һ�����Ƚ����Ҷ���
	public static String getFieldFormat(String len,String fieldVal){
		String formatStr="";
		for(int i=0;i<(Integer.parseInt(len)-fieldVal.getBytes().length);i++){
			formatStr="0"+formatStr;
		}
		return formatStr+fieldVal;
	}
	
	//��" "���ַ�����һ�����Ƚ����Ҷ���
	public static String getFieldFormatDef(String len,String fieldVal){
		String formatStr="";
		//����ַ����ĳ��ȴ���format�ĳ��ȣ�ֱ�ӷ���
		if(fieldVal.getBytes().length>=Integer.parseInt(len)){
			byte[] temp=new byte[Integer.parseInt(len)];
			for(int i=0;i<temp.length;i++){
				temp[i]=fieldVal.getBytes()[i];
			}
			return new String(temp);
		}
		for(int i=0;i<(Integer.parseInt(len)-fieldVal.getBytes().length);i++){
			formatStr=" "+formatStr;
		}
		return formatStr+fieldVal;
	}
		
	
	public static String[] subBytes(int begInx,int endInx,String str){	
		String[] retArray=new String[2];
		byte[] fieldVal=new byte[endInx-begInx];
		byte[] leftVal=new byte[str.getBytes().length-endInx];
		int j=0;
		//��ȡ�ֽ�
		for(int i=begInx;i<endInx;i++,j++){
			fieldVal[j]=str.getBytes()[i];
		}
		retArray[0]=new String(fieldVal);
		//�����ֽ�
		j=0;
		for(int i=endInx;i<str.getBytes().length;i++,j++){
			leftVal[j]=str.getBytes()[i];			
		}
		retArray[1]=new String(leftVal);
		return retArray;
	}
	
	//���� 999400~999499 �������Ա��
	public static String randomTeller(){
		String teller="9994";
		NumberFormat nf = new DecimalFormat("00");
        teller+= nf.format(Math.random() * 100);
        return teller.substring(0,6);
	}

	//�ַ��������	
	public static String leftStr(String len,String fieldVal){
		String formatStr="";
		for(int i=0;i<(Integer.parseInt(len)-fieldVal.getBytes().length);i++){
			formatStr+=" ";
		}
		return fieldVal+formatStr;
	}	
	
	//�ַ�����һ����ʽ�Ҷ���	
	public static String rightStrFormat(String len,String fieldVal,String format){
		String formatStr="";
		for(int i=0;i<(Integer.parseInt(len)-fieldVal.getBytes().length);i++){
			formatStr+=format;
		}
		return formatStr+fieldVal;
	}
	
	//�ַ�����һ����ʽ�����	
	public static String leftStrFormat(String len,String fieldVal,String format){
		String formatStr="";
		for(int i=0;i<(Integer.parseInt(len)-fieldVal.getBytes().length);i++){
			formatStr+=format;
		}
		return fieldVal+formatStr;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		//String str="000155|@222222222222222222222222222222222222222222@|111122222222222222222222222222222222222222222222222222222222222222222222222222222222222222223333333#|4|";
		//System.out.println(str.length());
		//ACEPackager test=new ACEPackager(str);
		//ACEPackUtil.backACEPack(str.getBytes());		
		//System.out.println(ACEPackUtil.getACEPackLen("0997"));
		//System.out.println(ACEPackUtil.getFieldFormat("3", ""));
		
		/*
		String temp="12345678�й�";
		System.out.println("["+ACEPackUtil.subBytes(2,12,temp)[0]+"]");
		System.out.println("["+ACEPackUtil.subBytes(2,12,temp)[1]+"]");
		
		for (int i=0;i<10000;i++){
			System.out.println(ACEPackUtil.randomTeller());
		}*/
		
		//System.out.println("["+ACEPackUtil.leftStr("10", "1")+"]");
		
		//System.out.println("["+ACEPackUtil.getFieldFormatDef("8", "20070912161616")+"]");			
		
		
		System.out.println(ACEPackUtil.rightStrFormat("10", "1234", "0"));
	}
}