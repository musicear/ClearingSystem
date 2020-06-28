package resoft.tips.action2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 准备退款申请2202报文
 * @author chenlujia
 *
 */
public class PrepareMsg2202 implements Action {
	private static final Log logger = LogFactory.getLog(PrepareMsg2202.class);
	public int execute(Message msg) throws Exception {
		Map refundPackMap = queryRefundPack();
		if (refundPackMap == null) {      //如果不存在未发送退款申请包，则直接返回。
			return FAIL;
		}
		msg.set("//CFX/MSG/Head2202/FinOrgCode", refundPackMap.get("FinOrgCode"));
		msg.set("//CFX/MSG/Head2202/TreCode", refundPackMap.get("TreCode"));
		msg.set("//CFX/MSG/Head2202/AgentBnkCode", refundPackMap.get("AgentBnkCode"));
		//msg.set("//CFX/MSG/Head2202/OriEntrustDate", refundPackMap.get("EntrustDate"));
		msg.set("//CFX/MSG/Head2202/EntrustDate", refundPackMap.get("EntrustDate"));
		//msg.set("//CFX/MSG/Head2202/PackNo", getPackNo());
		msg.set("//CFX/MSG/Head2202/PackNo", refundPackMap.get("PackNo"));
		msg.set("//CFX/MSG/Head2202/PayoutVouType", refundPackMap.get("PayoutVouType"));
		msg.set("//CFX/MSG/Head2202/PayMode", refundPackMap.get("PayMode"));
		int allNum = 0;
		double allAmt = 0.00;
		DecimalFormat dt = new DecimalFormat("0.00");
		List refundBills = queryRefundBills(refundPackMap);
		for (int i=0; i<refundBills.size(); i++) {
			Map billMap = (Map)(refundBills.get(i));
			logger.info("TraNo is: "+ billMap.get("TraNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/TraNo" , billMap.get("TraNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/VouNo" , billMap.get("VouNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/VouDate" , billMap.get("VouDate"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriTraNo" , billMap.get("OriTraNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriEntrustDate", billMap.get("OriEntrustDate"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriVouNo" , billMap.get("OriVouNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriVouDate" , billMap.get("OriVouDate"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriPayerAcct" , billMap.get("OriPayerAcct"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriPayerName" , billMap.get("OriPayerName"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriPayeeAcct" , billMap.get("OriPayeeAcct"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OriPayeeName" , billMap.get("OriPayeeName"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/PayDictateNo" , billMap.get("PayDictateNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/PayMsgNo" , billMap.get("PayMsgNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/PayEntrustDate" , billMap.get("PayEntrustDate"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/PaySndBnkNo" , billMap.get("PaySndBnkNo"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/BudgetType" , billMap.get("BudgetType"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/TrimSign" , billMap.get("TrimSign"));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/OfYear" , billMap.get("OfYear"));
			int billNum=0;
			double billAmt=0.00;
			List refundDetailList = queryRefundDetails(billMap);
			for (int j=0; j<refundDetailList.size(); j++) {
				Map detailMap = (Map)(refundDetailList.get(j));
				msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/SeqNo" , detailMap.get("SeqNo"));
				msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/BdgOrgCode" , detailMap.get("BdgOrgCode"));
				msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/FuncSbtCode" , detailMap.get("FuncSbtCode"));
				if (detailMap.containsKey("EcnomicSubjectCode") && (detailMap.get("EcnomicSubjectCode") != null) && (!"".equals(detailMap.get("EcnomicSubjectCode")))) {
					msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/EcnomicSubjectCode" , detailMap.get("EcnomicSubjectCode"));
				}
				//double amt = Double.parseDouble(detailMap.get("Amt").toString());
				BigDecimal amt=new BigDecimal(Double.parseDouble(detailMap.get("Amt").toString()));
				amt= amt.setScale(2, BigDecimal.ROUND_HALF_UP);
				logger.info("amt is: " + amt);
				billAmt = add(billAmt,amt.doubleValue());
				billNum ++;
				msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/Amt" , dt.format(amt.doubleValue()));
				msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Detail2202[" + (j+1) + "]/AcctProp" , detailMap.get("AcctProp"));
				//updateBalance(detailMap);
			}

			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/Amt" , dt.format(billAmt));
			msg.set("//CFX/MSG/Bill2202[" + (i+1) + "]/StatInfNum" , String.valueOf(billNum));
			allNum ++;
			allAmt =add(allAmt,billAmt);
			
		}
		msg.set("//CFX/MSG/Head2202/AllNum", String.valueOf(allNum));
		msg.set("//CFX/MSG/Head2202/AllAmt", dt.format(allAmt));
		updateSendStatus(refundPackMap, "3");
		return 0;
	}
	
	/**
	 * 更新额度余额
	 * @throws Exception 
	 *
	 */
	/*
	private void updateBalance(Map detailMap) throws Exception {
		//TODO:更新额度余额
		String bdgOrgCode = (String)(detailMap.get("BdgOrgCode"));
		String funcBdgSbtCode = (String)(detailMap.get("FuncBdgSbtCode"));
		String sql = "SELECT * FROM PayQuotaBalance WHERE BdgOrgCode='" + bdgOrgCode + "' AND FUNCSBTCODE='" + funcBdgSbtCode + "'";
		List result = QueryUtil.queryRowSet(sql);
		if (result.size() == 0) {
			logger.error("不存在额度余额信息: 预算单位代码:" + bdgOrgCode + ", 功能类科目代码: " + funcBdgSbtCode);
			return;
		}
		Map balanceMap = (Map)(result.get(0));
		double balanceAmt = Double.valueOf((String)(balanceMap.get("BalanceAmt"))).doubleValue();
		double deltaAmt = Double.valueOf((String)(detailMap.get("Amt"))).doubleValue();
		double newBalance = balanceAmt + deltaAmt;
		sql = "UPDATE PayQuotaBalance SET BalanceAmt=" + CurrencyUtil.getCurrencyFormat(String.valueOf(newBalance)) + " WHERE BdgOrgCode='" + bdgOrgCode + "' AND FUNCSBTCODE='" + funcBdgSbtCode + "'";
		DBUtil.executeUpdate(sql);
		sql = "UPDATE RefundDetail SET BalanceStatus='1' WHERE  SeqNo=" + detailMap.get("SeqNo") ;
		DBUtil.executeUpdate(sql);
	}
	 */

	/**
	 * 更新退款申请包的发送状态
	 * @param refundPackMap
	 */
	private void updateSendStatus(Map refundPackMap, String status) {
		String entrustDate=(String)(refundPackMap.get("entrustDate"));
		String packNo = (String)(refundPackMap.get("packNo"));
		String sql = "UPDATE RefundPack SET ProcStatus='" + status + "' WHERE entrustDate='" + entrustDate + "' AND packNo='" + packNo + "'";
		DBUtil.executeUpdate(sql);
		String sql_info = "UPDATE RefundInfo SET ProcStatus='" + status + "' WHERE entrustDate='" + entrustDate + "' AND packNo='" + packNo + "'";
		DBUtil.executeUpdate(sql_info);
	}


	/**
	 * 查询退款明细信息表
	 * 
	 */
	private List queryRefundDetails(Map billMap) throws SQLException {
		String entrustDate=(String)(billMap.get("entrustDate"));
		String ID = (String)(billMap.get("ID"));
		String sql="SELECT * FROM RefundDetail WHERE  PayId=" + ID ;
		logger.info("detail_sql is: "+sql);
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	/**
	 * 查询退款交易信息表
	 * @param refundPackMap
	 * @return
	 * @throws SQLException 
	 */
	private List queryRefundBills(Map refundPackMap) throws SQLException {
		String entrustDate=(String)(refundPackMap.get("entrustDate"));
		String packId = (String)(refundPackMap.get("Id"));
		
		String sql = "SELECT * FROM RefundInfo WHERE entrustDate='" + entrustDate + "' AND packId='" + packId + "'";
		logger.info("bill_sql is: "+sql);
		List result = QueryUtil.queryRowSet(sql);
		return result;
	}

	/**
	 * 查询未发送退款包信息，如果存在记录，则返回第一条，如不存在，返回null
	 * @return
	 * @throws SQLException
	 */
	private Map queryRefundPack() throws SQLException {
		String sql="SELECT * FROM RefundPack WHERE ProcStatus='2'";
		List resultList = QueryUtil.queryRowSet(sql);
		if (resultList.size() > 0) {
			return (Map)(resultList.get(0));
		} else {
			return null;
		}
		
	}
	/**
	 * 
	 * 获得委托日期
	 * 
	 */
	public String getentrustDate() {
		DateFormat entrustDate = new SimpleDateFormat("yyyyMMdd");
		return entrustDate.format(new Date());

	}
	/**
	 * 
	 * 获得包流水号
	 * 
	 */
	public String getPackNo() {
		String time = DateTimeUtil.getTimeByFormat("hhmmss");
		NumberFormat nf = new DecimalFormat("00");
		String PackNo = time + nf.format(Math.random() * 100);
		return PackNo;
	}
	public static double add(double v1, double v2) {  
		  BigDecimal b1 = new BigDecimal(Double.toString(v1));  
		  BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		  
		  return b1.add(b2).doubleValue();  
		}  
	
}
