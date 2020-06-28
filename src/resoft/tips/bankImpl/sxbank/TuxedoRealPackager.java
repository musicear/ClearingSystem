package resoft.tips.bankImpl.sxbank;

import java.sql.SQLException;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;

public class TuxedoRealPackager {
	
	
	String ZYDM = "TIPS扣税";
	//返回实时交易报文体
	private static final Log logger = LogFactory.getLog(TuxedoRealPackager.class);
	public String RealTimePackger(String protocolNo,String PayAcct,String TraAmt ,String payeeBankNo,String WorkDate) throws SQLException{
		
		String PackName = getYwbsh(payeeBankNo);//要查询bank_relation表 通过payeebankno字段关联 ywbsh
		String returnstring =ToChangeDate(PackName.getBytes().length)+PackName +ToChangeDate(protocolNo.getBytes().length) + protocolNo + GetForm1(PayAcct,TraAmt) + GetForm2(payeeBankNo,TraAmt) + ToChangeDate(WorkDate.getBytes().length) + WorkDate;
		
		//logger.info("报文体长度：["+returnstring.length()+"]");
		return returnstring;
		
	}
	
	
	//获取F76411表单数据
	
	public String GetForm1(String PayAcct,String TraAmt){
		String formName = "F76411";
		TraAmt = DoubleToString(TraAmt);
		String str1 = ToChangeDate(formName.getBytes().length)+formName + ToChangeDate(1) + ToChangeDate(25) + ToChangeDate(PayAcct.trim().getBytes().length) + PayAcct.trim() + ToChangeDate(0) + ToChangeDate(1) +"0"+ ToChangeDate(4)+"0001" + ToChangeDate(0)+ToChangeDate(0);//表单头与1--5项
		String str2 = ToChangeDate(TraAmt.getBytes().length) + TraAmt + ToChangeDate(1) + "0" + ToChangeDate(2) + "01" + ToChangeDate(0);//表单6--10项
		String str3 = ToChangeDate(1) + "0" +ToChangeDate(1)+"0"+ToChangeDate(1)+"1"+ToChangeDate(1) + "1"+ToChangeDate(ZYDM.getBytes().length)+ZYDM+ToChangeDate(0);//表单11-15项
		String str4 = ToChangeDate(1) +"0"+ToChangeDate(1)+"0" +ToChangeDate(0) +ToChangeDate(0);//表单16--20项
		String str5 = zeroToStr("00000");//表单21--25
		return str1 + str2 + str3 + str4 + str5 ;
		
	}
	
	//获取F76412表单数据

	    public String GetForm2(String payeeBankNo,String TraAmt) throws SQLException{
		String formName = "F76412";
		String PayAcct = getPayAcct(payeeBankNo);
		TraAmt = DoubleToString(TraAmt);
		
		String str1 = ToChangeDate(formName.getBytes().length)+formName + ToChangeDate(1) + ToChangeDate(25) + ToChangeDate(PayAcct.trim().getBytes().length) + PayAcct.trim() + ToChangeDate(0) + ToChangeDate(1) +"0"+ ToChangeDate(4)+"0001" + ToChangeDate(0)+ToChangeDate(0);//表单头与1--5项
		String str2 = ToChangeDate(TraAmt.getBytes().length) + TraAmt + ToChangeDate(1) + "0" + ToChangeDate(2) + "01" + ToChangeDate(0);//表单6--10项
		String str3 = ToChangeDate(1) + "0" +ToChangeDate(1)+"1"+ToChangeDate(1)+"1"+ToChangeDate(1) + "1" +ToChangeDate(ZYDM.getBytes().length)+ZYDM+ ToChangeDate(0);//表单11-15项
		String str4 =ToChangeDate(1) +"0"+ToChangeDate(1) +"0"+ToChangeDate(0)+ToChangeDate(0);//表单16--20项
      	String str5 = zeroToStr("00000");//表单21--25
		return str1 + str2 + str3 + str4 + str5 ;
	}
	private String zeroToStr(String str5)
	{
		String returnstr="";
		char c = (char)0;
		for(int i=0;i<str5.length();i++)
		{
			String s=str5.substring(i,i+1);
			if(s.equals("0"))
				returnstr=returnstr+c;
			else
				returnstr=returnstr+s;
			
		}
		return returnstr;
		
	}
	//获取代理业务号
	public String GetYWH(String payeeBankNo) throws SQLException{
		String ywh = "";
		ywh=DBUtil.queryForString("select DLYWH from bank_relation where payeeBankNo='"+payeeBankNo+"'");	
		ywh=ywh.trim();
		return ywh;
	}
	
	//十六进制转换
	public char ToChangeDate(int ori){
		
		char s=(char)ori;		
		return s;
	
	}
	
	public String DoubleToString(String traAmt){
    	Double Amt = new Double(traAmt);
    	double c_Amt = Amt.doubleValue();
        DecimalFormat   df   =     new   DecimalFormat( "###############0.00");
        String b = df.format(c_Amt);
		return b;
    }
	public String getYwbsh(String payeeBankNo) throws SQLException{
		String ywbsh = "";
		ywbsh=DBUtil.queryForString("select ywbsh from bank_relation where PayeeBankNo='"+payeeBankNo+"'");	
		ywbsh=ywbsh.trim();
		return ywbsh;
	}
	public String getPayAcct(String payeeBaNo) throws SQLException{
		String payAcct = "";
		payAcct=DBUtil.queryForString("select bankpayacct from bank_relation where payeeBankNo='"+payeeBaNo+"'");	
		payAcct=payAcct.trim();
		return payAcct;
	}
}
