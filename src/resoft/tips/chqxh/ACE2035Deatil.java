package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * <p>重庆信合与后台400对帐明细</p>
 * Author: liwei
 * Date: 2007-09-06
 * Time: 14:06:06
 * */
public class ACE2035Deatil {
private static final Log logger = LogFactory.getLog(ACE2035Deatil.class);	
	//ACE交易包体信息	 		
	private String []tradeBodyList={"TipsDate","DaiLiCode","PayAcct","TaxOrgCode","TaxUserNo","TrsAmt","TrsNo","TipsOrd","Statue","CodeTrans"};
	private String []tradeBodyLenFormat={"8","7","19","12","32","15","10","4","1","4"};					
	public Map packTags=new HashMap();		
	
	public ACE2035Deatil() {		
		//交易报文体
		packTags.put("TipsDate","");						//TIPS日期	8								--原交易日期
		packTags.put("DaiLiCode", "1290001");				//代理代码	7
		packTags.put("PayAcct", "");						//上传账号	19
		packTags.put("TaxOrgCode", "");						//征收机构	12
		packTags.put("TaxUserNo", "");						//用户编号	32
		packTags.put("TrsAmt", "");							//交易金额	15
		packTags.put("TrsNo", "");							//交易ID		10
		packTags.put("TipsOrd", "");						//上划批次	4
		packTags.put("Statue", "0");						//记录状态	1			
		packTags.put("CodeTrans", "划  ");					//与400转码	4		
	}			
	
	//拼接报文体
	public String initTransBody(){
		String transBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			transBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}		
		//logger.info("ACE2035 transBody：["+transBody+"]");
		return transBody;
	}
}
