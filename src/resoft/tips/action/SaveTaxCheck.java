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
 * <p>保存对账信息到数据库</p>
 * Author: liguoyin
 * Date: 2007-6-14
 * Time: 16:49:33
 */

public class SaveTaxCheck implements Action {
	
	private static final Log logger = LogFactory.getLog(SaveTaxCheck.class);
	private static Object locker = new Object();
    
	public int execute(Message msg) throws Exception { 
    	synchronized (locker) {
			String filePath = msg.getString("批量文件");

			BatchXmlProcessor processor = new BatchXmlProcessor(filePath);
			processor.setHandler(new TaxCheckHandler(msg));
			processor.execute();
			if (checkNum(msg)) {
				return SUCCESS;
			} else {
				logger.error("明细笔数与总笔数不一致或皆为0，不进行后续操作！如果对帐包分包发送，请检查子包是否接收完整。");
				return FAIL;
			}
		}
    }
    
    /**
	 * 检查对帐包中明细笔数是否与总笔数一致
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
		//TODO: 多收款行对帐处理，应将查询判断条件加上payeeBankNo
		//yangyuanxu update
		String sql = "select allnum from paycheck where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String allNum = DBUtil.queryForString(sql);
		logger.info("总笔数："+allNum);
		msg.set("AllNum", allNum);
		//yangyuanxu update
		sql = "select count(*) from paycheckdetail where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String detailNum = DBUtil.queryForString(sql);
		logger.info("明细笔数："+detailNum);
		//yangyuanxu update
		sql = "select allamt from paycheck where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "' and payeeBankNo='" + payeeBankNo + "'";
		String allAmt = DBUtil.queryForString(sql);
		logger.info("总金额："+allAmt);
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
		logger.info("当前批次: " + chkAcctOrd + " 对帐总笔数:" + allNum + " 已收到明细笔数: " + detailNum + " 对帐总金额:" + allAmt + " 明细总金额:" + detailAmt);
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
     * 处理汇总部分
     * */
    public void processSum(Map sumMap) throws Exception {
    	logger.info("总账处理已做");
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
        //@todo 尚未考虑分包情况
        logger.info(sumMap);
        chkDate = (String) sumMap.get("ChkDate");
        chkAcctOrd = (String) sumMap.get("ChkAcctOrd");
        //yangyuanxu add
        payeeBankNo = (String) sumMap.get("PayeeBankNo");
        
        //若原记录已存在，则先进行删除 需要添加逻辑
       /*
        String delPayCheckDetailStmt = "delete from PayCheckDetail where chkDate='" + chkDate + "' and chkAcctOrd='" + chkAcctOrd + "'";
        DBUtil.executeUpdate(delPayCheckDetailStmt);
        
        String delPayCheckStmt = "delete from PayCheck where chkDate='" + chkDate + "' and chkAcctOrd='" + chkAcctOrd + "'";
        DBUtil.executeUpdate(delPayCheckStmt);
        */
        //去掉无需存储数据库内容
        sumMap.remove("PayBkCode");

        sumMap.remove("CurPackNo");
        sumMap.remove("CurPackAmt");
        sumMap.remove("CurPackNum");
        sumMap.remove("PriorChkAcctOrd");
        //类型转换
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
        	logger.info("对账明细信息分包处理！对账日期："+sumMap.get("ChkDate")+"，对账批次："+sumMap.get("ChkAcctOrd")+"，对账总笔数："+sumMap.get("AllNum"));
        	//logger.info(e.printStackTrace());
        }
    }
    /**
     * 处理明细部分
     * */
    public void processDetail(Map detailMap) throws Exception {
        Map params = new HashMap();

        //保存对账明细表
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
        //添加包序号作为主键之一
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
