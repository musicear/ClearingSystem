package resoft.tips.chqxh;
/**
 * <p>����Э���ѯ</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 08:56:56
 */
public class ACE2009 extends ACEPackager {	
	
	//ACE������ϸ������   ��˰�˱���|�����ʺ�
	private String[] deatilNames={"TaxPayCode","PayAcct"};			
	public ACE2009(String packStr){
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
