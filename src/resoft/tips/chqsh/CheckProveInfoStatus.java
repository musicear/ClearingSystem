package resoft.tips.chqsh;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��ѯ�Ƿ��Ѿ�ǩԼ</p>
 * Author: fanchengwei
 * 
 * Update by liwei 2008.10.29
 * Description���޸��˶����˺ŵ������кŵ���֤��ѯSQL�ĵ���
 */
public class CheckProveInfoStatus implements Action {
    
	private static final Log logger = LogFactory.getLog(CheckProveInfoStatus.class);
    
	public int execute(Message msg) throws Exception{
    	
		//String PAYBKCODE="313653000013";							//�������к�		
		String payAcct = msg.getString("PayAcct");			//�����ʺ�
        String taxPayCode = msg.getString("TaxPayCode");   	//��˰�˱���
        String taxPayName = msg.getString("TaxPayName");		//��˰������
        String protocolNo = msg.getString("ProtocolNo");		//Э�����
        String taxOrgCode = msg.getString("TaxOrgCode");		//���ջ��ش���
        String BranchNo=msg.getString("BranchNo").trim();           //�ڵ����
        String PayBkCode=msg.getString("PayBkCode");         //�����������к�
        String PayOpBkName= msg.getString("PayOpBkName");   //�����������
        String payOpBkCode=msg.getString("PayOpBkCode");     //��������к�
        String handOrgName=msg.getString("HandOrgName");
       // String AcctSeq=msg.getString("AcctSeq").trim();             //�˻�����
       // int len = payAcct.length();
        //String Check_type="";
        //Check_type = payAcct.substring(6, 8);
        //logger.info("Check_type is "+Check_type);
        if(payAcct == null || payAcct.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "�����˺Ų���Ϊ�գ�");
        	logger.info("�����˺Ų���Ϊ�գ�");
            return FAIL;
        }
        
