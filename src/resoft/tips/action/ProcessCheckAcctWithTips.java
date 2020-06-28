package resoft.tips.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
//import resoft.tips.bankImpl.qzbank.ProcessCheckAcctWithBank;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��TIPS���ж��˴���</p>
 * <p>�������ڱ�����˰������ϸ�����ݶ�����ϸ�е���ˮ�ŵ���Ϣ����ʵʱ��˰��������˰��(�����нɿ��)��
 * ����ʵʱ��˰��������˰��(�����нɿ��)�в�ѯ����Ӧ��¼�����䡰�ɹ����ˡ���־��Ϊ��1���������ϲ����ڲ�ѯ������¼�������
 * ��������ʵʱ��˰��������˰��(�����нɿ��)�ж��˱�־Ϊ��0������Ϊ��Ҫ���˵ļ�¼��
 * �����޸�ʵʱ��˰��������˰��(�����нɿ��)�ж���״̬Ϊ0�ļ�¼�ġ����˱�־��Ϊ��1����</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 0:36:08
 * Update by zhuchangwu
 */
public class ProcessCheckAcctWithTips implements Action {
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithTips.class);
    public int execute(Message msg) throws Exception {
        String chkDate = msg.getString("ChkDate");
        String chkAcctOrd=msg.getString("ChkAcctOrd");
        String succListStmt = "select oriTaxOrgCode,oriEntrustDate,oriPackNo,oriTraNo from PayCheckDetail " +
                " where chkDate='" + chkDate + "'";
        List tipsDetails = QueryUtil.queryRowSet(succListStmt);
        if(tipsDetails.size()==0){//���з�û����ϸ�ı�������,��ֱ�ӷ��سɹ�����,�������ݿ��гɹ���˰��¼��־Ϊ1,��ʾҪ����
        	updateSuccStatusAndRush(chkDate,chkAcctOrd);
        	return FAIL;
        }else{
        for (int i = 0; i < tipsDetails.size(); i++) {
            Map row = (Map) tipsDetails.get(i);
            String taxOrgCode = (String) row.get("oriTaxOrgCode");
            String entrustDate = (String) row.get("oriEntrustDate");
            String packNo = (String) row.get("oriPackNo");
            String traNo = (String) row.get("oriTraNo");

            checkAndUpdateStatus(packNo, taxOrgCode, entrustDate, traNo);
        }
        //����ִ����Ϻ������޸�ʵʱ��˰��������˰���ж���״̬Ϊ0�ļ�¼�ġ����˱�־��Ϊ��1����
        //ʵʱ��˰��������˰���ж��˱�־Ϊ��0������Ϊ��Ҫ���˵ļ�¼��
        String sql_realTime = "update RealtimePayment set revokeStatus='1' " +
                " where chkDate='" + chkDate + "' and checkStatus='0'";
        DBUtil.executeUpdate(sql_realTime);
        String sql_BPackDetail="update BatchPackDetail set revokeStatus='1' " +
                " where chkDate='" + chkDate + "' and checkStatus='0'";
        DBUtil.executeUpdate(sql_BPackDetail);
        String sql_Declare="update DeclareInfo set revokeStatus='1' " +
        " where chkDate='" + chkDate + "' and checkStatus='0'";
        DBUtil.executeUpdate(sql_Declare);        
        return SUCCESS;
        }
    }

    private void checkAndUpdateStatus(String packNo, String taxOrgCode, String entrustDate, String traNo) throws SQLException {
        if (packNo == null || packNo.equals("")) {
            //����ʵʱ��˰��
            String sql = "select count(*) from RealtimePayment " +
                    " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
                sql = "update RealtimePayment set checkStatus='1' " +
                        " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
            //�������нɿ��
            String sqlDeclare = "select count(*) from DeclareInfo " +
                          " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
            if (cntDeclare == 1) {
            //�ɹ�����������Ӧ˰Ʊ�Ķ���״̬
             sql = "update DeclareInfo set checkStatus='1' " +
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
                sql = "update BatchPackDetail set checkStatus='1' " +
                        " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }//end 
    private void updateSuccStatusAndRush(String chkDate,String chkAcctOrd){
    	String[] tables = {"RealtimePayment", "BatchPackDetail","DeclareInfo"};
    	//�������ݿ���û�ж���Ҳû�г����гɹ��ļ�¼,������ʱ�־��Ϊ3,���ʱ�־��Ϊ1,����¼��������Ϣ����
        String adjSel="select bankTraDate,bankTraNo,payOpBkCode,taxOrgCode,entrustDate,traNo,payAcct,traAmt ";
        String sqlFrom = " from ";
        String adjWhe=" where chkDate='"+chkDate+"' and checkStatus='0' and revokeStatus='0' and  result='90000' ";
        String adjSql="";
        Map rowAdj=null;
        String tableName="";
        Map params = new HashMap();
        try{
        for(int j=0;j<2;j++){
        	if(j==0){
        		tableName=tables[0];
        		adjSql=adjSel+",'' packNo "+sqlFrom+tableName+adjWhe;
        	}
        	if(j==1){
        		tableName=tables[1];
        		adjSql=adjSel+",packNo "+sqlFrom+tableName+adjWhe;
        	}
          	if(j==2){
        		tableName=tables[0];
        		adjSql=adjSel+",'' packNo "+sqlFrom+tableName+adjWhe;
          	}        	
            List adjustList = QueryUtil.queryRowSet(adjSql);
            adjSql="";
            if (adjustList.size()>0){
            	for(int adj=0;adj<adjustList.size();adj++){
            		rowAdj= (Map) adjustList.get(adj);
            		this.setAdjustAcctValue(params, rowAdj,chkDate,chkAcctOrd);
            	}
            }
//          ���Ķ���״̬1Ϊ3
            DBUtil.executeUpdate("update "+tableName +" set checkStatus='3',revokeStatus='1'"+adjWhe);
        }  
        }catch (Exception e) {
            logger.error("���¼�¼��־ʧ��", e);
        }        
    }
    private void setAdjustAcctValue(Map params, Map row,String chkDate,String chkAcctOrd) {
    	params.put("chkAcctOrd", chkAcctOrd);
        params.put("chkDate", chkDate);
        params.put("payOpBkCode", (String) row.get("payOpBkCode"));
        params.put("entrustDate", (String) row.get("entrustDate"));
        params.put("bankTraDate", (String) row.get("bankTraDate"));
        params.put("bankTraNo", (String) row.get("bankTraNo"));
        params.put("taxOrgCode", (String) row.get("taxOrgCode"));
        params.put("payAcct", (String) row.get("payAcct"));
        params.put("traAmt", new Double((String)row.get("traAmt")));
        params.put("packNo", (String) row.get("packNo"));
        params.put("traNo", (String) row.get("traNo"));
        params.put("checkStatus", "1");//
        params.put("adjustStatus", "1");
        params.put("reason", "�˼�¼TIPS������,��Ҫ����");
        String delSql="delete from AdjustAcct where bankTraDate='"+params.get("bankTraDate")+
                      "' and bankTraNo='"+params.get("bankTraNo")+"' and payAcct='"+params.get("payAcct")+
                      "' and traAmt="+params.get("traAmt")+" and chkDate='"+chkDate+"'";
        DBUtil.executeUpdate(delSql);
        DBUtil.insert("AdjustAcct", params);
    }    
}
