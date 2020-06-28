package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * <p>����Э��¼��</p>
 * Author: liwei
 * Date: 2007-07-29
 * Time: 14:51:06
 */
public class ACE2008 extends ACEPackager {	

	private static final Log logger = LogFactory.getLog(ACE2008.class);	
	
	//�ʺ�|�����ʺ�����|���ջ��ش���|��˰�˱���|��˰������|�������к�|Э���
	private String[] deatilNames={"PayAcct","PayAcctName","TaxOrgCode","TaxPayCode","TaxPayName","PayOpBkCode","ProtocolNo"};		//ACE������ϸ������	
	
	public ACE2008(String packStr){
		super(packStr);						
	}	
	
	//����ACE���ױ�����
	public void makeTransBody() throws Exception{
		String [] tmpAry=this.packTransBody.split("\\|");
		logger.info("Split Array Info:"+tmpAry);
		for(int i=0;i<tmpAry.length;i++){
			this.pkTHBodyList.put(deatilNames[i], tmpAry[i].trim());
		}
		logger.info("Split ACETransBody Info:"+this.pkTHBodyList);
	}
}
