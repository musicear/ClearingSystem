package resoft.tips.action2;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.IDUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 保存支付令到数据库
 * @author chenlujia
 *
 */
public class ProcessPayOrder5111 implements Action {
    private static final Log logger = LogFactory.getLog(ProcessPayOrder5111.class);
	public int execute(Message msg) throws Exception {
		int result=0;
		IDUtil util=IDUtil.getInstance();
		Long payOrderPackID=new Long(util.getMaxIDFromPayOrderPack());
		Long payOrderID=null;
		String filePath = msg.getString("批量文件");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filePath), "GBK");
		String localName = "", parentLocalName = "";
		String finOrgCode = "", entrustDate = "", packNo = "", traNo = "";
		// 包信息
		Map packParams = new HashMap();
		// 支付令信息
		Map billParams = new HashMap();
		// 支付令明细信息
		Map detailParams = new HashMap();
		Stack tagStack = new Stack();
		int billCount = 0, detailCount = 0;
		for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader
				.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				localName = reader.getLocalName();

				tagStack.push(localName);
				if (localName.equals("BatchHead5111")) {
					packParams.clear();
					billCount = 0;
				} else if (localName.equals("Bill5111")) {
					billParams.clear();
					payOrderID=new Long(util.getMaxIDFromPayOrder());
					billCount++;
					detailCount = 0;
				} else if (localName.equals("Detail5111")) {
					detailParams.clear();
					detailCount++;
				}
				if (tagStack.size() > 1) {
					parentLocalName = (String) (tagStack
							.get(tagStack.size() - 2));
				} else {
					parentLocalName = "";
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				String text = reader.getText().trim();
				if (!text.equals("")) {
					if (parentLocalName.equals("BatchHead5111")) {
						packParams.put(localName, text);
						if (localName.equals("FinOrgCode")) {
							finOrgCode = text;
						} else if (localName.equals("EntrustDate")) {
							entrustDate = text;
						} else if (localName.equals("PackNo")) {
							packNo = text;
						} else if (localName.equals("AllNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("AllAmt")) {
							packParams.put(localName, Double.valueOf(text));
						}
					} else if (parentLocalName.equals("Bill5111")) {
						billParams.put(localName, text);
						if (localName.equals("TraNo")) {
							traNo = text;
						} else if (localName.equals("Amt")) {
							billParams.put(localName, Double.valueOf(text));
						} else if (localName.equals("StatInfNum")) {
							billParams.put(localName, Integer.valueOf(text));
						}
					} else if (parentLocalName.equals("Detail5111")) {
						detailParams.put(localName, text);
						if (localName.equals("SeqNo")) {
							detailParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("Amt")) {
							detailParams.put(localName, Double.valueOf(text));
						} else if (localName.equals("EcnomicSubjectCode")) {
							detailParams.remove(localName);
							detailParams.put("EconSubjectCode", text);
						}
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				tagStack.pop();
				if (localName.equals("Detail5111")) {
					Long detailID=new Long(util.getMaxIDFromPayOrderDetail());
					detailParams.put("ID",detailID);
					detailParams.put("payID",payOrderID);
					detailParams.put("FinOrgCode", finOrgCode);
					detailParams.put("entrustDate", entrustDate);
					detailParams.put("packNo", packNo);
					detailParams.put("TraNo", traNo);
					detailParams.put("procstatus", "0");
					DBUtil.insert("PayOrderDetail", detailParams);
				} else if (localName.equals("Bill5111")) {
					billParams.put("ID",payOrderID);
					billParams.put("PackID",payOrderPackID);
					billParams.put("procstatus", "0");
					DBUtil.insert("PayOrder", billParams);
				} else if (localName.equals("BatchHead5111")) {
					// TODO: set status.
					packParams.put("ID",payOrderPackID);
					billParams.put("procstatus", "0");
					DBUtil.insert("PayOrderPack", packParams);
				} 
				break;
			}
		}
		logger.info("支付令批量包保存成功.packNo=" + packNo + ";entrustDate="
				+ entrustDate + ";FinOrgCode=" + finOrgCode);
		return result;
	}

	
	/**
	 * 检查科目额度余额，如果余额充足，则扣减额度，否则标志“扣款失败”
	 * @param detailParams
	 * @throws SQLException
	 */
	/*
	private int updateBalance(Map detailParams) throws SQLException {
		String bdgOrgCode = (String)(detailParams.get("BdgOrgCode"));
		String funcSbtCode = (String)(detailParams.get("FuncSbtCode"));
		String finOrgCode = (String)(detailParams.get("FinOrgCode"));
		String entrustDate = (String)(detailParams.get("entrustDate"));
		String packNo = (String)(detailParams.get("packNo"));
		String traNo = (String)(detailParams.get("TraNo"));
		Integer seqNo = (Integer)(detailParams.get("SeqNo"));
		
		String whereClause = " FinOrgCode='" + finOrgCode + "' AND entrustDate='" + entrustDate + "' AND packNo='" + packNo + "' AND TraNo='" +traNo + "' AND SeqNo=" + seqNo + " ";
		String sql = "SELECT * FROM PayQuotaBalance WHERE BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'";
		List result = QueryUtil.queryRowSet(sql);
		if (result.size() == 0) {     // 如果不存在该额度科目，直接标志“拨款失败”
			detailParams.put("PayStatus", "3");
			String addWord="不存在额度科目";
			sql = "UPDATE PayOrderDetail SET  AddWord='" +addWord +"' WHERE " + whereClause;
			DBUtil.executeUpdate(sql);
			return -1;
		} else {
			Map balanceMap = (Map)(result.get(0));
			double balance = ((Double)(balanceMap.get("BalanceAmt"))).doubleValue();
			double dialAmt = ((Double)(detailParams.get("Amt"))).doubleValue();
			double newBalance = balance - dialAmt;
			if (newBalance<0) {
				String addWord="当前科目额度余额不足";
				sql = "UPDATE PayOrderDetail SET  AddWord='" + addWord + "' WHERE " + whereClause;
				DBUtil.executeUpdate(sql);
				return -1;
			} else {
				String sqlList[] = new String[2];
				sqlList[0] = "UPDATE PayOrderDetail SET PayStatus='1' WHERE " + whereClause;
				sqlList[1] = "UPDATE PayQuotaBalance SET BalanceAmt=" + newBalance + " WHERE BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'";
				DBUtil.executeUpdate(sqlList);
			}
		}
		return 0;
		
	}*/
}
