package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>�����źϹ���ϵͳ����ACE����</p>
 * Author: liwei
 * Date: 2007-07-30
 * Time: 10:51:06
 */

public class ACEPackager {
	
	private static final Log logger = LogFactory.getLog(ACEPackager.class);	
	
	protected String packLength="";				//���ĳ���
	protected String pack="";					//���屨��	
	protected String packTransHead="";			//ACE���ױ���ͷ
	protected String packTransBody="";			//ACE���ױ�����
	protected String transCode="";				//������
	protected String tradeStatus="-1";			//�����ϵͳ��ҵ����״̬  000���ɹ�  	
	
	protected Map pkTHBodyList=new HashMap();	//ACE���ױ�����Ҫ����ϸ	 
	protected Map pkTHHeadList=new HashMap();	//ACE���ױ���ͷҪ����ϸ	
	
	public ACEPackager(){
		
	}
	
	public ACEPackager(String packStr){
		try{
			//���ĸ�ʽ   ���ĳ���[6]|TransHead[100]|TransBody|;
			pack=packStr;
			packLength=ACEPackUtil.subBytes(0,6,this.pack)[0];
			packTransHead=ACEPackUtil.subBytes(6+1,7+100,this.pack)[0];
			packTransBody=ACEPackUtil.subBytes(107+1,(packStr.getBytes().length-1),this.pack)[0];
			transCode=ACEPackUtil.subBytes(0,4,this.packTransHead)[0];
			//����ACE���ױ���ͷ
			makeTransHead();
		}catch(Exception e){
			logger.info("ACE���ĳ�ʼ���쳣��");
			e.printStackTrace();
		}
		/*
		logger.info("packLength:"+packLength);		
		logger.info("packTransHead:"+packTransHead);
		logger.info("packTransBody:"+packTransBody);
		logger.info("transCode:"+transCode);
		*/
	}		
		
	
	//����ACE���ױ���ͷ
	public void makeTransHead() throws Exception{
		//���ĸ�ʽ	������[4]|������[6]|��Ա��[6]|��Ȩ��[6]|��Ȩ����[6]|�ն˺�[8]|ҵ�����[3]|�����ӳ�����[50]|ҵ�����[7]|��������[1]|�汾��[3]
		pkTHHeadList.put("TransCode", ACEPackUtil.subBytes(0,4,this.packTransHead)[0]);
		pkTHHeadList.put("BankOrgCode", ACEPackUtil.subBytes(4,10,this.packTransHead)[0]);
		pkTHHeadList.put("InputTeller", ACEPackUtil.subBytes(10,16,this.packTransHead)[0]);
		pkTHHeadList.put("AccreditCode", ACEPackUtil.subBytes(16,22,this.packTransHead)[0]);
		pkTHHeadList.put("AccreditPass", ACEPackUtil.subBytes(22,28,this.packTransHead)[0]);
		pkTHHeadList.put("TerminalNum", ACEPackUtil.subBytes(28,36,this.packTransHead)[0]);
		pkTHHeadList.put("OperationType", ACEPackUtil.subBytes(36,39,this.packTransHead)[0]);
		pkTHHeadList.put("SpecialPsName", ACEPackUtil.subBytes(39,89,this.packTransHead)[0]);
		pkTHHeadList.put("OperationCode", ACEPackUtil.subBytes(89,96,this.packTransHead)[0]);
		pkTHHeadList.put("TransType", ACEPackUtil.subBytes(96,97,this.packTransHead)[0]);
		pkTHHeadList.put("EditionNum", ACEPackUtil.subBytes(97,100,this.packTransHead)[0]);		
	}
	//����ACE���ױ�����
	public void makeTransBody() throws Exception{
		
	}
	
	//��ʼ�����ױ���ͷ
	public void initTransHead(){
		
	}
	//��ʼ�����ױ�����
	public void initTransBody(){
		
	}
	
	//��װACE����
	public void initPack() throws Exception{
		//��װACE����ͷ
		initTransHead();
		//��װACE������
		initTransBody();
		
		this.pack="|"+this.packTransHead+"|"+this.packTransBody+"|";
		int packLen=6+this.pack.getBytes().length;
		this.pack=ACEPackUtil.getACEPackLen(""+packLen)+this.pack;
		//logger.info("ACE pack��"+this.pack);
	}	
}
