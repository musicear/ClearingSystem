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
 * <p>与TIPS进行对账处理。</p>
 * <p>按照日期遍历扣税对账明细，根据对账明细中的流水号等信息搜索实时扣税表、批量扣税表(和银行缴款表)。
 * 若在实时扣税表、批量扣税表(和银行缴款表)中查询到相应记录，则将其“成功对账”标志设为“1”；理论上不存在查询不到记录的情况。
 * 这样，则实时扣税表、批量扣税表(和银行缴款表)中对账标志为“0”的则为需要冲账的记录。
 * 批量修改实时扣税表、批量扣税表(和银行缴款表)中对账状态为0的记录的“冲账标志”为“1”。</p>
 */
public class ProcessCheckAcctWithTips implements Action { 
	//yangyuanxu update
	String chkDate="",chkAcctOrd="",payeeBankNo="";
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithTips.class);
    public int execute(Message msg) throws Exception {  
    	logger.info("      ProcessCheckAcctWithTips       ");
        chkDate = msg.getString("ChkDate");				//对账日期
        chkAcctOrd=msg.getString("ChkAcctOrd");			//对账批次
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
        
        String chkAcctType=msg.getString("ChkAcctType");		//对账类型 
        if(chkAcctType.equals("0")){							//0:日间;1:日切
			//日间不与行内进行核对
			return FAIL;
		}
        if(chkAcctType.equals("1")){							//0:日间;1:日切
	        //扣税成功但未对账置为 需要账务处理状态
	        //yangyuanxu update
        	logger.info("update realTimePayMent set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='需进行账务核对' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
        	DBUtil.executeUpdate("update realTimePayMent set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='需进行账务核对' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
	        DBUtil.executeUpdate("update batchPackdetail set checkStatus='3',chkDate='"+chkDate+"',result='00003',addWord='需进行账务核对' where checkStatus='0' and result='90000' and chkDate='"+chkDate+"' and payeeBankNo='" + payeeBankNo + "' ");
	  
        }
       
        return SUCCESS;
    }

    private void checkAndUpdateStatus(String packNo, String taxOrgCode, String entrustDate, String traNo, String payeeBankNo) throws SQLException {
    	if (packNo == null || packNo.equals("")) {
            //查找实时扣税表
        	//yangyuanxu update
        	String sql = "select count(*) from RealtimePayment " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
            int cnt = DBUtil.queryForInt(sql);
          //yangyuanxu update
            if (cnt == 1) {
                //成功，并更改相应税票的对账状态
                sql = "update RealtimePayment set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
	            //查找银行缴款表
            	//yangyuanxu update
	            String sqlDeclare = "select count(*) from DeclareInfo " +
	                                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
	            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
	            if (cntDeclare == 1) {
	            	//成功，并更改相应税票的对账状态
	            	//yangyuanxu update
		            sql = "update DeclareInfo set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
		                  " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
		            DBUtil.executeUpdate(sql);
	            }    
            }
        } else {
            //查找批量包明细
        	//yangyuanxu update
            String sql = "select count(*) from BatchPackDetail " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
            System.out.println("sql is:"+sql);
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //成功，并更改相应税票的对账状态
            	//yangyuanxu update
                sql = "update BatchPackDetail set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "' and payeeBankNo='" + payeeBankNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }
}
