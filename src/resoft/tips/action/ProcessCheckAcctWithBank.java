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
 * <p>前置与银行核心系统核对账务</p>
 * <p>遍历银行核心系统发来的对账文件，根据其中信息搜索实时扣税表、批量扣税表。
 * 若查询不到成功记录，则需要进行冲账，将信息记录在“调账信息表”中</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 2:32:41
 */
public class ProcessCheckAcctWithBank implements Action {
    private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithBank.class);
    String chkDate="",preChkDate="",payeeBankNo="";
	private boolean foundadjust=false;
    
    public int execute(Message msg) throws Exception {
        
    	chkDate = msg.getString("ChkDate");        				//对账日期
		String chkAcctType=msg.getString("ChkAcctType");		//对账类型
        logger.info("对账日期："+chkDate);     
		payeeBankNo = msg.getString("PayeeBankNo");
		
    	//加载读取类
        CheckInfoReader reader;
        try {
            reader = (CheckInfoReader) Class.forName(checkInfoReader).newInstance();
        } catch (Exception e) {
            logger.error("加载类" + checkInfoReader + "失败", e);
            return FAIL;
        }
        String filename = msg.getString("BankChkFileName");
        reader.setFilePath(filename);
        //遍历银行对账信息

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
            logger.info("银行交易日期："+bankDate+"银行交易流水号："+bankTransNo+"TIPS工作日期："+traDate+"TIPS交易流水号："+transNo+"账户："+payAcct+"金额："+traAmt);
            //根据其中信息搜索实时扣税表、批量扣税表,首先查实时扣税表,不存在就再查批量扣税表,存在则往下操作
            String sqlWhere = " where result='90000' "  + 
            " and bankTraNo='" + bankTransNo + "' and bankTraDate='" + bankDate + "' and chkDate='"+chkDate+"' and payeeBankNo = '"+payeeBankNo+"'" ;
            //调账信息
            
            params.put("bankTraDate", bankDate);
            params.put("bankTraNo", bankTransNo);
            params.put("payAcct", payAcct);
            params.put("traAmt", new Double(traAmt));
            params.put("payeeBankNo", payeeBankNo);
            params.put("adjustStatus", "Y");
            
            List tipsDetails = QueryUtil.queryRowSet(sqlSelect + sqlFrom + tables[0] + sqlWhere);
            logger.info("实时扣税SQL为："+sqlSelect + sqlFrom + tables[0] + sqlWhere);
            if (tipsDetails.size() == 1) {//查询到实时扣税成功记录
            	founddate=true;
                Map row = (Map) tipsDetails.get(0);
                checkStatus = (String) row.get("checkStatus");
                if ("0".equals(checkStatus) || "1".equals(checkStatus)) {//
                	logger.info(bankTransNo + ":该实时缴税信息已对账或未对账，暂不需要手工账务核对");
                }else{
                	params.put("reason", "该交易需要手工账务核对"); 
                	this.setTabaleValue(params, row);
                }
                
            }  
            String sqlSelect_patch = sqlSelect + ",packNo ";
                tipsDetails = QueryUtil.queryRowSet(sqlSelect_patch + sqlFrom + tables[1] + sqlWhere);
                logger.info("批量扣税SQL为："+sqlSelect_patch + sqlFrom + tables[1] + sqlWhere);
                if (tipsDetails.size() == 1) {//查询到成功记录
                	founddate=true;
                    Map row = (Map) tipsDetails.get(0);
                    checkStatus = (String) row.get("checkStatus");
                    if ("0".equals(checkStatus) || "1".equals(checkStatus)) {//
                    	logger.info(bankTransNo + ":该批量缴税信息已对账或未对账，暂不需要手工账务核对");
                    }else {
                    	params.put("packNo", row.get("packNo"));
                        params.put("reason", "该交易需要手工账务核对");
                        this.setTabaleValue(params, row);
                    }
                }
                logger.info("founddate is:"+founddate);
                 if(!founddate) 
                 { //all
                 	//在三个表中都查询不到成功记录,将信息记录在“调账信息表”中
                	 logger.info("未找到对应成功信息");
               	     String sql2="";
                     String Sel2="select chkDate,traAmt,payOpBkCode,entrustDate,traNo,taxOrgCode ";
                     String sqlWhere2 = " where  traNo='" + transNo + "' and tipsWorkDate='" + traDate +"' and payeeBankNo = '"+payeeBankNo+"'" ;
                     String tableName2="";
                     for(int j=0;j<2;j++)
                     {
                     	if(j==0){
                     		tableName2=tables[0];
                     		sql2=Sel2+sqlFrom+tableName2+sqlWhere2;
                     		logger.info("实时交易信息："+sql2);
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
	                         params.put("reason", "前置未扣税成功,但是银行扣税成功");
	                         params.put("traNo", transNo);
	                         params.put("taxOrgCode", row.get("TAXORGCODE"));
	                         params.put("BRANCHNO", row.get("BRANCHNO"));
	                         params.put("checkStatus", "0");
	                         params.put("adjustStatus", "0");//记录在前置也无法找到的信息
	                         this.setTabaleValue(params, row);  	                    	 
	                    	 break;
	                     }
                     }
                     if(!foundadjust)
                     {
                    	 params.put("chkDate", chkDate);
                      	  params.put("packNo", "n");
                      	  params.put("traNo", transNo);
                      	  params.put("reason", "前置无记录,但是银行存在此记录并扣税成功");
                            params.put("taxOrgCode", "");
                            params.put("payOpBkCode", "未知");
                            params.put("entrustDate", "未知");
                            params.put("checkStatus", "0");
                            DBUtil.insert("AdjustAcct", params);
                     }
                   } 
                }
          
        return SUCCESS; 
        }

     
  
    private void setTabaleValue(Map params, Map row) {
    	logger.info("记录错帐信息");
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
