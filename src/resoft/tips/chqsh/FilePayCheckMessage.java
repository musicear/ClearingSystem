package resoft.tips.chqsh;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import resoft.basLink.util.DBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import com.sybase.jdbc3.jdbc.Convert;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.CurrencyUtil;

/**
 * ������ϸ��Ϣ
 * @author fanchengwei
 * 
 * Update by liwei 2008.10.25
 * Description���޸Ĵ������̣�����˶��ڿۿ�ɹ���δ���˵Ĵ���
 */
public class FilePayCheckMessage {

	private static final Log logger = LogFactory.getLog(FilePayCheckMessage.class);
	private static final String TL_TD = "880";
	private String sql = "";
	public String payCheck = "", lineStartFormat = "    ", payOpBkCode = "";
	public Map packTags = new HashMap();
	
	public FilePayCheckMessage() {
		
	}

	/**
	 * ��������ͳ��ȫ�еĶ�����Ϣ
	 * @throws SQLException 
	 */
	public String initPayCheck(String WorkDate, String PreWorkDate,String BranchNo,String PayeeBankNo) throws SQLException {

		payCheck += "\n\n\n\n";
		payCheck += "                           ���˽�����";
		payCheck += "\n";
		payCheck += lineStartFormat;
		payCheck += ACEPackUtil.leftStrFormat("5", "���", " ");
		payCheck += ACEPackUtil.leftStrFormat("14", "�տ����к�", " ");
		payCheck += ACEPackUtil.leftStrFormat("10", "��������", " ");
		payCheck += ACEPackUtil.leftStrFormat("6", "����", " ");
		payCheck += ACEPackUtil.leftStrFormat("6", "����", " ");
		payCheck += ACEPackUtil.leftStrFormat("8", "�ܱ���", " ");
		payCheck += ACEPackUtil.leftStrFormat("20", "�ܽ��", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "δ���˱���", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "δ���˽��", " ");
		//payCheck += ACEPackUtil.leftStrFormat("8", "�������", " ");
		payCheck += '\n';
		
		
	    sql = "select * from paycheck where CHKDATE='" + WorkDate + "' and PayeeBankNo='"+PayeeBankNo+"'";
		
		logger.info("��������ͳ��ȫ�ж������ SQL is ��" + sql);
		String result = "",rtsql = "",bpsql = "";	
		int noCheckCount=0;		//δ�����ܱ���
		double noCheckAmt=0;	//δ�����ܽ��
		rtsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from realtimepayment where tipsWorkDate='" + WorkDate + "' and PayeeBankNo='" + PayeeBankNo + "'";
		bpsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from batchpackdetail where tipsWorkDate='" + WorkDate + "' and PayeeBankNo='" + PayeeBankNo + "'";			
		// ��ѯʵʱ������������Ϣ
		List rtqueryList = null;
		List bpqueryList = null;
		try {
			logger.info("ʵʱ��˰������� SQL is�� " + rtsql);
			logger.info("������˰������� SQL is�� " + bpsql);
			rtqueryList = QueryUtil.queryRowSet(rtsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "�������ݿ��������";
			payCheck += '\n';
			return payCheck;
		}

		if ((rtqueryList.size() + bpqueryList.size()) == 0){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "δ�ܲ�ѯ����ؼ�¼������";
			payCheck += '\n';
			return payCheck;
		}
		
		if (rtqueryList.size() > 0 || bpqueryList.size() >0) {
			
			Map row = null;			
			//����ʵʱ
			for(int k=0;k<rtqueryList.size();k++){
				row = (Map) rtqueryList.get(k);
				String rtstr = (String) row.get("traAmt");
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3") ){ 		//�ۿ�ɹ���û�н��ж���
					noCheckCount++;
					noCheckAmt= noCheckAmt + Double.parseDouble(rtstr);
				}		
			}
			//��������
			for (int k=0;k<bpqueryList.size();k++){
				row = (Map) bpqueryList.get(k);
				String bpstr = (String) row.get("traAmt");				
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				String checkDate=(String)row.get("CHKDATE");
				if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")){ 		//�ۿ�ɹ���û�н��ж���
					noCheckCount++;
					noCheckAmt+=Double.parseDouble(bpstr);
				}
			}
		}
		
		
		
		
		// ��ѯ������Ϣ
		List queryList = null;		
		try {
			queryList = QueryUtil.queryRowSet(sql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += "�������ݿ��������";
			payCheck += '\n';
			return payCheck;
		}
		if (queryList.size() == 0) {						
			payCheck += '\n';
			payCheck += "û�и�TIPS�������ڵĶ�����Ϣ������";
			payCheck += '\n';
			return payCheck;			
		}
		//��װ�������Ķ�����Ϣ
		if (queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				Map row = (Map) queryList.get(i);
				payCheck += lineStartFormat + id(i);
				payCheck += ACEPackUtil.leftStrFormat("14", (String) row.get("payeeBankNo"), " ");
				payCheck += ACEPackUtil.leftStrFormat("10", (String) row.get("chkDate"), " ");
				payCheck += ACEPackUtil.leftStrFormat("6", (String) row.get("chkAcctOrd"), " ");
				String chkAcctType = (String) row.get("chkAcctType");
				if ("0".equals(chkAcctType)) {
					chkAcctType = "�ռ�";
					noCheckCount = 0;
					noCheckAmt = 0.0;
				} else {
					chkAcctType = "����";
				}
				String allamtcount=(String) row.get("allAmt");
				double allamtdouble=Convert.objectToDoubleValue(allamtcount);
				payCheck += ACEPackUtil.leftStrFormat("6", chkAcctType, " ");
				payCheck += ACEPackUtil.leftStrFormat("8", (String) row.get("allNum"), " ");
				payCheck += ACEPackUtil.leftStrFormat("20", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+allamtdouble)), " ");
				payCheck += ACEPackUtil.leftStrFormat("11", ""+noCheckCount , " ");
				payCheck += ACEPackUtil.leftStrFormat("11", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+noCheckAmt)), " ");
				//payCheck += ACEPackUtil.leftStrFormat("8", (String) row.get("payTreasFlag"), " ");
				payCheck += '\n';
			}
		}
		return payCheck;
	}

	/**
	 * ������ͳ�ƶ������
	 */
	public String initPayCheckDetail(String WorkDate, String PreWorkDate,String BranchNo){
		
		int noCheckCount=0;		//δ�����ܱ���
		double noCheckAmt=0;	//δ�����ܽ��
		
		int succCount=0;		//�ɹ������ܱ���
		double succAmt=0;		//�ɹ������ܽ��
		
		int failCount=0;		//ʧ���ܱ���
		double failAmt=0;		//ʧ���ܽ��
		
		String result = "",rtsql = "",bpsql = "";		

		SendMsgToBankSystem send = new SendMsgToBankSystem();
		payCheck = "\n\n\n\n" + "                               ���˽�����" + "\n";
		payCheck += "\n";
		payCheck += lineStartFormat ;
		payCheck += ACEPackUtil.leftStrFormat("10", "TIPS����", " "); 
		payCheck += ACEPackUtil.leftStrFormat("13", "�ɹ����˱���", " ");
		payCheck += ACEPackUtil.leftStrFormat("17", "�ɹ����˽��", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "δ���˱���", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "δ���˽��", " "); 
		payCheck += ACEPackUtil.leftStrFormat("9", "�쳣����", " "); 
		payCheck += ACEPackUtil.leftStrFormat("9", "�쳣���", " ");
		payCheck += '\n';
		
		/*
		//���̨ȡ���������������ת��
		sql = "select p.bkdbnkcod from ps_bkd p where p.bkdbnknpno='" + BranchNo + "'";
		payOpBkCode = send.sendMsg(sql, TL_TD + (System.currentTimeMillis() % 4 + 1));
		int i = payOpBkCode.indexOf(10);
		int j = payOpBkCode.lastIndexOf(10);
		if(!payOpBkCode.substring(i-1, i).equals("1")){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "δ�ܲ�ѯ�����������кţ�";
			payCheck += '\n';
			return payCheck;
		}
		try{
			payOpBkCode = payOpBkCode.substring(i + 1, j);
		}catch(Exception e){
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "δ�ܲ�ѯ�����������кţ�";
			payCheck += '\n';
			return payCheck;
		}*/		
		
		rtsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from realtimepayment where tipsWorkDate='" + WorkDate + "' and BRANCHNO='" + BranchNo.trim() + "'";
		bpsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from batchpackdetail where tipsWorkDate='" + WorkDate + "' and BRANCHNO='" + BranchNo.trim() + "'";			
		
		// ��ѯʵʱ������������Ϣ
		List rtqueryList = null;
		List bpqueryList = null;
		try {
			logger.info("ʵʱ��˰������� SQL is�� " + rtsql);
			logger.info("������˰������� SQL is�� " + bpsql);
			rtqueryList = QueryUtil.queryRowSet(rtsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "�������ݿ��������";
			payCheck += '\n';
			return payCheck;
		}

		if ((rtqueryList.size() + bpqueryList.size()) == 0){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "δ�ܲ�ѯ����ؼ�¼������";
			payCheck += '\n';
			return payCheck;
		}
		
		if (rtqueryList.size() > 0 || bpqueryList.size() >0) {
			
			Map row = null;			
			//����ʵʱ
			for(int k=0;k<rtqueryList.size();k++){
				row = (Map) rtqueryList.get(k);
				String rtstr = (String) row.get("traAmt");
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				if ( result.equals("90000") && checkStatus.equals("1") ) {	 		//�ۿ�ɹ������˳ɹ�
					succCount++;
					succAmt= succAmt + Double.parseDouble(rtstr);
				}else if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")){ 		//�ۿ�ɹ���û�н��ж���
					noCheckCount++;
					noCheckAmt= noCheckAmt + Double.parseDouble(rtstr);
				}else {														 		//�쳣����ۿ���Ϣ
					failAmt = failAmt + Double.parseDouble(rtstr);
					failCount++;
				}
			}
			//��������
			for (int k=0;k<bpqueryList.size();k++){
				row = (Map) bpqueryList.get(k);
				String bpstr = (String) row.get("traAmt");				
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				String checkDate=(String)row.get("CHKDATE");
				if ( result.equals("90000") && checkStatus.equals("1") ) {			//�ۿ�ɹ������˳ɹ�
					succCount++;
					succAmt+=Double.parseDouble(bpstr);
				}else if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")	){ 		//�ۿ�ɹ���û�н��ж���
					noCheckCount++;
					noCheckAmt+=Double.parseDouble(bpstr);
				}else {														 		//�쳣����ۿ���Ϣ
					failAmt += Double.parseDouble(bpstr);
					failCount++;
				}
			}
		}
		
		logger.info("\n succCount is:"+succCount+",succAmt is:"+succAmt+",noCheckCount is:"+noCheckCount+",noCheckAmt is:"+noCheckAmt+",failCount is:"+failCount+",failAmt is:"+failAmt);
		
		payCheck += lineStartFormat;
		payCheck += ACEPackUtil.leftStrFormat("10", WorkDate, " ");		
		payCheck += ACEPackUtil.leftStrFormat("13", ""+succCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("17", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+succAmt)), " ");
		payCheck += ACEPackUtil.leftStrFormat("11", ""+noCheckCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("11", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+noCheckAmt)), " ");
		payCheck += ACEPackUtil.leftStrFormat("9", ""+failCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("9", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+failAmt)), " ");
		payCheck += '\n';
		
		return payCheck;
	
	}
	
	//yangyuanxu add 
	public int getStatus(String BranchNo) throws SQLException{
		int len = 0;
		len=DBUtil.queryForInt("select count(*) from bank_relation where bankorgcode='"+BranchNo+"'");	
		if(len > 0)
			return 1;
		else
			return 0;
		
	}
	//yangyuanxu add 
	public String getPayeeBankNo(String BranchNo) throws SQLException{
		String PayeeBankNo = "";
		PayeeBankNo=DBUtil.queryForString("select PayeeBankNo from bank_relation where bankorgcode='"+BranchNo+"'");	
		PayeeBankNo=PayeeBankNo.trim();
		return PayeeBankNo;
	}	
		
	public String id(int i) {
		String format = "     ";
		String buf = "";
		buf = "" + (i + 1);
		buf = buf + format.substring(0, format.length() - buf.length());
		return buf;
	}
	
}

