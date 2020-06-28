package resoft.tips.action;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���������Ϣ�����ݿ�</p>
 * Author: liguoyin
 * Date: 2007-6-14
 * Time: 16:49:33
 */

public class SaveTaxCheck implements Action {
	
	private static final Log logger = LogFactory.getLog(SaveTaxCheck.class);
	private static Object locker = new Object();
    
	public int execute(Message msg) throws Exception { 
    	synchronized (locker) {
			String filePath = msg.getString("�����ļ�");

			BatchXmlProcessor processor = new BatchXmlProcessor(filePath);
			processor.setHandler(new TaxCheckHandler(msg));
			processor.execute();
			if (checkNum(msg)) {
				return SUCCESS;
			} else {
				logger.error("��ϸ�������ܱ�����һ�»��Ϊ0�������к���������������ʰ��ְ����ͣ������Ӱ��Ƿ����������");
				return FAIL;
			}
		}
    }
    
    /**
	 * �����ʰ�����ϸ�����Ƿ����ܱ���һ��
	 * 
	 * @param msg
	 * @return
	 * @throws SQLException
	 */
	private boolean checkNum(Message msg) throws SQLException {
		String chkDate=msg.getString("ChkDate");
		String chkAcctOrd = msg.getString("ChkAcctOrd");
		//yangyuanxu add
		String payeeBankNo = msg.getString("PayeeBankNo");
		//TODO: ���տ��ж��ʴ���Ӧ����ѯ�ж���������payeeBankNo
		//yangyuanxu update
		String sql = "select allnum from paycheck where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String allNum = DBUtil.queryForString(sql);
		logger.info("�ܱ�����"+allNum);
		msg.set("AllNum", allNum);
		//yangyuanxu update
		sql = "select count(*) from paycheckdetail where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String detailNum = DBUtil.queryForString(sql);
		logger.info("��ϸ������"+detailNum);
		//yangyuanxu update
		sql = "select allamt from paycheck where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String allAmt = DBUtil.queryForString(sql);
		logger.info("�ܽ�"+allAmt);
		//yangyuanxu update
		sql = "select sum(traamt) from paycheckdetail where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String detailAmt = DBUtil.queryForString(sql);
		if (null == detailAmt || "".equals(detailAmt) || detailAmt.equals("null")) {
			detailAmt = "0.00";
		}
		if ( null == allNum || "".equals(allNum)) {
			
			return false;
		}
		allNum = allNum.trim();
		detailNum = detailNum.trim();
		logger.info("��ǰ����: " + chkAcctOrd + " �����ܱ���:" + allNum + " ���յ���ϸ����: " + detailNum + " �����ܽ��:" + allAmt + " ��ϸ�ܽ��:" + detailAmt);
		//return (allNum.equals(detailNum) && allAmt.equals(detailAmt));
		if(allNum.equals("0")){
			return false;
		}
		return ( allNum.equals(detailNum) );
	}
}

class TaxCheckHandler implements BatchXmlHandler {
    private static final Log logger = LogFactory.getLog(TaxCheckHandler.class);
    TaxCheckHandler(Message msg) {
        this.msg = msg;
    }

    public Collection getTags() {
        String[] tags = {"BatchHead3111","BankCompare3111"};
        logger.info("return tags");
        return Arrays.asList(tags);
    }

