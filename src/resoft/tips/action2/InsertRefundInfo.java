package resoft.tips.action2;


import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.tips.util.IDUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 处理退款信息
 * 
 * @author guanyongxin
 * 
 */
public class InsertRefundInfo implements Action {

	private static final Log logger = LogFactory.getLog(PrepareMsg2202.class);

	private String ID;

	private String PayMode;// 支付方式

	private String AcctProp;// 账户性质

	private String AgentBnkCode;// 代理行行号

	private String[] ids;

	private String allnum; // 退款包总条数

	private String allamt; // 退款包总金额
	//private String finCode;//登录用户名称

	public int execute(Message msg) throws Exception {
		
		// 商行端传递过来的退款明细ID
		ID = msg.getString("ID");
		AgentBnkCode = msg.getString("AgentBnkCode");
		if (!"".equals(ID)) {
			if(ID.length()==1){
				String sq = "select PayMode from PAYORDERPACK where id = "
					+ "(select PackID from PayOrder where id ="
					+ "(select payID from PayOrderDetail where id = '" + ID
					+ "'))";

			PayMode = DBUtil.queryForString(sq);
			if ("0".equals(PayMode)) {
				AcctProp = "1";
			}
			if ("1".equals(PayMode)) {
				AcctProp = "2";
			}
			
           // 插入退款明细表信息
			String sql = "insert into RefundDetail (select " 
				+ IDUtil.getInstance().getMaxIDFromRefundDetail()
					+ ",'',SeqNo,BdgOrgCode,FUNCSBTCODE,EconSubjectCode,Amt,"
					+ AcctProp
					+ ",'0',PAYID from PayOrderDetail "
					+ " where ID='" + ID + "' and payID in (select id from PayOrder where id in(select payID from PayRequest where PayResult = '0')))";

			DBUtil.executeUpdate(sql);
			updatepayorderdetail(ID);
			}else{
			ids = ID.split("-");
			for (int i = 0; i < ids.length; i++) {
				ID = ids[i];

				String sq = "select PayMode from PAYORDERPACK where id = "
						+ "(select PackID from PayOrder where id ="
						+ "(select payID from PayOrderDetail where id = '" + ID
						+ "'))";
				PayMode = DBUtil.queryForString(sq);
				if ("0".equals(PayMode)) {
					AcctProp = "1";
				}
				if ("1".equals(PayMode)) {
					AcctProp = "2";
				}
				
               // 插入退款明细表信息
				String sql = "insert into RefundDetail (select " 
					+ IDUtil.getInstance().getMaxIDFromRefundDetail()
						+ ",'',SeqNo,BdgOrgCode,FUNCSBTCODE,EconSubjectCode,Amt,"
						+ AcctProp
						+ ",'0',PAYID from PayOrderDetail "
						+ " where ID='" + ID + "' and payID in (select id from PayOrder where id in(select payID from PayRequest where PayResult = '0')))";

				DBUtil.executeUpdate(sql);
				updatepayorderdetail(ID);
			}
			}
			// 把明细表中的数据分组、合计金额
			String sql1 = "Select ORDID,sum(amt) amt from RefundDetail group by ORDID";

			List rs = QueryUtil.queryRowSet(sql1);
			// 根据分组信息的ID插入更新数据表
			for (Iterator it = rs.iterator(); it.hasNext();) {
				Map dat = (Map) it.next();
				String supid = (String) dat.get("ORDID");
				String amt = (String) dat.get("amt");

				String sql2 = "Insert into RefundInfo (Select '"
					    +IDUtil.getInstance().getMaxIDFromRefundinfo()
					    +"','"
						+ this.getTraNo()
						+ "','"
						+ this.getentrustDate()
						+ "','', PayOrder.TraNo, PayOrderPack.entrustDate,"
						+ " PayOrder.VouNo, PayOrder.VouDate, PayOrder.PayerAcct, PayOrder.PayerName, "
						+ " PayOrder.PayeeAcct, PayOrder.TraNo, PayOrder.TraNo, PayOrderPack.entrustDate, "
						+ " PayOrder.PayeeOpnBnkNo, PayOrder.BudgetType, PayOrder.TrimSign, PayOrder.OfYear,"
						+ amt
						+ ", PayOrder.StatInfNum, '0', PayOrderPack.AgentBnkCode,'',"
						+ supid
						+ " from PayOrder, PayOrderPack"
						+ " where PayOrder.id='" + supid
						+ "' and PayOrder.packid=PayOrderPack.id)";
				DBUtil.executeUpdate(sql2);
				
			}

			// 插入支付退款批量包信息表
			Map counts = queryRefundinfo();
			allnum = (String) counts.get("allnum");
			allamt = (String) counts.get("allamt");
			String sqlbk = "insert into RefundPack (select distinct " 
				    +IDUtil.getInstance().getMaxIDFromRefundPack()
					+",FinOrgCode,TreCode,AgentBnkCode,'"
					+ this.getentrustDate()
					+ "','"
					+ this.getTraNo()
					+ "','"
					+ allnum
					+ "','"
					+ allamt
					+ "',PayoutVouType,PayMode,'0','0',''"
					+ " from PayOrderPack where AgentBnkCode='"
					+ AgentBnkCode
					+ "')";
			DBUtil.executeUpdate(sqlbk);
			updaterefund();
			updaterefunddetail();
			DBUtil.executeUpdate("update Refundinfo set RefundStatus='1' where RefundStatus = '0'");
		}

		else {
			logger.error("信息错误！");
		}
		logger.info("退款信息处理成功，等待发送退款请求......");
		return 0;
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
	 * 获得交易流水号
	 * 
	 */
	public String getTraNo() {
		String time = DateTimeUtil.getTimeByFormat("hhmmss");
		NumberFormat nf = new DecimalFormat("00");
		String traNo = time + nf.format(Math.random() * 100);
		return traNo;
	}

	/**
	 * 取得包汇总信息
	 * 
	 * @throws Exception
	 * 
	 */
	private Map queryRefundinfo() throws SQLException {
		String sql = "SELECT count(*) allnum,sum(amt) allamt FROM Refundinfo WHERE RefundStatus='0'";
		List resultList = QueryUtil.queryRowSet(sql);
		if (resultList.size() > 0) {
			return (Map) (resultList.get(0));
		} else {
			return null;
		}

	}

	/**
	 * 更新退款表所属包信息
	 * 
	 * @throws Exception
	 * 
	 */
	private void updaterefund() throws SQLException {

		String updsql = "update Refundinfo set packNo = (select distinct packNo from RefundPack where entrustDate=Refundinfo.entrustDate and AgentBnkCode=Refundinfo.AgentBnkCode) where packNo is null";
		DBUtil.executeUpdate(updsql);
		String  idsql = "update Refundinfo set packid = (select id from RefundPack where entrustDate = Refundinfo.entrustDate and AgentBnkCode = Refundinfo.AgentBnkCode )where packid is null";
		DBUtil.executeUpdate(idsql);
		
	}

	/**
	 * 更新退款明细表所属退款凭证信息ID
	 * 
	 * @throws Exception
	 * 
	 */
	private void updaterefunddetail() throws SQLException {

		String updsql = "update RefundDetail set refundid = (select distinct id from Refundinfo a where ordid = RefundDetail.ordid and RefundStatus = '0' and a.packid=packid) where refundid is null";
		DBUtil.executeUpdate(updsql);
	}
	/**
	 * 更新支付令明细信息退款状态
	 * 
	 * @throws Exception
	 * 
	 */
	private void updatepayorderdetail(String id) throws SQLException {

		String updsql = "update payorderDetail set PROCSTATUS = '4' where id = '"+id+"'";
		DBUtil.executeUpdate(updsql);
	}
//	public static void main(String args[]) {
//		InsertRefundInfo is = new InsertRefundInfo();
//		String d = is.getentrustDate();
//		String t = is.getTraNo();
//		System.out.println(d + "===" + t);
//	}

}
