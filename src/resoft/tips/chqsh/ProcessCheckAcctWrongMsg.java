package resoft.tips.chqsh;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;
/**
 * <p>�������д��������Ϣ</p>
 * Author: liwei
 * Date: 2007-10-11
 * Time: 09:36:06
 */

public class ProcessCheckAcctWrongMsg implements Action{	
	
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWrongMsg.class);
	
	public int execute(Message msg) {
    	
		try{
			String tipsDate = msg.getString("ChkDate");        		//��������
			//String chkAcctOrd=msg.getString("ChkAcctOrd");		//��������
	        String chkAcctType=msg.getString("ChkAcctType");		//��������
	        Map params=new HashMap();
	        
	        if(chkAcctType.equals("1")){		//0:�ռ�;1:����
	        	
	        	//ɾ��������Ϣ
	        	DBUtil.executeUpdate("delete from adjustacct where chkDate='"+tipsDate+"' ");
	        	
	        	//����ʵʱ        	
	    		String sql = "select * from RealtimePayment where checkStatus='0' and tipsWorkDate='"+tipsDate+"' order by traNo";
	    		logger.info("ǰ�ô���ʵʱ�����Ϣ��"+sql);
	            List realTimePayLists = QueryUtil.queryRowSet(sql);
	            for (int i=0;i<realTimePayLists.size();i++){            	    	
		    		Map row=(Map)realTimePayLists.get(i);  
		    		params.put("chkDate", tipsDate);
		            params.put("payOpBkCode", (String) row.get("payOpBkCode"));
		            params.put("entrustDate", (String) row.get("entrustDate"));
		            params.put("bankTraDate", (String) row.get("bankTraDate"));
		            params.put("bankTraNo", (String) row.get("bankTraNo"));
		            params.put("taxOrgCode", (String) row.get("taxOrgCode"));
		            params.put("payAcct", (String) row.get("payAcct"));
		            params.put("traAmt", new Double((String) row.get("traAmt")));
		            params.put("JRN_NO",(String)row.get("JRN_NO"));
		            params.put("VCH_NO", (String)row.get("VCH_NO"));
		            params.put("MSG_DATA", (String)row.get("MSG_DATA"));
		            params.put("TR_CODE", (String)row.get("TR_CODE"));
		            params.put("packNo", "");
		            params.put("traNo", (String)row.get("traNo"));
		            //����״̬��ȷ����Ϣ����Ҫ����
		            //��������Ӧ�ý�result='90000'������δ���˵ļ�¼���г��ʣ���Ϊ��������Ѿ����������
		            //��result='90000'��Ӧ�ļ�¼��adjustStatus��Ϊ"0"����ʾ��ҪĨ��
		            if(row.get("result").equals("90000") && row.get("MSG_DATA").equals("9004")) {
		            	params.put("adjustStatus", "0");
		            }else {
		            	params.put("adjustStatus", "1");
		            }
		            //�Ѿ����ʳɹ���Ҳ�������
		            if (((String)row.get("revokeStatus")).equals("2") ) {
		            	params.put("adjustStatus", "1");
		            }	            
		            params.put("reason", (String)row.get("addWord"));
		            DBUtil.insert("AdjustAcct", params);
		            params.clear();
	            }
	        	
	            
	            //��������        	
	    		sql = "select * from BatchPackDetail where checkStatus='0' and tipsWorkDate='"+tipsDate+"' order by traNo";
	    		logger.info("ǰ�ô������������Ϣ��"+sql);
	            List batchLists = QueryUtil.queryRowSet(sql);
	            for (int i=0;i<batchLists.size();i++){            	
		    		Map row=(Map)batchLists.get(i);	
		    		params.put("chkDate", tipsDate);
		            params.put("payOpBkCode", (String) row.get("payOpBkCode"));
		            params.put("entrustDate", (String) row.get("entrustDate"));
		            params.put("bankTraDate", (String) row.get("bankTraDate"));
		            params.put("bankTraNo", (String) row.get("bankTraNo"));
		            params.put("taxOrgCode", (String) row.get("taxOrgCode"));
		            params.put("payAcct", (String) row.get("payAcct"));
		            params.put("traAmt", new Double((String) row.get("traAmt")));
		            params.put("packNo", (String)row.get("packNo"));
		            params.put("JRN_NO",(String)row.get("JRN_NO"));
		            params.put("VCH_NO", (String)row.get("VCH_NO"));
		            params.put("MSG_DATA", (String)row.get("MSG_DATA"));
		            params.put("TR_CODE", (String)row.get("TR_CODE"));
		            params.put("traNo", ACEPackUtil.rightStrFormat("10",(String)row.get("traNo"),"0"));
		            //����״̬��ȷ����Ϣ����Ҫ����
		            if(row.get("result").equals("90000") && row.get("MSG_DATA").equals("9004")){
		            	params.put("adjustStatus", "0");
		            }else {
		            	params.put("adjustStatus", "1");
		            }
		            //�Ѿ����ʳɹ����������
		            if ( ((String)row.get("revokeStatus")).equals("2") ) {
		            	params.put("adjustStatus", "1");
		            }	            
		            params.put("reason", (String)row.get("addWord"));
		            DBUtil.insert("AdjustAcct", params);
		            params.clear();
	            }                                  
	        
	        }
	    }catch(Exception e){
	    	logger.info("���������Ϣ�쳣��");
	    	e.printStackTrace();        	
        }
        
        return SUCCESS;
    }
	
	public static void main(String[] args){
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
		Message msg=new DefaultMessage();
		msg.set("ChkDate", DateTimeUtil.getDateString());
		msg.set("ChkAcctType", "1");						//0:�ռ�;1:����
		(new ProcessCheckAcctWrongMsg()).execute(msg);
	}
	
	
}
