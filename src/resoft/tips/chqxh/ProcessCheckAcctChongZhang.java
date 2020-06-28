package resoft.tips.chqxh;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>�����ź����̨����,TIPS���е�ʱ����һ�γ���</p>
 * Author: liwei
 * Date: 2007-10-10
 * Time: 09:36:06
 */
public class ProcessCheckAcctChongZhang implements Action{
	
	public int execute(Message msg) throws Exception {
        //������Ϣ
		String tipsDate = msg.getString("ChkDate");        		//��������
        String chkAcctType=msg.getString("ChkAcctType");		//��������
        if(chkAcctType.equals("1")){							//0:�ռ�;1:����
        	//���г���    	
    		String sql = "select * from adjustAcct where adjustStatus='0' and chkDate='"+tipsDate+"' order by traNo";
            List adjustLists = QueryUtil.queryRowSet(sql);
            for (int i=0;i<adjustLists.size();i++){
            	//��ʼ��������Ϣ	    		
	    		Map row=(Map)adjustLists.get(i);  
		    	ACE2033 ace2033=new ACE2033();
		    	ace2033.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("PayAcct")));
		    	ace2033.packTags.put("OldTradeNo",ACEPackUtil.leftStrFormat("10", (String)row.get("TraNo"), " "));
		    	ace2033.packTags.put("NewTradeNo","++++++++++");
		    	ace2033.packTags.put("TipsDate",tipsDate);
		    	ace2033.packTags.put("TradeAmount", (String)row.get("TraAmt"));
		    	ace2033.packTags.put("TaxOrgCode", (String)row.get("taxOrgCode"));
		    	//�������ʵ�ʱ����Ҫԭ����
		    	if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
		    		ace2033.packTags.put("OldPackNo", (String)row.get("packNo"));
		    	}
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
			    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='���ʳɹ�' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");			    		
			    		//�޸Ŀۿ���ϸ�е�״̬
			    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
			    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='2' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}else {
			    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='2' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}			    		
			    	}else {//����ʧ��
			    		//�޸Ŀۿ���ϸ�е�״̬
			    		String failInfo=((String)ace2033.packTags.get("FailInfo")).trim();
			    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='"+failInfo+"' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
			    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}else {
			    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}
			    	}
		    	}else {//��ʱ������������
		    		//�޸Ŀۿ���ϸ�е�״̬
		    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='������������' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
		    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		}else {
		    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		}
		    	}
            }        	
        }
        return SUCCESS;
    }		
}
