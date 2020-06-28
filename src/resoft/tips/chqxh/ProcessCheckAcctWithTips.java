package resoft.tips.chqxh;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
 * Author: liwei
 * Date: 2007-10-14
 * Time: 0:36:08
 */
public class ProcessCheckAcctWithTips implements Action {
	String chkDate="",chkAcctOrd="";
    public int execute(Message msg) throws Exception {    	
        chkDate = msg.getString("ChkDate");				//对账日期
        chkAcctOrd=msg.getString("ChkAcctOrd");			//对账批次
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
        //上述执行完毕后，批量修改实时扣税表、批量扣税表中对账状态为0的记录的“冲账标志”为“1”。
        //实时扣税表、批量扣税表中对账标志为“0”的则为需要冲账的记录。
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
            //查找实时扣税表
            String sql = "select count(*) from RealtimePayment " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //成功，并更改相应税票的对账状态
                sql = "update RealtimePayment set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
	            //查找银行缴款表
	            String sqlDeclare = "select count(*) from DeclareInfo " +
	                                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
	            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
	            if (cntDeclare == 1) {
	            	//成功，并更改相应税票的对账状态
		            sql = "update DeclareInfo set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
		                  " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
		            DBUtil.executeUpdate(sql);
	            }    
            }
        } else {
            //查找批量包明细
            String sql = "select count(*) from BatchPackDetail " +
                         " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //成功，并更改相应税票的对账状态
                sql = "update BatchPackDetail set checkStatus='1',chkDate='"+chkDate+"',chkAcctOrd='"+chkAcctOrd+"' " +
                      " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }
}
