package resoft.tips.chqxh;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>�����ź����̨400����</p>
 * Author: liwei
 * Date: 2007-08-19
 * Time: 16:52:06
 * */
public class ACE2032 extends ACEPackager{
	
	private static final Log logger = LogFactory.getLog(ACE2032.class);
	
	//ACE���װ�ͷ��Ϣ   100
	private String []tradeHeadList={"TradeCode","OrgCode","TradeTeller","AccreditTeller","Accredit","TermNum","TransType","SepPorcessName","TransCode","TradeType","VersionNo"};
	private String []tradeHeadLenFormat={"4","6","6","6","6","8","3","50","7","1","3"};
	//ACE���װ�����Ϣ	 163
	private String []tradeBodyList={"TipsDate","TradeNo","PackNo","TaxOrgCode","UserNo","TradeAmount","XZFlag","MonenyType","PayAcct","AcctType","JDFlag","TaxVodNo","ZFCondition","QFDate","QKPass","ZJHM"};
	private String []tradeBodyLenFormat={"8","10","8","12","32","15","1","2","19","1","1","13","1","8","12","20"};	
	//ACE�ɹ���ִ��ϸ��Ϣ  138
	private String[] replaySuccList={"MarkId","Times","BankTradeDate","BankTradeTime","BankTradeNo","AcctOrg","QsAcct","ClientName","AcctYE"};
	private String[] replaySuccLenFormat={"7","2","8","6","15","6","19","60","15"};
	//ACEʧ�ܻ�ִ��ϸ��Ϣ	67
	private String[] replayFailList={"MarkId","FailInfo"};
	private String[] replayFailLenFormat={"7","60"};
	
		
	public Map packTags=new HashMap();
	
	public ACE2032(String packStr){
		super(packStr);						
	}	
	
	public ACE2032(){
		super();
		this.transCode="2032";								//���˽���
		//���ױ���ͷ
		packTags.put("TradeCode","2032");					//������ 		4
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
		packTags.put("TipsDate","");						//TIPS ����	8
		packTags.put("TradeNo","");							//������ˮ��	10
		packTags.put("PackNo","");							//����		8
		packTags.put("TaxOrgCode","");						//���ջ���	12
		packTags.put("UserNo","");							//�û����	32
		packTags.put("TradeAmount","");						//���׽��	15,2
		packTags.put("XZFlag","1");							//��ת��־	1
		packTags.put("MonenyType","01");					//����		2
		packTags.put("PayAcct", "");						//�ۿ��˺�	19
		packTags.put("AcctType","0");						//�˻�����	1   	0
		packTags.put("JDFlag","0");							//�����־	1		0
		packTags.put("TaxVodNo","");						//ƾ֤����	13
		packTags.put("ZFCondition","2");					//֧������	1
		packTags.put("QFDate","00000000");					//ǩ������	8	
		packTags.put("QKPass","888888000000");				//ȡ������	12	
		packTags.put("ZJHM", "");							//֤������	20
		//����״̬��
		packTags.put("MarkId", "");							//��ʶ		7
		//���׳ɹ���ִ��Ϣ
		packTags.put("Times", "");							//����		2
		packTags.put("BankTradeDate", "");					//��������	8
		packTags.put("BankTradeTime", "");					//����ʱ��	6
		packTags.put("BankTradeNo", "");					//��Ա��ˮ��	15
		packTags.put("AcctOrg", "");						//�ʻ���������	6
		packTags.put("QsAcct", "");							//�����ʻ�	19
		packTags.put("ClientName", "");						//�ͻ�������	60	
		packTags.put("AcctYE", "");							//�ʺ����	15,2
		//����ʧ�ܻ�ִ��Ϣ
		packTags.put("FailInfo","");						//ʧ����Ϣ	60
		
	}			
	

	//ƴ�ӱ���ͷ
	public void initTransHead(){
		this.packTransHead="";
		for(int i=0;i<tradeHeadList.length;i++){
			this.packTransHead+=ACEPackUtil.getFieldFormat(tradeHeadLenFormat[i], (String)packTags.get(tradeHeadList[i]));
		}
		//logger.info("ACE2032 transHead��["+this.packTransHead+"]");
	}
	//ƴ�ӱ�����
	public void initTransBody(){
		this.packTransBody="";
		for(int i=0;i<tradeBodyList.length;i++){
			//logger.info("["+tradeBodyList[i]+"]["+(String)packTags.get(tradeBodyList[i])+"]");
			this.packTransBody+=ACEPackUtil.getFieldFormat(tradeBodyLenFormat[i],(String)packTags.get(tradeBodyList[i]));
		}
		//logger.info("ACE2032 transBody��["+this.packTransBody+"]");
	}
	
	//�����ؽ��ױ���
	public void makeTransBody() throws Exception{		
		String temp=this.packTransBody;
		String[] tmpArray=null;			
		
		logger.info("���˻�ִ��["+temp+"],Bytes len:"+temp.getBytes().length);
		
		//ȡ����״̬��		
		this.packTags.put(replaySuccList[0], ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[0]),temp)[0].trim());
				
		//���׳ɹ�
		if(packTags.get(replaySuccList[0]).equals("AAAAAAA")){
			/*
			for(int i=0;i<replaySuccList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replaySuccLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replaySuccList[i], tmpArray[0].trim());				
			}*/
			
		}else{//����ʧ��
			for(int i=0;i<replayFailList.length;i++){
				tmpArray=ACEPackUtil.subBytes(0,Integer.parseInt(replayFailLenFormat[i]),temp);
				temp=tmpArray[1];
				this.packTags.put(replayFailList[i], tmpArray[0].trim());
			}
		}
		
	}
}
