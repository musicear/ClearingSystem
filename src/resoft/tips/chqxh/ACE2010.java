package resoft.tips.chqxh;
/**
 * <p>����Э��ɾ��</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 13:00:00
 */
public class ACE2010 extends ACEPackager {	
	
	//ACE������ϸ������   Э�����[60]|��˰�˱���[20]|�����ʺ�[19]
	private String[] deatilNames={"ProtocolNo","TaxPayCode","PayAcct"};			
	public ACE2010(String packStr){
		super(packStr);						
	}
	
	//����ACE���ױ�����
	public void makeTransBody() throws Exception{
		String [] tmpAry=this.packTransBody.split("\\|");
		for(int i=0;i<tmpAry.length;i++){
			this.pkTHBodyList.put(deatilNames[i], tmpAry[i].trim());
		}
	}
}
