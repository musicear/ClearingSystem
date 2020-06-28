package resoft.tips.chqxh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Э��ɾ��/p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 11:02:00
 */
public class SfxySc implements Action {    
	private static final Log logger = LogFactory.getLog(SfxySc.class);    
    public int execute(Message msg) throws Exception {
    	    	
    	ACEPackager ace2010=(ACE2010)msg.get("ACEObj");
    	//��ʼ��ACE���ױ�����
    	ace2010.makeTransBody();
    	
    	String taxPayCode=(String)ace2010.pkTHBodyList.get("TaxPayCode");		//��˰�˱���
    	String payAcct=(String)ace2010.pkTHBodyList.get("PayAcct");				//�����ʺ�
    	String protocolNo=(String)ace2010.pkTHBodyList.get("ProtocolNo");			//Э�����
    	
    	//��ѯ�Ƿ���Э����Ϣ    	
    	int count=DBUtil.queryForInt("select count(*) from ProveInfo where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
    	logger.info("�ź�����Э��ɾ����select count(*) from ProveInfo where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"'  and protocolNo='"+protocolNo+"' ");
        if(count>0){
        	//���Э��Ϊ��������Э�飬��������������Э��
        	count=DBUtil.queryForInt("select count(*) from ProveInfo where verifyResult='0' and EnabledFlag='Y' and payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
        	if (count<=0){
	        	//����Э��
	        	String removeTeller=(String)ace2010.pkTHHeadList.get("InputTeller");//ɾ����Ա
	        	DateFormat dt=new SimpleDateFormat("yyyyMMddhhkkss");
	            String removeTime=dt.format(new Date());
	        	DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',removeTeller='"+removeTeller+"',removeTime='"+removeTime+"' where payAcct='"+payAcct+"' and taxPayCode='"+taxPayCode+"' and protocolNo='"+protocolNo+"' ");
	        	
	        	ace2010.tradeStatus="000";
	        	msg.set("VCResult",ace2010.tradeStatus);
	        	msg.set("AddWord", "Э��ɾ���ɹ�");        	
	        }else {
	        	ace2010.tradeStatus="002";
	        	msg.set("VCResult",ace2010.tradeStatus);
	        	msg.set("AddWord", "��Э�鲻������");
	        }
        }else{        	
        	ace2010.tradeStatus="001";
        	msg.set("VCResult",ace2010.tradeStatus);
        	msg.set("AddWord", "Э�鲻����");        	
        }
    	    	
    	/**
    	 * ���׷�����Ϣ
    	 * 		�ɹ���״̬��[3]|�ɹ���Ϣ
    	 * 		ʧ�ܣ�״̬��[3]|������Ϣ����
    	 * */
    	if (ace2010.tradeStatus.equals("000")) {//�ɹ�
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	return SUCCESS;
    }
    
}