        if(taxPayCode == null || taxPayCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "��˰�˱��벻��Ϊ�գ�");
        	logger.info("��˰�˱��벻��Ϊ�գ�");
            return FAIL;
        }
        
        if(taxPayName == null || taxPayName.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "��˰�����Ʋ���Ϊ�գ�");
        	logger.info("��˰�����Ʋ���Ϊ�գ�");
            return FAIL;
        }
        
        if(protocolNo == null || protocolNo.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "Э����Ų���Ϊ�գ�");
        	logger.info("Э����Ų���Ϊ�գ�");
            return FAIL;
        }
        
        if(taxOrgCode == null || taxOrgCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "���ջ��ش��벻��Ϊ�գ�");
        	logger.info("���ջ��ش��벻��Ϊ�գ�");
            return FAIL;
        }
       
        if(PayOpBkName == null || PayOpBkName.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "����������Ʋ���Ϊ�գ�");
        	logger.info("����������Ʋ���Ϊ�գ�");
            return FAIL;
        }
        
        if(payOpBkCode == null || payOpBkCode.equals("")){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "��������кŲ���Ϊ�գ�");
        	logger.info("��������кŲ���Ϊ�գ�");
            return FAIL;
        }
        
        if(getStatus(PayBkCode) == 0){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "�����������кŲ���ȷ��");
        	logger.info("�����������кŲ���ȷ��");
            return FAIL;
        }
       /*
        if(AcctSeq.equals("0")){
        	if(len == 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺��������˺Ų�ƥ����˺Ŵ�����������д��");
            	logger.info("�˺��������˺Ų�ƥ��");
                return FAIL;
        	}
        	if(!Check_type.equals("04")){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺��������˺Ų�ƥ����˺Ŵ�����������д��");
            	logger.info("�˺��������˺Ų�ƥ��");
                return FAIL;
        	}
        }
        if(AcctSeq.equals("1")){
        	if(len != 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺��������˺Ų�ƥ����˺Ŵ�����������д��");
            	logger.info("�˺��������˺Ų�ƥ��");
                return FAIL;
        	}
        }
        if(AcctSeq.equals("2")){
        	if(len == 16){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺��������˺Ų�ƥ����˺Ŵ�����������д��");
            	logger.info("�˺��������˺Ų�ƥ��");
                return FAIL;
        	}
        	if(!Check_type.equals("10")){
        		msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺��������˺Ų�ƥ����˺Ŵ�����������д��");
            	logger.info("�˺��������˺Ų�ƥ��");
                return FAIL;
        	}
        }*/
        if(PayBkCode == null){
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "�����������кŲ���Ϊ��");
        	logger.info("�������к�Ϊ��");
            return FAIL;
        }
        
        String sql = "select count(*) from ProveInfo where EnabledFlag='Y' and protocolNo='" + protocolNo +"'";
        logger.info("��֤�Ƿ��Ѿ���ǩԼЭ�� sql:" + sql);
        int count = 0;
        try{
        	count = DBUtil.queryForInt(sql);
        }catch(Exception e){
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "���ݿ��������");
            logger.info("���ݿ��������");
            return FAIL;
        }        
        if (count > 0){ //�Ѿ��ɹ�ǩԼ        
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "��Э������Ѿ�ǩԼ��������¼�룡");
            return FAIL;
        } else{        	
        /*	try{
    	        int result=QueryAcct.checkSzhh(msg);        
    	        if ( result!=0 ) {
    	        	return result;
    	        }
            }catch(Exception e){
            	e.printStackTrace();
            	msg.set("ReturnResult", "N");
            	msg.set("AddWord", "�˺Ŵ���");
            	return FAIL;
            }
        	*/
        	Map params=new HashMap();        	
    		params.put("PayOpBkName",PayOpBkName);                     //�����������
        	params.put("taxPayName", taxPayName);  							//��˰������
        	params.put("payacct", payAcct);        							//�����˻�
        	params.put("protocolNo", protocolNo);  							//Э�����
        	params.put("taxOrgCode", taxOrgCode);  							//���ջ��ش���
        	params.put("taxPayCode", taxPayCode);  							//��˰�˱���
        	params.put("payOpBkCode", payOpBkCode);//��������к�
        	params.put("handOrgName", handOrgName);//�ɿλ����        	       	
        	params.put("sendTime", DateTimeUtil.getDateString());			//Э��ǩ������
            params.put("verifyResult","1");									//��֤���
            params.put("AddWord","Э��ǩ���ɹ�!");    							//����
            params.put("inputTeller",msg.getString("UserId"));				//��Ա��
            params.put("EnabledFlag","Y");									//��ЧЭ��
            params.put("PAYBKCODE",PayBkCode);								//�������к�
            params.put("BranchNo", BranchNo);								//����������
            try{ 
            	String protocolNoquerysql = "select count(*) from ProveInfo where protocolNo='" + protocolNo +"'";
            	int protocolcount = DBUtil.queryForInt(protocolNoquerysql);
            	if(protocolcount>0)
            		DBUtil.executeUpdate("delete from ProveInfo where protocolNo='"+protocolNo+"'");
            	DBUtil.insert("ProveInfo",params);
            	msg.set("ReturnResult", "Y");
                msg.set("AddWord", "Э��ǩ���ɹ���");
                logger.info("Э��ǩ���ɹ�");
            }catch(Exception e){
            	e.printStackTrace();
            	msg.set("ReturnResult", "N");
                msg.set("AddWord", "���������ظ���");
                logger.info("�������ݿ����");                
                return FAIL;
            }
            return SUCCESS;
        }
    }			
	//yangyuanxu add 
	public int getStatus(String PayBkCode) throws SQLException{
		
		int len;
		len=DBUtil.queryForInt("select count(*) from bank_relation where paybankcode='"+PayBkCode+"'");	
		if(len > 0)
			return 1;
		else
			return 0;
	}	
	
}
