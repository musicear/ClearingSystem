package resoft.tips.action;

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
      ����ȫ���ҵ�����ʳɹ�,�������ʾʧ��,�����������κβ���,����2111
   </p>
 * Author: zhuchangwu
 * Date: 2007-10-17
 * Time: 0:36:08
 */
public class ProcessCheckAcctWithTipsInDate implements Action {
    public int execute(Message msg) throws Exception {
        String chkDate = msg.getString("ChkDate");

        String succListStmt = "select oriTaxOrgCode,oriEntrustDate,oriPackNo,oriTraNo from PayCheckDetail " +
                " where chkDate='" + chkDate + "'";
        List tipsDetails = QueryUtil.queryRowSet(succListStmt);
        int total=tipsDetails.size();
        int succTotal=0;
        for (int i = 0; i < tipsDetails.size(); i++) {
            Map row = (Map) tipsDetails.get(i);
            String taxOrgCode = (String) row.get("oriTaxOrgCode");
            String entrustDate = (String) row.get("oriEntrustDate");
            String packNo = (String) row.get("oriPackNo");
            String traNo = (String) row.get("oriTraNo");

            int succFlag=checkAndUpdateStatus(packNo, taxOrgCode, entrustDate, traNo);
            succTotal=succTotal+succFlag;
        }
        if(total==succTotal)
        	return FAIL;
        else
            return SUCCESS;
    }

    private int checkAndUpdateStatus(String packNo, String taxOrgCode, String entrustDate, String traNo) throws SQLException {
       
    	if (packNo == null || packNo.equals("")) {
            //����ʵʱ��˰��
            String sql = "select count(*) from RealtimePayment " +
                    " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                return cnt;
            }else{//
            //�������нɿ��
            String sqlDeclare = "select count(*) from DeclareInfo " +
                          " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
            if (cntDeclare == 1) {
            	return cntDeclare;
            }else{    
            	return 0;
            }
            }//end else
        } else {
            //������������ϸ
            String sql = "select count(*) from BatchPackDetail " +
                    " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
            	return cnt;
            }else{
            	return 0;
            }
        }
    }
}

