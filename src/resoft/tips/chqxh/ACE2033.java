package resoft.tips.chqxh;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>�����ź����̨400����</p>
 * Author: liwei
 * Date: 2007-08-21
 * Time: 11:17:06
 * */
public class ACE2033 extends ACEPackager{
	
	private static final Log logger = LogFactory.getLog(ACE2033.class);
	
	//ACE���װ�ͷ��Ϣ   	100
	private String []tradeHeadList={"TradeCode","OrgCode","TradeTeller","AccreditTeller","Accredit","TermNum","TransType","SepPorcessName","TransCode","TradeType","VersionNo"};
	private String []tradeHeadLenFormat={"4","6","6","6","6","8","3","50","7","1","3"};
	//ACE���װ�����Ϣ	 	163+12
	private String []tradeBodyList={"PayAcct","TradeAmount","TipsDate","NewTradeNo","OldTradeNo","OldPackNo","JDFlag","TaxOrgCode"};
	private String []tradeBodyLenFormat={"19","15","8","10","10","8","1","12"};	
	//ACE�ɹ���ִ��ϸ��Ϣ 38
	private String[] replaySuccList={"MarkId","Times","BankTradeDate","BankTradeTime","BankTradeNo"};
	private String[] replaySuccLenFormat={"7","2","8","6","15"};
	//ACEʧ�ܻ�ִ��ϸ��Ϣ	67
	private String[] replayFailList={"MarkId","FailInfo"};
	private String[] replayFailLenFormat={"7","60"};
			
	public Map packTags=new HashMap();
	
	public ACE2033(String packStr){
		super(packStr);						
	}	
	
	public ACE2033(){
		super();
		this.transCode="2033";								//���˽���
		//���ױ���ͷ
		packTags.put("TradeCode","2033");					//������ 		4
		packTags.put("OrgCode","009999");					//����		6  		009999
		packTags.put("TradeTeller",ACEPackUtil.randomTeller());//���׹�Ա	6  		999400~999499
		packTags.put("AccreditTeller","000000");			//��Ȩ��Ա	6  		000000
		packTags.put("Accredit","000000");					//��Ȩ		6  		000000
		packTags.put("TermNum","M1290001");					//�ն˺�		8  		M1290001
		packTags.put("TransType","129");					//ҵ�����	3  		129
		packTags.put("SepPorcessName","");					//�����ӳ�����	50  
		packTags.put("TransCode","1290001");				//ҵ�����	7  		1290001
		packTags.put("TradeType","5");						//��������	1		5
		packTags.put("VersionNo", "000");					//�汾��		3		000
		//���ױ�����
		packTags.put("PayAcct", "");						//�ۿ��˺�	19
		packTags.put("TradeAmount","");						//���׽��	15,2
		packTags.put("TipsDate","");						//TIPS����	8								--ԭ��������
		packTags.put("NewTradeNo", "");						//�½ɿ� ID	10
		packTags.put("OldTradeNo","");						//�ɽɿ� ID	10								--TIPS��ˮ��
		packTags.put("OldPackNo","");						//ԭ����		8								
		packTags.put("JDFlag","0");							//������	1		0
		packTags.put("TaxOrgCode", "");						//���ջ���	12
		
		//����״̬��
		packTags.put("MarkId", "");							//��ʶ		7
		//���׳ɹ���ִ��Ϣ
		packTags.put("Times", "");							//����		2
		packTags.put("BankTradeDate", "");					//��������	8
		packTags.put("BankTradeTime", "");					//����ʱ��	6
		packTags.put("BankTradeNo", "");					//��Ա��ˮ��	15
		//����ʧ�ܻ�ִ��Ϣ
		packTags.put("FailInfo","");						//ʧ����Ϣ	60
		
	}				

	//ƴ�ӱ���ͷ
	public void initTransHead(){
		this.packTransHead="";
		for(int i=0;i<tradeHeadList.length;i++){
			this.packTransHead+=ACEPackUtil.getFieldFormat(tradeHeadLenFormat[i], (String)packTags.get(tradeHeadList[i]));
		}
		logger.info("ACE2033 transHead��["+this.packTransHead+"]");
	}
	//ƴ�ӱ�����
	public void initTransBody(){
		this.packTransBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			this.packTransBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}
		logger.info("ACE2033 transBody��["+this.packTransBody+"]");
	}
	
	//�����ؽ��ױ���
	public void makeTransBody() throws Exception{		
		String temp=this.packTransBody;
		String[] tmpArray=null;			
		
		logger.info("���ʻ�ִ��["+temp+"],length:"+temp.length()+",Bytes len:"+temp.getBytes().length);
		
		//ȡ����״̬��		
		this.packTags.put(replaySuccList[0], ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[0]),temp)[0].trim());
		
		//���׳ɹ�
		if(packTags.get(replaySuccList[0]).equals("AAAAAAA")){			
			for(int i=0;i<replaySuccList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replaySuccList[i], tmpArray[0].trim());				
			}
		}else{//����ʧ��
			for(int i=0;i<replayFailList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replayFailLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replayFailList[i], tmpArray[0].trim());
			}
		}
	}
}
