package resoft.tips.chqxh;
/**
 * <p>三方协议删除</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 13:00:00
 */
public class ACE2010 extends ACEPackager {	
	
	//ACE交易明细参名称   协议书号[60]|纳税人编码[20]|付款帐号[19]
	private String[] deatilNames={"ProtocolNo","TaxPayCode","PayAcct"};			
	public ACE2010(String packStr){
		super(packStr);						
	}
	
	//处理ACE交易报文体
	public void makeTransBody() throws Exception{
		String [] tmpAry=this.packTransBody.split("\\|");
		for(int i=0;i<tmpAry.length;i++){
			this.pkTHBodyList.put(deatilNames[i], tmpAry[i].trim());
		}
	}
}
