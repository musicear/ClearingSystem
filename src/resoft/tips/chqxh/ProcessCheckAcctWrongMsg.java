package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>�����źϴ��������Ϣ</p>
 * Author: liwei
 * Date: 2007-10-11
 * Time: 09:36:06
 */
public class ProcessCheckAcctWrongMsg implements Action{	
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWrongMsg.class);
	public int execute(Message msg) throws Exception {
        //������Ϣ
		String tipsDate = msg.getString("ChkDate");        		//��������
		String chkAcctOrd=msg.getString("ChkAcctOrd");			//��������
        String chkAcctType=msg.getString("ChkAcctType");		//��������
        Map params=new HashMap();
        if(chkAcctType.equals("1")){		//0:�ռ�;1:����
        	//ɾ���ɵ�����
        	DBUtil.executeUpdate("delete from adjustAcct where chkdate='"+tipsDate+"' ");        	
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
	            params.put("packNo", "");
	            params.put("traNo", (String)row.get("traNo"));
	            //����״̬��ȷ����Ϣ����Ҫ����
	            if ((String)row.get("result")!=null && ((String)row.get("result")).equals("")) {
	            	params.put("adjustStatus", "1");
	            }else {
	            	params.put("adjustStatus", "0");
	            }
	            //�Ѿ����ʳɹ���Ҳ�������
	            if (((String)row.get("revokeStatus")).equals("2") ) {
	            	params.put("adjustStatus", "1");
	            }	            
	            params.put("reason", (String)row.get("addWord"));
	            DBUtil.insert("AdjustAcct", params);
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
	            params.put("traNo", (String)row.get("traNo"));
	            //����״̬��ȷ����Ϣ����Ҫ����
	            if ((String)row.get("result")!=null && ((String)row.get("result")).trim().equals("")) {
	            	params.put("adjustStatus", "1");
	            }else {
	            	params.put("adjustStatus", "0");
	            }
	            //�Ѿ����ʳɹ����������
	            if (((String)row.get("revokeStatus")).equals("2") ) {
	            	params.put("adjustStatus", "1");
	            }
	            params.put("reason", (String)row.get("addWord"));
	            DBUtil.insert("AdjustAcct", params);	    		
            }
        }
        return SUCCESS;
    }		
}
