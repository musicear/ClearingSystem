package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>重庆信合柜面系统交互ACE报文</p>
 * Author: liwei
 * Date: 2007-07-30
 * Time: 10:51:06
 */

public class ACEPackager {
	
	private static final Log logger = LogFactory.getLog(ACEPackager.class);	
	
	protected String packLength="";				//报文长度
	protected String pack="";					//整体报文	
	protected String packTransHead="";			//ACE交易报文头
	protected String packTransBody="";			//ACE交易报文体
	protected String transCode="";				//交易码
	protected String tradeStatus="-1";			//与核心系统的业务处理状态  000：成功  	
	
	protected Map pkTHBodyList=new HashMap();	//ACE交易报文体要素明细	 
	protected Map pkTHHeadList=new HashMap();	//ACE交易报文头要素明细	
	
	public ACEPackager(){
		
	}
	
	public ACEPackager(String packStr){
		try{
			//报文格式   报文长度[6]|TransHead[100]|TransBody|;
			pack=packStr;
			packLength=ACEPackUtil.subBytes(0,6,this.pack)[0];
			packTransHead=ACEPackUtil.subBytes(6+1,7+100,this.pack)[0];
			packTransBody=ACEPackUtil.subBytes(107+1,(packStr.getBytes().length-1),this.pack)[0];
			transCode=ACEPackUtil.subBytes(0,4,this.packTransHead)[0];
			//设置ACE交易报文头
			makeTransHead();
		}catch(Exception e){
			logger.info("ACE报文初始化异常！");
			e.printStackTrace();
		}
		/*
		logger.info("packLength:"+packLength);		
		logger.info("packTransHead:"+packTransHead);
		logger.info("packTransBody:"+packTransBody);
		logger.info("transCode:"+transCode);
		*/
	}		
		
	
	//处理ACE交易报文头
	public void makeTransHead() throws Exception{
		//报文格式	交易码[4]|机构号[6]|柜员号[6]|授权码[6]|授权密码[6]|终端号[8]|业务类别[3]|特殊子程序名[50]|业务代码[7]|交易类型[1]|版本号[3]
		pkTHHeadList.put("TransCode", ACEPackUtil.subBytes(0,4,this.packTransHead)[0]);
		pkTHHeadList.put("BankOrgCode", ACEPackUtil.subBytes(4,10,this.packTransHead)[0]);
		pkTHHeadList.put("InputTeller", ACEPackUtil.subBytes(10,16,this.packTransHead)[0]);
		pkTHHeadList.put("AccreditCode", ACEPackUtil.subBytes(16,22,this.packTransHead)[0]);
		pkTHHeadList.put("AccreditPass", ACEPackUtil.subBytes(22,28,this.packTransHead)[0]);
		pkTHHeadList.put("TerminalNum", ACEPackUtil.subBytes(28,36,this.packTransHead)[0]);
		pkTHHeadList.put("OperationType", ACEPackUtil.subBytes(36,39,this.packTransHead)[0]);
		pkTHHeadList.put("SpecialPsName", ACEPackUtil.subBytes(39,89,this.packTransHead)[0]);
		pkTHHeadList.put("OperationCode", ACEPackUtil.subBytes(89,96,this.packTransHead)[0]);
		pkTHHeadList.put("TransType", ACEPackUtil.subBytes(96,97,this.packTransHead)[0]);
		pkTHHeadList.put("EditionNum", ACEPackUtil.subBytes(97,100,this.packTransHead)[0]);		
	}
	//处理ACE交易报文体
	public void makeTransBody() throws Exception{
		
	}
	
	//初始化交易报文头
	public void initTransHead(){
		
	}
	//初始化交易报文体
	public void initTransBody(){
		
	}
	
	//封装ACE报文
	public void initPack() throws Exception{
		//封装ACE报文头
		initTransHead();
		//封装ACE报文体
		initTransBody();
		
		this.pack="|"+this.packTransHead+"|"+this.packTransBody+"|";
		int packLen=6+this.pack.getBytes().length;
		this.pack=ACEPackUtil.getACEPackLen(""+packLen)+this.pack;
		//logger.info("ACE pack："+this.pack);
	}	
}
