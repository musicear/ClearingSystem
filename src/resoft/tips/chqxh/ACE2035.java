package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>重庆信合与后台400对帐交易</p>
 * Author: liwei
 * Date: 2007-08-30
 * Time: 14:06:06
 * */
public class ACE2035 extends ACEPackager{
	
	private static final Log logger = LogFactory.getLog(ACE2035.class);
	
	//ACE交易包头信息   		100
	private String []tradeHeadList={"TradeCode","OrgCode","TradeTeller","AccreditTeller","Accredit","TermNum","TransType","SepPorcessName","TransCode","TradeType","VersionNo"};
	private String []tradeHeadLenFormat={"4","6","6","6","6","8","3","50","7","1","3"};
	//ACE交易包体信息	 		45
	private String []tradeBodyList={"TipsDate","FileName","SumAmt","SumCount","TipsOrd"};
	private String []tradeBodyLenFormat={"8","10","15","8","4"};	
	//ACE成功回执明细信息  	38
	private String []replaySuccList={"MarkId","Times","BankTradeDate","BankTradeTime","BankTradeNo"};
	private String []replaySuccLenFormat={"7","2","8","6","15"};
	//ACE失败回执明细信息		67
	private String []replayFailList={"MarkId","FailInfo"};
	private String []replayFailLenFormat={"7","60"};
			
	public Map packTags=new HashMap();
	
	public ACE2035(String packStr){
		super(packStr);						
	}	
	
	public ACE2035(){
		super();
		this.transCode="2035";								//记账交易
		//交易报文头
		packTags.put("TradeCode","2035");					//交易码 	4
		packTags.put("OrgCode","009999");					//机构		6  		009999
		packTags.put("TradeTeller",ACEPackUtil.randomTeller());//交易柜员6  		999400~999499
		packTags.put("AccreditTeller","000000");			//授权柜员	6  		000000
		packTags.put("Accredit","000000");					//授权		6  		000000
		packTags.put("TermNum","M1290001");					//终端号		8  		M1290001
		packTags.put("TransType","129");					//业务类别	3  		129
		packTags.put("SepPorcessName","");					//特殊子程序名50  
		packTags.put("TransCode","1290001");				//业务代码	7  		1290001
		packTags.put("TradeType","5");						//交易类型	1		5
		packTags.put("VersionNo", "000");					//版本号		3		000
		//交易报文体
		packTags.put("TipsDate","");						//TIPS日期	8								--原交易日期
		packTags.put("FileName", "");						//文件名称	10
		packTags.put("SumAmt", "");							//合计金额	15,2
		packTags.put("SumCount", "");						//总比数		8
		packTags.put("TipsOrd", "");						//对账批次	4
		
		//交易状态码
		packTags.put("MarkId", "");							//标识		7
		//交易成功回执信息
		packTags.put("Times", "");							//次数		2
		packTags.put("BankTradeDate", "");					//交易日期	8
		packTags.put("BankTradeTime", "");					//交易时间	6
		packTags.put("BankTradeNo", "");					//柜员流水号	15
		//交易失败回执信息
		packTags.put("FailInfo","");						//失败信息	60		
	}			
	
	//拼接报文头
	public void initTransHead(){
		this.packTransHead="";
		for(int i=0;i<tradeHeadList.length;i++){
			this.packTransHead+=ACEPackUtil.getFieldFormat(tradeHeadLenFormat[i], (String)packTags.get(tradeHeadList[i]));
		}
		//logger.info("ACE2035 transHead：["+this.packTransHead+"]");
	}
	//拼接报文体
	public void initTransBody(){
		this.packTransBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			this.packTransBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}
		//logger.info("ACE2035 transBody：["+this.packTransBody+"]");
	}
	
	//处理返回交易报文
	public void makeTransBody() throws Exception{		
		String temp=this.packTransBody;
		String[] tmpArray=null;			
		
		logger.info("对账回执：["+temp+"],length:"+temp.length()+",Bytes len:"+temp.getBytes().length);
		
		//取交易状态码		
		this.packTags.put(replaySuccList[0], ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[0]),temp)[0].trim());
		
		//交易成功
		if(packTags.get(replaySuccList[0]).equals("AAAAAAA")){
			/*
			for(int i=0;i<replaySuccList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replaySuccList[i], tmpArray[0].trim());				
			}
			*/
		}else{//交易失败
			for(int i=0;i<replayFailList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replayFailLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replayFailList[i], tmpArray[0].trim());
			}
		}
	}	
}
