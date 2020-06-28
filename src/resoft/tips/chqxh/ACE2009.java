package resoft.tips.chqxh;
/**
 * <p>三方协议查询</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 08:56:56
 */
public class ACE2009 extends ACEPackager {	
	
	//ACE交易明细参名称   纳税人编码|付款帐号
	private String[] deatilNames={"TaxPayCode","PayAcct"};			
	public ACE2009(String packStr){
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
