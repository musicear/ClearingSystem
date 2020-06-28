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
 * <p>与TIPS进行对账处理。</p>
 * <p>按照日期遍历扣税对账明细，根据对账明细中的流水号等信息搜索实时扣税表、批量扣税表(和银行缴款表)。
 * 若在实时扣税表、批量扣税表(和银行缴款表)中查询到相应记录，则将其“成功对账”标志设为“1”；理论上不存在查询不到记录的情况。
 * 这样，则实时扣税表、批量扣税表(和银行缴款表)中对账标志为“0”的则为需要冲账的记录。
 * 批量修改实时扣税表、批量扣税表(和银行缴款表)中对账状态为0的记录的“冲账标志”为“1”。</p>
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
        if(tipsDetails.size()==0){//银行发没有明细的报文下来,则直接返回成功报文,更改数据库中成功扣税记录标志为1,表示要冲帐
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
        //上述执行完毕后，批量修改实时扣税表、批量扣税表中对账状态为0的记录的“冲账标志”为“1”。
        //实时扣税表、批量扣税表中对账标志为“0”的则为需要冲账的记录。
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
            //查找实时扣税表
            String sql = "select count(*) from RealtimePayment " +
                    " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cnt = DBUtil.queryForInt(sql);
            if (cnt == 1) {
                //成功，并更改相应税票的对账状态
                sql = "update RealtimePayment set checkStatus='1' " +
                        " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }else{//
            //查找银行缴款表
            String sqlDeclare = "select count(*) from DeclareInfo " +
                          " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
            int cntDeclare = DBUtil.queryForInt(sqlDeclare);
            if (cntDeclare == 1) {
            //成功，并更改相应税票的对账状态
             sql = "update DeclareInfo set checkStatus='1' " +
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
                sql = "update BatchPackDetail set checkStatus='1' " +
                        " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and traNo='" + traNo + "'";
                DBUtil.executeUpdate(sql);
            }
        }
    }//end 
    private void updateSuccStatusAndRush(String chkDate,String chkAcctOrd){
    	String[] tables = {"RealtimePayment", "BatchPackDetail","DeclareInfo"};
    	//查找数据库中没有对帐也没有冲帐切成功的记录,将其对帐标志改为3,冲帐标志改为1,并记录“调账信息表”中
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
//          更改对帐状态1为3
            DBUtil.executeUpdate("update "+tableName +" set checkStatus='3',revokeStatus='1'"+adjWhe);
        }  
        }catch (Exception e) {
            logger.error("更新记录标志失败", e);
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
        params.put("reason", "此记录TIPS不对帐,需要冲帐");
        String delSql="delete from AdjustAcct where bankTraDate='"+params.get("bankTraDate")+
                      "' and bankTraNo='"+params.get("bankTraNo")+"' and payAcct='"+params.get("payAcct")+
                      "' and traAmt="+params.get("traAmt")+" and chkDate='"+chkDate+"'";
        DBUtil.executeUpdate(delSql);
        DBUtil.insert("AdjustAcct", params);
    }    
}
