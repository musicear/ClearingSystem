package resoft.tips.chqxh;
/**
 * <p>Ʊ�ݴ�ӡ</p>
 * Author: liwei
 * Date: 2007-10-22
 * Time: 17:56:56
 */
public class ACE2006 extends ACEPackager {	
	
	//ACE������ϸ������   ��ӡ����|���ջ���|�������˻�|��˰�˱���|������|ֹ����
	private String[] deatilNames={"PrintType","TaxOrgCode","PayAcct","TaxPayCode","StartDate","EndDate"};
	private String[] deatilNamesFormats={"1","12","19","32","8","8"};
	
	public ACE2006(String packStr){
		super(packStr);						
	}
	
	//����ACE���ױ�����
	public void makeTransBody() throws Exception{
		String temp=this.packTransBody;
		String[] tmpArray=null;			
		for(int i=0;i<deatilNames.length;i++){
			tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(deatilNamesFormats[i]),temp);
			temp=tmpArray[1];
			this.pkTHBodyList.put(deatilNames[i], tmpArray[0].trim());				
		}		
	}
}
