package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>�����ź����̨400���ʽ���</p>
 * Author: liwei
 * Date: 2007-08-30
 * Time: 14:06:06
 * */
public class ACE2035 extends ACEPackager{
	
	private static final Log logger = LogFactory.getLog(ACE2035.class);
	
	//ACE���װ�ͷ��Ϣ   		100
	private String []tradeHeadList={"TradeCode","OrgCode","TradeTeller","AccreditTeller","Accredit","TermNum","TransType","SepPorcessName","TransCode","TradeType","VersionNo"};
	private String []tradeHeadLenFormat={"4","6","6","6","6","8","3","50","7","1","3"};
	//ACE���װ�����Ϣ	 		45
	private String []tradeBodyList={"TipsDate","FileName","SumAmt","SumCount","TipsOrd"};
	private String []tradeBodyLenFormat={"8","10","15","8","4"};	
	//ACE�ɹ���ִ��ϸ��Ϣ  	38
	private String []replaySuccList={"MarkId","Times","BankTradeDate","BankTradeTime","BankTradeNo"};
	private String []replaySuccLenFormat={"7","2","8","6","15"};
	//ACEʧ�ܻ�ִ��ϸ��Ϣ		67
	private String []replayFailList={"MarkId","FailInfo"};
	private String []replayFailLenFormat={"7","60"};
			
	public Map packTags=new HashMap();
	
	public ACE2035(String packStr){
		super(packStr);						
	}	
	
	public ACE2035(){
		super();
		this.transCode="2035";								//���˽���
		//���ױ���ͷ
		packTags.put("TradeCode","2035");					//������ 	4
		packTags.put("OrgCode","009999");					//����		6  		009999
		packTags.put("TradeTeller",ACEPackUtil.randomTeller());//���׹�Ա6  		999400~999499
		packTags.put("AccreditTeller","000000");			//��Ȩ��Ա	6  		000000
		packTags.put("Accredit","000000");					//��Ȩ		6  		000000
		packTags.put("TermNum","M1290001");					//�ն˺�		8  		M1290001
		packTags.put("TransType","129");					//ҵ�����	3  		129
		packTags.put("SepPorcessName","");					//�����ӳ�����50  
		packTags.put("TransCode","1290001");				//ҵ�����	7  		1290001
		packTags.put("TradeType","5");						//��������	1		5
		packTags.put("VersionNo", "000");					//�汾��		3		000
		//���ױ�����
		packTags.put("TipsDate","");						//TIPS����	8								--ԭ��������
		packTags.put("FileName", "");						//�ļ�����	10
		packTags.put("SumAmt", "");							//�ϼƽ��	15,2
		packTags.put("SumCount", "");						//�ܱ���		8
		packTags.put("TipsOrd", "");						//��������	4
		
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
		//logger.info("ACE2035 transHead��["+this.packTransHead+"]");
	}
	//ƴ�ӱ�����
	public void initTransBody(){
		this.packTransBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			this.packTransBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}
		//logger.info("ACE2035 transBody��["+this.packTransBody+"]");
	}
	
	//�����ؽ��ױ���
	public void makeTransBody() throws Exception{		
		String temp=this.packTransBody;
		String[] tmpArray=null;			
		
		logger.info("���˻�ִ��["+temp+"],length:"+temp.length()+",Bytes len:"+temp.getBytes().length);
		
		//ȡ����״̬��		
		this.packTags.put(replaySuccList[0], ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[0]),temp)[0].trim());
		
		//���׳ɹ�
		if(packTags.get(replaySuccList[0]).equals("AAAAAAA")){
			/*
			for(int i=0;i<replaySuccList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replaySuccList[i], tmpArray[0].trim());				
			}
			*/
		}else{//����ʧ��
			for(int i=0;i<replayFailList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replayFailLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replayFailList[i], tmpArray[0].trim());
			}
		}
	}	
}
