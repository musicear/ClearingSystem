package resoft.tips.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p> * 保存单笔实时扣税数据库 * </p>
 * User: liguoyin 
 * Date: 2007-4-24 
 * Time: 22:43:08
 */
public class SaveDB3001 implements Action {
		private static final Log logger = LogFactory.getLog(SaveDB3001.class);
		public int execute(Message msg) throws RuntimeException {
            //检查当前是否有此记录
			
            String traNo = msg.getString("//CFX/MSG/RealHead3001/TraNo");
            msg.set("TraNo",traNo);
            String taxOrgCode = msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
            msg.set("TaxOrgCode",taxOrgCode);
            String entrustDate = msg.getString("//CFX/MSG/RealHead3001/EntrustDate");
            msg.set("EntrustDate",entrustDate);
            String workDate = msg.getString("//CFX/HEAD/WorkDate");
            msg.set("ChkDate",workDate);
            //yangyuanxu add
            String payeeBankNo = msg.getString("//CFX/MSG/TurnAccount3001/PayeeBankNo");
            msg.set("PayeeBankNo",payeeBankNo);
            try {
                //yangyuanxu update
            	String sql = "select count(*) from RealtimePayment where TraNo='" + traNo + "'"+
						" and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + entrustDate +"' and payeeBankNo='" + payeeBankNo + "'";
                System.out.println("实时扣税数据库 查询 is........:"+sql);
                int cnt = DBUtil.queryForInt(sql);
                if(cnt>0) {
                    //删除实时扣税
                	sql = "delete from RealtimePayment where TraNo='" + traNo + "'"+
						" and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + entrustDate +"' and payeeBankNo='" + payeeBankNo + "'";
                    DBUtil.executeUpdate(sql);
                    //删除实时扣税税种明细
                    sql = "delete from VoucherTaxType where TraNo='" + traNo + "' and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + entrustDate +"' and payeeBankNo='" + payeeBankNo + "'";
                    DBUtil.executeUpdate(sql);
                }
            } catch (SQLException e) {
				logger.error("query data error",e);
				return FAIL;
			}

            Map sumParams = new HashMap();
            sumParams.put("traNo", traNo);
            sumParams.put("entrustDate", entrustDate);
            sumParams.put("taxOrgCode", taxOrgCode);
            sumParams.put("tipsWorkDate",msg.get("//CFX/HEAD/WorkDate"));
            sumParams.put("handleType", msg.get("//CFX/MSG/TurnAccount3001/HandleType"));
			sumParams.put("payeeBankNo", msg.get("//CFX/MSG/TurnAccount3001/PayeeBankNo"));
			sumParams.put("payeeOrgCode", msg.get("//CFX/MSG/TurnAccount3001/PayeeOrgCode"));
			sumParams.put("payeeAcct", msg.get("//CFX/MSG/TurnAccount3001/PayeeAcct"));
            msg.set("PayeeAcct",msg.get("//CFX/MSG/TurnAccount3001/PayeeAcct"));
            sumParams.put("payeeName", msg.get("//CFX/MSG/TurnAccount3001/PayeeName"));
			sumParams.put("payBkCode", msg.get("//CFX/MSG/TurnAccount3001/PayBkCode"));
			sumParams.put("payOpBkCode", msg.get("//CFX/MSG/TurnAccount3001/PayOpBkCode"));
			sumParams.put("payAcct", msg.get("//CFX/MSG/Payment3001/PayAcct"));
            msg.set("AcctNo",msg.get("//CFX/MSG/Payment3001/PayAcct"));
            sumParams.put("handOrgName", msg.get("//CFX/MSG/Payment3001/HandOrgName"));
            String traAmt = (String) msg.get("//CFX/MSG/Payment3001/TraAmt");
            sumParams.put("traAmt", new Double(traAmt));
            msg.set("TraAmt",traAmt);
            sumParams.put("taxVouNo", msg.get("//CFX/MSG/Payment3001/TaxVouNo"));
			sumParams.put("TaxPayName",msg.get("//CFX/MSG/Payment3001/TaxPayName"));
			sumParams.put("protocolNo", msg.get("//CFX/MSG/Payment3001/ProtocolNo"));
			sumParams.put("Remark", msg.get("//CFX/MSG/Payment3001/Remark"));
			sumParams.put("Remark1", msg.get("//CFX/MSG/Payment3001/Remark1"));
			sumParams.put("Remark2", msg.get("//CFX/MSG/Payment3001/Remark2"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sumParams.put("chkDate", workDate);
            sumParams.put("recvTime",df.format(new Date()));
            //增加记录
            DBUtil.insert("RealtimePayment",sumParams);
            
            //保存税票明细信息
            Map detailParams =new HashMap();
            detailParams.put("traNo",traNo);
            detailParams.put("entrustDate",entrustDate);
            detailParams.put("taxOrgCode",taxOrgCode);
            //yangyuanxu add
            detailParams.put("payeeBankNo",payeeBankNo);
            detailParams.put("PackNo","0");//实时扣税填0
            int taxTypeNum = Integer.parseInt(msg.getString("//CFX/MSG/Payment3001/TaxTypeNum"));
            logger.info("税票明细数："+taxTypeNum);
            String prefix = "//CFX/MSG/Payment3001/TaxTypeList3001";
            for(int i = 1;i<=taxTypeNum;i++) {
                String projectId = msg.getString(prefix + "[" + i + "]/ProjectId");
                detailParams.put("ProjectId", new Integer(projectId));
            	detailParams.put("TaxTypeName",msg.getString(prefix + "[" + i + "]/TaxTypeName" ));
            	detailParams.put("TaxStartDate",msg.getString(prefix + "[" + i + "]/TaxStartDate" ));
            	detailParams.put("TaxEndDate",msg.getString(prefix + "[" + i + "]/TaxEndDate" ));
                String taxTypeAmt = msg.getString(prefix + "[" + i + "]/TaxTypeAmt");
                detailParams.put("TaxTypeAmt",new Double(taxTypeAmt));
                detailParams.put("bizCode", "1");
            	logger.info(detailParams);
            	DBUtil.insert("VoucherTaxType",detailParams);
            }
			return SUCCESS;
		}
}

