package resoft.tips.chqsh;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>��TIPS���ж��˴���</p>
 * <p>�������ڱ�����˰������ϸ�����ݶ�����ϸ�е���ˮ�ŵ���Ϣ����ʵʱ��˰��������˰��(�����нɿ��)��
 * ����ʵʱ��˰��������˰��(�����нɿ��)�в�ѯ����Ӧ��¼�����䡰�ɹ����ˡ���־��Ϊ��1���������ϲ����ڲ�ѯ������¼�������
 * ��������ʵʱ��˰��������˰��(�����нɿ��)�ж��˱�־Ϊ��0������Ϊ��Ҫ���˵ļ�¼��
 * �����޸�ʵʱ��˰��������˰��(�����нɿ��)�ж���״̬Ϊ0�ļ�¼�ġ����˱�־��Ϊ��1����</p>
 */
public class ProcessCheckAcctWithTips implements Action { 
	//yangyuanxu update
	String chkDate="",chkAcctOrd="",payeeBankNo="";
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithTips.class);
    public int execute(Message msg) throws Exception {  
    	logger.info("      ProcessCheckAcctWithTips       ");
        chkDate = msg.getString("ChkDate");				//��������
        chkAcctOrd=msg.getString("ChkAcctOrd");			//��������
        //String AllNum=msg.getString("//CFX/MSG/BatchHead3111/AllNum");
        String AllNum=msg.getString("AllNum");
        //yangyuanxu add
        payeeBankNo=msg.getString("PayeeBankNo");
        //yangyuanxu update
        String succListStmt = "select oriTaxOrgCode,oriEntrustDate,oriPackNo,oriTraNo,payeeBankNo from PayCheckDetail where chkAcctOrd='"+chkAcctOrd+"' and chkDate='"+chkDate+"' and payeeBankNo='"+payeeBankNo+"' ";
        List tipsDetails = QueryUtil.queryRowSet(succListStmt); 
        for (int i = 0; i < tipsDetails.size(); i++) {
            Map row = (Map) tipsDetails.get(i);
            String taxOrgCode = (String) row.get("oriTaxOrgCode");
            String entrustDate = (String) row.get("oriEntrustDate");
            String packNo = (String) row.get("oriPackNo");
            String traNo = (String) row.get("oriTraNo");
            
            //yangyuanxu add
            String payeeBankNo = (String) row.get("payeeBankNo");
            //yangyuanxu update
            checkAndUpdateStatus(packNo, taxOrgCode, entrustDate, traNo,payeeBankNo);
        }               
        
        String chkAcctType=msg.getString("ChkAcctType");		//�������� 
        if(chkAcctType.equals("0")){							//0:�ռ�;1:����
			//�ռ䲻�����ڽ��к˶�
			return FAIL;
		}
        if(chkAcctType.equals("1")){							//0:�ռ�;1:����
	        //��˰�ɹ���δ������Ϊ ��Ҫ������״̬
	        //yangyuanxu update
        	logger.info("update realTimePayMent set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='���������˶�' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
        	DBUtil.executeUpdate("update realTimePayMent set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='���������˶�' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
	        DBUtil.executeUpdate("update batchPackdetail set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='���������˶�' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
	  
        }
       
        return SUCCESS;
    }

    private void checkAndUpdateStatus(String packNo, String taxOrgCode, String entrustDate, String traNo, String payeeBankNo) throws SQLException {
    	if (packNo == null || packNo.equals("")) {
            //����ʵʱ��˰��
        	//yangyuanxu update
        	String sql = "select count(*) from RealtimePayment " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
            int cnt = DBUtil.queryForInt(sql);
          //yangyuanxu update
            if (cnt == 1) {
                //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
                sql = "update RealtimePayment set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
	            //�������нɿ��
            	//yangyuanxu update
	            String sqlDeclare = "select count(*) from DeclareInfo " +
	                                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
	            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
	            if (cntDeclare == 1) {
	            	//�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
	            	//yangyuanxu update
		            sql = "update DeclareInfo set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
		                  " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
		            DBUtil.executeUpdate(sql);
	            }    
            }
        } else {
            //������������ϸ
        	//yangyuanxu update
            String sql = "select count(*) from BatchPackDetail " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
            System.out.println("sql is:"+sql);
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
            	//yangyuanxu update
                sql = "update BatchPackDetail set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }
}
