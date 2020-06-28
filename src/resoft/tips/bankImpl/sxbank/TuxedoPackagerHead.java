package resoft.tips.bankImpl.sxbank;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class TuxedoPackagerHead {
	
	//系统信息头
	private static final Log logger = LogFactory.getLog(TuxedoPackagerHead.class);
	public String getSysMsgHead(){
		String SCAD = "    "; //目标地址 4
		String DNAD = "    "; //源地址 4
		String RSVB = " "; // 系统保留位 1 
		String MGEF = " "; //信息结束标志 1
		String MGSQ = "  "; //报文序号  2
		String IFCK = " "; //校验标志 1
		String CKVL = "        "; //校验值 8
		
		
		
		return StToChar(SCAD + DNAD + RSVB + MGEF + MGSQ + IFCK + CKVL);
	}
	private String StToChar(String oristr)
	{
		String returnstr="";
		char c = (char)0;
		for(int i=0;i<oristr.length();i++)
		{
			String s=oristr.substring(i,i+1);
			if(s.equals(" "))
				returnstr=returnstr+c;
			else
				returnstr=returnstr+s;
			
		}
		logger.info("报文头长度分别为"+returnstr.length());
		return returnstr;
	}
	//交易公共头
	public String getTransHead(){
		String WSNO = "p0000"; //交易终端号 5
		String CTNO = "    "; //城市代码 4
		String SBNO = "0101"; //机构代码 4
		String USID = "01019920";//交易柜员 8

		return StToChar(WSNO + CTNO + SBNO + USID);
	}
	
	//交易数据头
	public String getTransDateHead(int flag){
		String tranCode = null;
		if(flag == 1)					//1:实时交易 2:冲正交易 3:对账交易
			tranCode = "7645";
		else if(flag==2)
			tranCode="7867";
		else if(flag==3)
			tranCode="7629";
		String TRCD = tranCode; //交易代码 4 
		String TRSD = "    "; //交易子码 4
	    String TRMD = " "; //交易模式 1
	    String TRSQ = "  "; //交易序号 2
	    String TRLN = "  "; //本交易包长度2
	    String OFF1 = "  "; //系统偏移1 2位
	    String OFF2 = "  "; //系统偏移2 2位
	    String AUUS = "        "; //授权柜员 8
	    String AUPS = "        ";//授权密码 8
		
	    return StToChar(TRCD + TRSD + TRMD + TRSQ + TRLN + OFF1 + OFF2 + AUUS + AUPS);
	}
	
	public String getPackagerHead(int flag){
		
		return getSysMsgHead() + getTransHead() + getTransDateHead(flag);
	}
}
