package resoft.tips.chqxh;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
 * Author: liwei
 * Date: 2007-10-14
 * Time: 0:36:08
 */
public class ProcessCheckAcctWithTips implements Action {
	String chkDate="",chkAcctOrd="";
    public int execute(Message msg) throws Exception {    	
        chkDate = msg.getString("ChkDate");				//��������
        chkAcctOrd=msg.getString("ChkAcctOrd");			//��������
        String succListStmt = "select oriTaxOrgCode,oriEntrustDate,oriPackNo,oriTraNo from PayCheckDetail where chkAcctOrd='"+chkAcctOrd+"' and chkDate='"+chkDate+"' ";
        List tipsDetails = QueryUtil.queryRowSet(succListStmt);
        for (int i = 0; i < tipsDetails.size(); i++) {
            Map row = (Map) tipsDetails.get(i);
            String taxOrgCode = (String) row.get("oriTaxOrgCode");
            String entrustDate = (String) row.get("oriEntrustDate");
            String packNo = (String) row.get("oriPackNo");
            String traNo = (String) row.get("oriTraNo");

            checkAndUpdateStatus(packNo, taxOrgCode, entrustDate, traNo);
        }
        
        /*
        //����ִ����Ϻ������޸�ʵʱ��˰��������˰���ж���״̬Ϊ0�ļ�¼�ġ����˱�־��Ϊ��1����
        //ʵʱ��˰��������˰���ж��˱�־Ϊ��0������Ϊ��Ҫ���˵ļ�¼��
        String sql_realTime = "update RealtimePayment set revokeStatus='1' " +
                " where checkStatus='0' and revokeStatus!='2' and chkAcctOrd='"+chkAcctOrd+"' and chkDate='"+chkDate+"' ";
        DBUtil.executeUpdate(sql_realTime);
        String sql_BPackDetail="update BatchPackDetail set revokeStatus='1' " +
                " where chkDate='" + chkDate + "' and checkStatus='0'";
        DBUtil.executeUpdate(sql_BPackDetail);
        String sql_Declare="update DeclareInfo set revokeStatus='1' " +
        " where chkDate='" + chkDate + "' and checkStatus='0'";
        DBUtil.executeUpdate(sql_Declare);
        */
                
        return SUCCESS;
    }

    private void checkAndUpdateStatus(String packNo, String taxOrgCode, String entrustDate, String traNo) throws SQLException {
        if (packNo == null || packNo.equals("")) {
            //����ʵʱ��˰��
            String sql = "select count(*) from RealtimePayment " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
                sql = "update RealtimePayment set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
	            //�������нɿ��
	            String sqlDeclare = "select count(*) from DeclareInfo " +
	                                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
	            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
	            if (cntDeclare == 1) {
	            	//�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
		            sql = "update DeclareInfo set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
		                  " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
		            DBUtil.executeUpdate(sql);
	            }    
            }
        } else {
            //������������ϸ
            String sql = "select count(*) from BatchPackDetail " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
                sql = "update BatchPackDetail set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }
}
