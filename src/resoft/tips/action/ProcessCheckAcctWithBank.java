package resoft.tips.action;

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
 * <p>ǰ�������к���ϵͳ�˶�����</p>
 * <p>�������к���ϵͳ�����Ķ����ļ�������������Ϣ����ʵʱ��˰��������˰��
 * ����ѯ�����ɹ���¼������Ҫ���г��ˣ�����Ϣ��¼�ڡ�������Ϣ����</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 2:32:41
 */
public class ProcessCheckAcctWithBank implements Action {
    private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithBank.class);
    String chkDate="",preChkDate="",payeeBankNo="";
	private boolean foundadjust=false;
    
    public int execute(Message msg) throws Exception {
        
    	chkDate = msg.getString("ChkDate");        				//��������
		String chkAcctType=msg.getString("ChkAcctType");		//��������
        logger.info("�������ڣ�"+chkDate);     
		payeeBankNo = msg.getString("PayeeBankNo");
		
    	//���ض�ȡ��
        CheckInfoReader reader;
        try {
            reader = (CheckInfoReader) Class.forName(checkInfoReader).newInstance();
        } catch (Exception e) {
            logger.error("������" + checkInfoReader + "ʧ��", e);
            return FAIL;
        }
        String filename = msg.getString("BankChkFileName");
        reader.setFilePath(filename);
        //�������ж�����Ϣ

        String[] tables = {"RealtimePayment", "BatchPackDetail","DeclareInfo"};
        String sqlSelect = "select traAmt,traNo,chkDate,payOpBkCode,entrustDate,checkStatus,revokeStatus,taxVouNo,taxOrgCode,tipsworkdate,bankTraNo ";
        String sqlFrom = " from ";
        String up_sql = "update ";
        String up_set = "";
        String checkStatus = "";
        boolean founddate;
        Map params = new HashMap();
        while (reader.next()) {
        	founddate = false;
        	String bankDate = reader.getString("BankDate");
            String bankTransNo = reader.getString("BanktransNo");
            String traDate = reader.getString("TraDate");
            String transNo = reader.getString("TransNo");
            String payAcct = reader.getString("DebitAcct");
            String traAmt = reader.getString("TraAmt");
            logger.info("���н������ڣ�"+bankDate+"���н�����ˮ�ţ�"+bankTransNo+"TIPS�������ڣ�"+traDate+"TIPS������ˮ�ţ�"+transNo+"�˻���"+payAcct+"��"+traAmt);
            //����������Ϣ����ʵʱ��˰��������˰��,���Ȳ�ʵʱ��˰��,�����ھ��ٲ�������˰��,���������²���
            String sqlWhere = " where result='90000' "  + 
            " and bankTraNo='" + bankTransNo + "' and bankTraDate='" + bankDate + "' and chkDate='"+chkDate+"' and payeeBankNo = '"+payeeBankNo+"'" ;
            //������Ϣ
            
            params.put("bankTraDate", bankDate);
            params.put("bankTraNo", bankTransNo);
            params.put("payAcct", payAcct);
            params.put("traAmt", new Double(traAmt));
            params.put("payeeBankNo", payeeBankNo);
            params.put("adjustStatus", "Y");
            
            List tipsDetails = QueryUtil.queryRowSet(sqlSelect + sqlFrom + tables[0] + sqlWhere);
            logger.info("ʵʱ��˰SQLΪ��"+sqlSelect + sqlFrom + tables[0] + sqlWhere);
            if (tipsDetails.size() == 1) {//��ѯ��ʵʱ��˰�ɹ���¼
            	founddate=true;
                Map row = (Map) tipsDetails.get(0);
                checkStatus = (String) row.get("checkStatus");
                if ("0".equals(checkStatus) || "1".equals(checkStatus)) {//
                	logger.info(bankTransNo + ":��ʵʱ��˰��Ϣ�Ѷ��˻�δ���ˣ��ݲ���Ҫ�ֹ�����˶�");
                }else{
                	params.put("reason", "�ý�����Ҫ�ֹ�����˶�"); 
                	this.setTabaleValue(params, row);
                }
                
            }  
            String sqlSelect_patch = sqlSelect + ",packNo ";
                tipsDetails = QueryUtil.queryRowSet(sqlSelect_patch + sqlFrom + tables[1] + sqlWhere);
                logger.info("������˰SQLΪ��"+sqlSelect_patch + sqlFrom + tables[1] + sqlWhere);
                if (tipsDetails.size() == 1) {//��ѯ���ɹ���¼
                	founddate=true;
                    Map row = (Map) tipsDetails.get(0);
                    checkStatus = (String) row.get("checkStatus");
                    if ("0".equals(checkStatus) || "1".equals(checkStatus)) {//
                    	logger.info(bankTransNo + ":��������˰��Ϣ�Ѷ��˻�δ���ˣ��ݲ���Ҫ�ֹ�����˶�");
                    }else {
                    	params.put("packNo", row.get("packNo"));
                        params.put("reason", "�ý�����Ҫ�ֹ�����˶�");
                        this.setTabaleValue(params, row);
                    }
                }
                logger.info("founddate is:"+founddate);
                 if(!founddate) 
                 { //all
                 	//���������ж���ѯ�����ɹ���¼,����Ϣ��¼�ڡ�������Ϣ����
                	 logger.info("δ�ҵ���Ӧ�ɹ���Ϣ");
               	     String sql2="";
                     String Sel2="select chkDate,traAmt,payOpBkCode,entrustDate,traNo,taxOrgCode ";
                     String sqlWhere2 = " where  traNo='" + transNo + "' and tipsWorkDate='" + traDate +"' and payeeBankNo = '"+payeeBankNo+"'" ;
                     String tableName2="";
                     for(int j=0;j<2;j++)
                     {
                     	if(j==0){
                     		tableName2=tables[0];
                     		sql2=Sel2+sqlFrom+tableName2+sqlWhere2;
                     		logger.info("ʵʱ������Ϣ��"+sql2);
                     	}
                     	if(j==1){
                     		tableName2=tables[1];
                     		sql2=Sel2+",packNo "+sqlFrom+tableName2+sqlWhere2;
                     	}  
	                     List adjustList2 = QueryUtil.queryRowSet(sql2); 
	                     if(adjustList2.size()>0)
	                     {
	                    	 foundadjust=true;
	                    	 Map row = (Map) adjustList2.get(0);
	                         params.put("packNo", row.get("PACKNO"));
	                         params.put("reason", "ǰ��δ��˰�ɹ�,�������п�˰�ɹ�");
	                         params.put("traNo", transNo);
	                         params.put("taxOrgCode", row.get("TAXORGCODE"));
	                         params.put("BRANCHNO", row.get("BRANCHNO"));
	                         params.put("checkStatus", "0");
	                         params.put("adjustStatus", "0");//��¼��ǰ��Ҳ�޷��ҵ�����Ϣ
	                         this.setTabaleValue(params, row);  	                    	 
	                    	 break;
	                     }
                     }
                     if(!foundadjust)
                     {
                    	 params.put("chkDate", chkDate);
                      	  params.put("packNo", "n");
                      	  params.put("traNo", transNo);
                      	  params.put("reason", "ǰ���޼�¼,�������д��ڴ˼�¼����˰�ɹ�");
                            params.put("taxOrgCode", "");
                            params.put("payOpBkCode", "δ֪");
                            params.put("entrustDate", "δ֪");
                            params.put("checkStatus", "0");
                            DBUtil.insert("AdjustAcct", params);
                     }
                   } 
                }
          
        return SUCCESS; 
        }

     
  
    private void setTabaleValue(Map params, Map row) {
    	logger.info("��¼������Ϣ");
        params.put("chkDate", (String) row.get("chkDate"));
        params.put("payOpBkCode", (String) row.get("payOpBkCode"));
        params.put("entrustDate", (String) row.get("EntrustDate"));

        DBUtil.insert("AdjustAcct", params);
    }
    public void setCheckInfoReader(String checkInfoReader) {
        this.checkInfoReader = checkInfoReader;
    }

    private String checkInfoReader;
}
