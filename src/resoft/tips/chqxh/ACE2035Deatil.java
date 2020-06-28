package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * <p>�����ź����̨400������ϸ</p>
 * Author: liwei
 * Date: 2007-09-06
 * Time: 14:06:06
 * */
public class ACE2035Deatil {
private static final Log logger = LogFactory.getLog(ACE2035Deatil.class);	
	//ACE���װ�����Ϣ	 		
	private String []tradeBodyList={"TipsDate","DaiLiCode","PayAcct","TaxOrgCode","TaxUserNo","TrsAmt","TrsNo","TipsOrd","Statue","CodeTrans"};
	private String []tradeBodyLenFormat={"8","7","19","12","32","15","10","4","1","4"};					
	public Map packTags=new HashMap();		
	
	public ACE2035Deatil() {		
		//���ױ�����
		packTags.put("TipsDate","");						//TIPS����	8								--ԭ��������
		packTags.put("DaiLiCode", "1290001");				//�������	7
		packTags.put("PayAcct", "");						//�ϴ��˺�	19
		packTags.put("TaxOrgCode", "");						//���ջ���	12
		packTags.put("TaxUserNo", "");						//�û����	32
		packTags.put("TrsAmt", "");							//���׽��	15
		packTags.put("TrsNo", "");							//����ID		10
		packTags.put("TipsOrd", "");						//�ϻ�����	4
		packTags.put("Statue", "0");						//��¼״̬	1			
		packTags.put("CodeTrans", "��  ");					//��400ת��	4		
	}			
	
	//ƴ�ӱ�����
	public String initTransBody(){
		String transBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			transBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}		
		//logger.info("ACE2035 transBody��["+transBody+"]");
		return transBody;
	}
}
