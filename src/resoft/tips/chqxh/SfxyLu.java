package resoft.tips.chqxh;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>����Э����Ϣ¼��</p>
 * Author: liwei
 * Date: 2007-08-08
 * Time: 17:54:00
 */
public class SfxyLu implements Action {    
	private static final Log logger = LogFactory.getLog(SfxyLu.class);	
    
	public int execute(Message msg) throws Exception {
    	String payBkCode="402653000011",taxOrgName="",payOpBkCode="",payOpBkName="";    	
    	ACEPackager ace2008=(ACE2008)msg.get("ACEObj");
    	//��ʼ��ACE���ױ�����
    	ace2008.makeTransBody();
    	
    	Map params=new HashMap();
    	params.put("PayAcct", ace2008.pkTHBodyList.get("PayAcct"));				//�ʺ�
    	params.put("TaxOrgCode", ace2008.pkTHBodyList.get("TaxOrgCode"));		//���ջ��ش���
    	params.put("TaxPayCode", ace2008.pkTHBodyList.get("TaxPayCode"));		//��˰�˱���
    	params.put("TaxPayName", ace2008.pkTHBodyList.get("TaxPayName"));		//��˰������
    	params.put("HandOrgName", ace2008.pkTHBodyList.get("PayAcctName"));		//�ɿλ����
    	params.put("ProtocolNo", ace2008.pkTHBodyList.get("ProtocolNo"));		//Э���    	    	
    	params.put("RegisterOrgCode", ace2008.pkTHHeadList.get("BankOrgCode")); //�Ǽǻ���
    	params.put("payBkCode", payBkCode);										//�������к�
    	
    	//ȡ���ջ�������    	
    	taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and TaxOrgCode='"+ace2008.pkTHBodyList.get("TaxOrgCode")+"'");
    	if (taxOrgName!=null && !taxOrgName.equals("")){
    		msg.set("TaxOrgName", taxOrgName);
    		ace2008.tradeStatus="000";
    	}else{		
    		ace2008.tradeStatus="011";				//���ջ��ش������
    		msg.set("VCResult", ace2008.tradeStatus);			
    		msg.set("AddWord", "���ջ��ش����");    		
    	}
        //������ջ��ش�����ȡ����֤���д���
    	if (ace2008.tradeStatus.equals("000")) { 	
    		//�������д���ת��
			payOpBkCode=DBUtil.queryForString("select DMBENO from pmdma where DMSBNO='"+ace2008.pkTHHeadList.get("BankOrgCode")+"' ");			    	
    		if (payOpBkCode!=null && !payOpBkCode.equals("")){
    			msg.set("PayOpBkCode", payOpBkCode);            
    	        params.put("PayOpBkCode", payOpBkCode);	//��������к�                	        
    	        ace2008.tradeStatus="000";
    		}else {	
    			ace2008.tradeStatus="012";				//���ջ��ش������
    			msg.set("VCResult", ace2008.tradeStatus);			
    			msg.set("AddWord", "���д����");
    		}
        }
    	//����Э���¼��
        if (ace2008.tradeStatus.equals("000")) {
        	//������Э������Ƿ����   ע���������ݿ���Э�����Ψһ��ֻ��ʾһ��Э���¼
        	int count=DBUtil.queryForInt("select count(*) from proveInfo where ProtocolNo='"+(String)ace2008.pkTHBodyList.get("ProtocolNo")+"'");
        	if (count<=0) {
	        	//�����Ƿ�����Ч��Э��     ע��һ����˰��ֻ����һ����Ч��Э�飬����Ҫǩ���Э��   	
	        	count=DBUtil.queryForInt("select count(*) from ProveInfo where EnabledFlag='Y' and taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' ");
		        if(count<=0){
		        	//��ѯ�Ƿ��гɹ�ǩԼ����Ϣ
			    	count=DBUtil.queryForInt("select count(*) from ProveInfo where verifyResult='0' and EnabledFlag='Y' and taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' and payAcct='"+params.get("PayAcct")+"' and protocolNo='"+params.get("ProtocolNo")+"' ");
			    	if (count>0) {//�Ѿ�����ǩԼ    		
			    		ace2008.tradeStatus="001";			//��ǩԼ
			    		msg.set("VCResult", ace2008.tradeStatus);			
			    		msg.set("AddWord", "��ǩԼ");    		
			    	}else {	    		
			    		//��ɾ����¼��
			    		//DBUtil.executeUpdate("delete from ProveInfo where taxOrgCode='"+params.get("TaxOrgCode")+"' and taxPayCode='"+params.get("TaxPayCode")+"' and payAcct='"+params.get("PayAcct")+"' and protocolNo='"+params.get("ProtocolNo")+"'");	    		
			    		//¼����Ϣ
			    		params.put("InputTeller", ace2008.pkTHHeadList.get("InputTeller"));
			        	DateFormat dt=new SimpleDateFormat("yyyyMMddhhkkss");
			            params.put("SendTime",dt.format(new Date()));			//��������
			            params.put("VerifyResult", "1");						//0:ͨ����1:δͨ��
			            params.put("EnabledFlag", "Y");							//Y:��Ч��1:��Ч
			            params.put("RegisterTime", dt.format(new Date()));		//�Ǽ�����		                        
			    		logger.info(params);
			            DBUtil.insert("ProveInfo", params);	           
			            //���׳ɹ�
			            ace2008.tradeStatus="000";
			            msg.set("VCResult", ace2008.tradeStatus);			//�ɹ�
			            msg.set("AddWord", "�ɹ�ǩԼ");            	            	    	
			    	}
		        }else {
		        	ace2008.tradeStatus="002";			//���и���˰�˵�Э����Ϣ��������¼��
		    		msg.set("VCResult", ace2008.tradeStatus);			
		    		msg.set("AddWord", "���и���˰��Э����Ϣ");   
		        }
        	}else {
        		ace2008.tradeStatus="004";				//����Э�������Ϣ��������¼��
	    		msg.set("VCResult", ace2008.tradeStatus);			
	    		msg.set("AddWord", "���и�Э����ţ������ظ�ʹ��");
        	}
        }    	
    	
    	/**
    	 * ���׷�����Ϣ
    	 * 		�ɹ���״̬��[3]|���ջ�������|�������к�
    	 * 		ʧ�ܣ�״̬��[3]|������Ϣ����
    	 * */
    	if (ace2008.tradeStatus.equals("000")) {//�ɹ�
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("TaxOrgName")+"|"+msg.getString("PayOpBkCode"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	
    	logger.info("���ؽ��ױ����壺["+msg.getString("ACETrandBody")+"]");
    	return SUCCESS;
    }    
}