    public void process(String tag,Map children) throws Exception {
    	logger.info("tag="+tag);
        if(tag.equals("BatchHead3111")) {
            processSum(children);
        } else if(tag.equals("BankCompare3111")) {
            processDetail(children);
        }
    }
    /**
     * ������ܲ���
     * */
    public void processSum(Map sumMap) throws Exception {
    	logger.info("���˴�������");
        msg.set("PayBkCode",sumMap.get("PayBkCode"));
        msg.set("ChkDate",sumMap.get("ChkDate"));
        msg.set("ChkAcctOrd",sumMap.get("ChkAcctOrd"));
        msg.set("PayeeBankNo",sumMap.get("PayeeBankNo"));
        msg.set("ChkAcctType",sumMap.get("ChkAcctType"));
        //msg.set("ChildPackNum", (String) sumMap.get("ChildPackNum"));
        //msg.set("CurPackNo", (String) sumMap.get("CurPackNo"));
       
        /**
         * fan
         */
        msg.set("PRINTTIMES", "0");
        //@todo ��δ���Ƿְ����
        logger.info(sumMap);
        chkDate = (String) sumMap.get("ChkDate");
        chkAcctOrd = (String) sumMap.get("ChkAcctOrd");
        //yangyuanxu add
        payeeBankNo = (String) sumMap.get("PayeeBankNo");
        
        //��ԭ��¼�Ѵ��ڣ����Ƚ���ɾ�� ��Ҫ����߼�
       /*
        String delPayCheckDetailStmt = "delete from PayCheckDetail where chkDate='" + chkDate + "' and chkAcctOrd='" + chkAcctOrd + "'";
        DBUtil.executeUpdate(delPayCheckDetailStmt);
        
        String delPayCheckStmt = "delete from PayCheck where chkDate='" + chkDate + "' and chkAcctOrd='" + chkAcctOrd + "'";
        DBUtil.executeUpdate(delPayCheckStmt);
        */
        //ȥ������洢���ݿ�����
        sumMap.remove("PayBkCode");

        sumMap.remove("CurPackNo");
        sumMap.remove("CurPackAmt");
        sumMap.remove("CurPackNum");
        sumMap.remove("PriorChkAcctOrd");
        //����ת��
        // String recvPackNum = (String) sumMap.get("RecvPackNum");
        // sumMap.put("RecvPackNum",new Integer(recvPackNum));
        String childPackNum = (String) sumMap.get("ChildPackNum");
        sumMap.put("ChildPackNum",new Integer (childPackNum));
        //curPackNo = (String) sumMap.get("CurPackNo");
        //sumMap.put("CurPackNo",new Integer (curPackNo));
        //sumMap.remove("CurPackNo");
        String allNum = (String) sumMap.get("AllNum");
        sumMap.put("AllNum",new Integer(allNum));
        String allAmt = (String) sumMap.get("AllAmt");
        sumMap.put("AllAmt",new Double(allAmt));
        
        //String CurPackNo = (String)sumMap.get("CurPackNo");
       // sumMap.put("CurPackNo",new Integer (CurPackNo));
        //sumMap.remove("")
        //sumMap.put("recvPackNum", "1");
        sumMap.put("recvPackNum", new Integer(1));
        try{
        	if(!checkAcctOrd(chkAcctOrd,payeeBankNo))
        		DBUtil.insert("PayCheck", sumMap);
        	else{
        		
        	}
        		
        }catch(Exception e){
        	logger.info("������ϸ��Ϣ�ְ������������ڣ�"+sumMap.get("ChkDate")+"���������Σ�"+sumMap.get("ChkAcctOrd")+"�������ܱ�����"+sumMap.get("AllNum"));
        	//logger.info(e.printStackTrace());
        }
    }
    /**
     * ������ϸ����
     * */
    public void processDetail(Map detailMap) throws Exception {
        Map params = new HashMap();

        //���������ϸ��
        params.put("chkDate", chkDate);
        params.put("chkAcctOrd", chkAcctOrd);
        //yangyuanxu add
        params.put("payeeBankNo", payeeBankNo);
        params.put("payOpBkCode", detailMap.get("PayOpBkCode"));
        params.put("PayAcct", detailMap.get("PayAcct"));
        params.put("OriTaxOrgCode", detailMap.get("OriTaxOrgCode"));
        params.put("OriEntrustDate", detailMap.get("OriEntrustDate"));
        String oriPackNo = (String) detailMap.get("OriPackNo");
        if(oriPackNo!=null) {
            params.put("OriPackNo", oriPackNo);
        }
        params.put("OriTraNo", detailMap.get("OriTraNo"));
        String traAmt = (String) detailMap.get("TraAmt");
        params.put("TraAmt", new Double(traAmt));
        params.put("TaxVouNo",detailMap.get("TaxVouNo"));
        params.put("TaxPayName",detailMap.get("TaxPayName"));
        params.put("ProtocolNo",detailMap.get("ProtocolNo"));
        //��Ӱ������Ϊ����֮һ
        //params.put("CurPackNo",Integer.parseInt(curPackNo));
        params.put("SuccCheck","N");
        
        
        DBUtil.insert("PayCheckDetail", params);
        logger.info(detailMap);
    }

    public void end() {

    }
    
    public boolean checkAcctOrd(String chkAcctOrd,String payeeBankNo) throws SQLException{
    	String sql="";
    	sql = "select count(*) from paycheck where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		int Acctord = DBUtil.queryForInt(sql);
		if( Acctord == 0 )
			return false;
		return true;
    }
    /*
    public boolean checkCurPackNo(String CurPackNo) throws SQLException{
    	String sql="";
    	sql = "select count(*) from paycheck where chkdate='" + chkDate + "' and curpackno = " + Integer.parseInt(CurPackNo) + "";
		int Acctord = DBUtil.queryForInt(sql);
		if( Acctord == 0 )
			return false;
		return true;
    }
    */
    private String chkDate,chkAcctOrd,payeeBankNo,curPackNo;
    private Message msg;
}
