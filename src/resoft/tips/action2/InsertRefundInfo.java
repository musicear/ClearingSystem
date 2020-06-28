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
 * �����˿���Ϣ
 * 
 * @author guanyongxin
 * 
 */
public class InsertRefundInfo implements Action {

	private static final Log logger = LogFactory.getLog(PrepareMsg2202.class);

	private String ID;

	private String PayMode;// ֧����ʽ

	private String AcctProp;// �˻�����

	private String AgentBnkCode;// �������к�

	private String[] ids;

	private String allnum; // �˿��������

	private String allamt; // �˿���ܽ��
	//private String finCode;//��¼�û�����

	public int execute(Message msg) throws Exception {
		
		// ���ж˴��ݹ������˿���ϸID
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
			
           // �����˿���ϸ����Ϣ
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
				
               // �����˿���ϸ����Ϣ
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
			// ����ϸ���е����ݷ��顢�ϼƽ��
			String sql1 = "Select ORDID,sum(amt) amt from RefundDetail group by ORDID";

			List rs = QueryUtil.queryRowSet(sql1);
			// ���ݷ�����Ϣ��ID����������ݱ�
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

			// ����֧���˿���������Ϣ��
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
			logger.error("��Ϣ����");
		}
		logger.info("�˿���Ϣ����ɹ����ȴ������˿�����......");
		return 0;
	}

	/**
	 * 
	 * ���ί������
	 * 
	 */
	public String getentrustDate() {
		DateFormat entrustDate = new SimpleDateFormat("yyyyMMdd");
		return entrustDate.format(new Date());

	}

	/**
	 * 
	 * ��ý�����ˮ��
	 * 
	 */
	public String getTraNo() {
		String time = DateTimeUtil.getTimeByFormat("hhmmss");
		NumberFormat nf = new DecimalFormat("00");
		String traNo = time + nf.format(Math.random() * 100);
		return traNo;
	}

	/**
	 * ȡ�ð�������Ϣ
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
	 * �����˿����������Ϣ
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
	 * �����˿���ϸ�������˿�ƾ֤��ϢID
	 * 
	 * @throws Exception
	 * 
	 */
	private void updaterefunddetail() throws SQLException {

		String updsql = "update RefundDetail set refundid = (select distinct id from Refundinfo a where ordid = RefundDetail.ordid and RefundStatus = '0' and a.packid=packid) where refundid is null";
		DBUtil.executeUpdate(updsql);
	}
	/**
	 * ����֧������ϸ��Ϣ�˿�״̬
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
