package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * <p>三方协议录入</p>
 * Author: liwei
 * Date: 2007-07-29
 * Time: 14:51:06
 */
public class ACE2008 extends ACEPackager {	

	private static final Log logger = LogFactory.getLog(ACE2008.class);	
	
	//帐号|付款帐号名称|征收机关代码|纳税人编码|纳税人名称|开户行行号|协议号
	private String[] deatilNames={"PayAcct","PayAcctName","TaxOrgCode","TaxPayCode","TaxPayName","PayOpBkCode","ProtocolNo"};		//ACE交易明细参名称	
	
	public ACE2008(String packStr){
		super(packStr);						
	}	
	
	//处理ACE交易报文体
	public void makeTransBody() throws Exception{
		String [] tmpAry=this.packTransBody.split("\\|");
		logger.info("Split Array Info:"+tmpAry);
		for(int i=0;i<tmpAry.length;i++){
			this.pkTHBodyList.put(deatilNames[i], tmpAry[i].trim());
		}
		logger.info("Split ACETransBody Info:"+this.pkTHBodyList);
	}
}
