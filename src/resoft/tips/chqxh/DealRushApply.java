package resoft.tips.chqxh;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.AbstractSyncAction;
import resoft.xlink.core.Message;
/**
 * <p>ʵʱ����</p>
 * User: liwei
 * Date: 2007-10-11
 * Time: 14:11:26
 */
public class DealRushApply extends AbstractSyncAction {
	 private static final Log logger = LogFactory.getLog(DealRushApply.class);
	 public int execute(Message msg) throws RuntimeException,Exception {
	        String tipsDate=(String)msg.getString("//CFX/HEAD/WorkDate");
	        String traNo=(String)msg.getString("//CFX/MSG/RushApply1021//OriTraNo");  
	        String taxOrgCode=msg.getString("//CFX/MSG/RushApply1021/TaxOrgCode");
            String entrustDate=msg.getString("//CFX/MSG/RushApply1021/EntrustDate");
            String sql="select * from realTimePayMent where cancelFlag='N' and taxOrgCode='"+taxOrgCode+"' and entrustDate='"+entrustDate+"' and traNo='"+traNo+"' ";
            List realTimeList=QueryUtil.queryRowSet(sql);
            if (realTimeList.size()==1) {
            	Map row=(Map)realTimeList.get(0);
            	ACE2033 ace2033=new ACE2033();
		    	ace2033.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("PayAcct")));
		    	ace2033.packTags.put("OldTradeNo", ACEPackUtil.rightStrFormat("10",(String)row.get("TraNo"),"0"));
		    	ace2033.packTags.put("NewTradeNo","++++++++++");
		    	ace2033.packTags.put("TipsDate",tipsDate);
		    	ace2033.packTags.put("TradeAmount", (String)row.get("TraAmt"));
		    	ace2033.packTags.put("TaxOrgCode", (String)row.get("taxOrgCode"));
		    	ace2033.initPack();    	    	
		    			    	
		    	//��װ����������Ϣ
		    	msg.set("ACESendObj", ace2033);
		    	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
		    	sender.sendMsg();
		    	
		    	//������˽��
		    	ace2033=(ACE2033)msg.get("ACERecObj");
		    	if ( ace2033 !=null ) {//�г���Ӧ��
			    	ace2033.makeTransBody();			    	
			    	if (((String)ace2033.packTags.get("MarkId")).trim().equals("AAAAAAA")) { //���ʳɹ�		    					    					    	
			    		//�޸Ŀۿ���ϸ�е�״̬			    		
			    		DBUtil.executeUpdate("update realTimePayMent set revokeStatus='2',cancelFlag='Y' where taxOrgCode='"+taxOrgCode+"' and entrustDate='"+entrustDate+"' and traNo='"+traNo+"'  ");			    					    		
			    	}else {//����ʧ��
			    		//�޸Ŀۿ���ϸ�е�״̬			    		
			    		DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3',cancelFlag='N' where taxOrgCode='"+taxOrgCode+"' and entrustDate='"+entrustDate+"' and traNo='"+traNo+"'  ");			    		
			    	}
		    	}else {//��ʱ������������
		    		//�޸Ŀۿ���ϸ�е�״̬
		    		DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3',cancelFlag='N' where taxOrgCode='"+taxOrgCode+"' and entrustDate='"+entrustDate+"' and traNo='"+traNo+"' ");		    		
		    	}
            }
                        
            return SUCCESS;
	 }

}
