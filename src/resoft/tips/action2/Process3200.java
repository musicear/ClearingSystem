package resoft.tips.action2;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 处理3200包核对报文，执行5102、5103、2200、2201、2202、5111报文包的对帐操作
 * @author zhaobo
 *
 */
public class Process3200 implements Action {
	private static final Log logger = LogFactory.getLog(Process3200.class);

	public int execute(Message msg) throws Exception {
		String filePath = msg.getString("批量文件");
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(
				new FileInputStream(filePath), "GBK");
		String localName = "", parentLocalName = "";

		// 包信息
		Map packParams = new HashMap();
		// 单笔核对信息
		Map billParams = new HashMap();

		Stack tagStack = new Stack();
		// int billCount = 0, detailCount = 0;
		for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader
				.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				localName = reader.getLocalName();
				tagStack.push(localName);
				if (localName.equals("BatchHead3201")) {
					packParams.clear();
				} else if (localName.equals("CompDeduct3201")) {
					billParams.clear();
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
					if (parentLocalName.equals("BatchHead3201")) {
						if (localName.equals("ChkDate")) {
							packParams.put(localName, text);
						} else if (localName.equals("PackNo")) {
							packParams.put(localName, text);
						} else if (localName.equals("BillOrg")) {
							packParams.put(localName, text);
						} else if (localName.equals("OrgType")) {
							packParams.put(localName, text);
						} else if (localName.equals("AllSendNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("ChildPackNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("CurPackNo")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("CurPackNum")) {
							packParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("CurPackAmt")) {
							packParams.put(localName, Double.valueOf(text));
						}

					} else if (parentLocalName.equals("CompDeduct3201")) {
						if (localName.equals("AllNum")) {
							billParams.put(localName, Integer.valueOf(text));
						} else if (localName.equals("AllAmt")) {
							billParams.put(localName, Double.valueOf(text));
						} else if (localName.equals("OriMsgNo")) {
							billParams.put(localName, text);
						} else if (localName.equals("OriSendOrgCode")) {
							billParams.put(localName, text);
						} else if (localName.equals("OriEntrustDate")) {
							billParams.put(localName, text);
						} else if (localName.equals("OriPackNo")) {
							billParams.put(localName, text);
						}
					}
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				localName = reader.getLocalName();
				tagStack.pop();
				if (localName.equals("CompDeduct3201")) {
					validatePack(billParams);
				} else if (localName.equals("BatchHead3201")) {
					printInfo(packParams);
				}
				break;
			}
		}
		return SUCCESS;
	}

	/**
	 * 打印收到对帐包信息
	 * @param packParams
	 */
	private void printInfo(Map packParams) {
		StringBuffer sb = new StringBuffer();
		sb.append("收到3200信息核对包: 核对日期: ").append(packParams.get("ChkDate"));
		logger.info(sb.toString());
		
	}

	/**
	 * 对具体包进行对帐校验。
	 * 此处仅校验包的总金额和总笔数
	 * @param billParams
	 * @throws SQLException
	 */
	private void validatePack(Map billParams) throws SQLException {
		String oriMsgNo = (String)(billParams.get("OriMsgNo"));
		if (oriMsgNo.equals("5102") || oriMsgNo.equals("5103")) {
			String payType;
			if (oriMsgNo.equals("5102")) {
				payType="1";
			} else {
				payType="2";
			}
			String whereClause = "TreCode='" + billParams.get("OriSendOrgCode") +
			"' AND entrustDate='" + billParams.get("OriEntrustDate") + "' AND packNo='" + billParams.get("OriPackNo") +
			"' AND payType='" + payType + "' ";
			String sql = "SELECT * FROM QuotaPack WHERE " + whereClause;
			List result = QueryUtil.queryRowSet(sql);
			if (result.size() == 0) {
				String info = "对帐失败：系统未接收到包" + MapToString(billParams);
				logger.info(info);
			} else {
				Map tarMap = (Map)(result.get(0));
				int tarNum = Integer.parseInt(tarMap.get("AllNum").toString());
				double tarAmt = Double.parseDouble(tarMap.get("AllAmt").toString());	
				int srcNum = Integer.parseInt(billParams.get("AllNum").toString());
				double srcAmt = Double.parseDouble(billParams.get("AllAmt").toString());
				if ((tarNum != srcNum) || (tarAmt != srcAmt)) {
					sql = "UPDATE QuotaPack SET checkStatus='2', chkDate='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info = "对帐失败: 包" + MapToString(billParams)
							+ " 系统笔数: " + tarNum + " 对帐笔数: " + srcNum
							+ " 系统总金额: " + tarAmt + " 对帐总金额: " + srcAmt;
					logger.info(info);
				} else {
					sql = "UPDATE QuotaPack SET checkStatus='1', chkDate='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info ="对帐成功. 包：" + MapToString(billParams);
					logger.info(info);
				}
			}
		} else if (oriMsgNo.equals("2200")) {
			String whereClause = "FinOrgCode='" + billParams.get("OriSendOrgCode") +
			"' AND returnEntrustDate='" + billParams.get("OriEntrustDate") + "' AND returnPackNo='" + billParams.get("OriPackNo") +
			"' ";
			String sql = "SELECT * FROM PayOrderPack WHERE " + whereClause;
			List result = QueryUtil.queryRowSet(sql);
			if (result.size() == 0) {
				String info = "对帐失败：系统未接收到包" + MapToString(billParams);
				logger.info(info);
			} else {
				Map tarMap = (Map)(result.get(0));
				int tarNum = Integer.parseInt(tarMap.get("returnNum").toString());
				double tarAmt = Double.parseDouble(tarMap.get("returnAmt").toString());	
				int srcNum = Integer.parseInt(billParams.get("AllNum").toString());
				double srcAmt = Double.parseDouble(billParams.get("AllAmt").toString());
				if ((tarNum != srcNum) || (tarAmt != srcAmt)) {
					sql = "UPDATE PayOrderPack SET returnCheckStatus='2', returnChkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info = "对帐失败: 包" + MapToString(billParams)
							+ " 系统笔数: " + tarNum + " 对帐笔数: " + srcNum
							+ " 系统总金额: " + tarAmt + " 对帐总金额: " + srcAmt;
					logger.info(info);
				} else {
					sql = "UPDATE PayOrderPack SET returnCheckStatus='1', returnChkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info ="对帐成功. 包：" + MapToString(billParams);
					logger.info(info);
				}
			}
		} else if (oriMsgNo.equals("2201")) {
			String whereClause = "AgentBnkCode='" + billParams.get("OriSendOrgCode") +
			"' AND entrustDate='" + billParams.get("OriEntrustDate") + "' AND REQUESTPACKNO ='" + billParams.get("OriPackNo") +
			"' ";
			String sql = "SELECT * FROM PayRequestPack WHERE " + whereClause;
			List result = QueryUtil.queryRowSet(sql);
			if (result.size() == 0) {
				String info = "对帐失败：系统未接收到包" + MapToString(billParams);
				logger.info(info);
			} else {
				Map tarMap = (Map)(result.get(0));
				int tarNum = Integer.parseInt(tarMap.get("AllNum").toString());
				double tarAmt = Double.parseDouble(tarMap.get("AllAmt").toString());	
				int srcNum = Integer.parseInt(billParams.get("AllNum").toString());
				double srcAmt = Double.parseDouble(billParams.get("AllAmt").toString());
				if ((tarNum != srcNum) || (tarAmt != srcAmt)) {
					sql = "UPDATE PayRequestPack SET checkStatus='2', r_chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info = "对帐失败: 包" + MapToString(billParams)
							+ " 系统笔数: " + tarNum + " 对帐笔数: " + srcNum
							+ " 系统总金额: " + tarAmt + " 对帐总金额: " + srcAmt;
					logger.info(info);
				} else {
					sql = "UPDATE PayRequestPack SET checkStatus='1', r_chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info ="对帐成功. 包：" + MapToString(billParams);
					logger.info(info);
				}
			} 
		} else if (oriMsgNo.equals("2202")) {
			String whereClause = "AgentBnkCode='" + billParams.get("OriSendOrgCode") +
			"' AND entrustDate='" + billParams.get("OriEntrustDate") + "' AND packNo='" + billParams.get("OriPackNo") +
			"' AND SendStatus='1' ";
			String sql = "SELECT * FROM RefundPack WHERE " + whereClause;
			List result = QueryUtil.queryRowSet(sql);
			if (result.size() == 0) {
				String info = "对帐失败：系统包" + MapToString(billParams);
				logger.info(info);
			} else {
				Map tarMap = (Map)(result.get(0));
				int tarNum = Integer.parseInt(tarMap.get("AllNum").toString());
				double tarAmt = Double.parseDouble(tarMap.get("AllAmt").toString());	
				int srcNum = Integer.parseInt(billParams.get("AllNum").toString());
				double srcAmt = Double.parseDouble(billParams.get("AllAmt").toString());
				if ((tarNum != srcNum) || (tarAmt != srcAmt)) {
					sql = "UPDATE RefundPack SET checkStatus='2', chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info = "对帐失败: 包" + MapToString(billParams)
							+ " 系统笔数: " + tarNum + " 对帐笔数: " + srcNum
							+ " 系统总金额: " + tarAmt + " 对帐总金额: " + srcAmt;
					logger.info(info);
				} else {
					sql = "UPDATE RefundPack SET checkStatus='1', chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info ="对帐成功. 包：" + MapToString(billParams);
					logger.info(info);
				}
			}
		} else if (oriMsgNo.equals("5111")) {
			String whereClause = "FinOrgCode='" + billParams.get("OriSendOrgCode") +
			"' AND entrustDate='" + billParams.get("OriEntrustDate") + "' AND packNo='" + billParams.get("OriPackNo") +
			"' ";
			String sql = "SELECT * FROM PayOrderPack WHERE " + whereClause;
			List result = QueryUtil.queryRowSet(sql);
			if (result.size() == 0) {
				String info = "对帐失败：系统未接收到包" + MapToString(billParams);
				logger.info(info);
			} else {
				Map tarMap = (Map)(result.get(0));
				int tarNum = Integer.parseInt(tarMap.get("AllNum").toString());
				double tarAmt = Double.parseDouble(tarMap.get("AllAmt").toString());	
				int srcNum = Integer.parseInt(billParams.get("AllNum").toString());
				double srcAmt = Double.parseDouble(billParams.get("AllAmt").toString());
				if ((tarNum != srcNum) || (tarAmt != srcAmt)) {
					sql = "UPDATE PayOrderPack SET checkStatus='2', chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info = "对帐失败: 包" + MapToString(billParams)
							+ " 系统笔数: " + tarNum + " 对帐笔数: " + srcNum
							+ " 系统总金额: " + tarAmt + " 对帐总金额: " + srcAmt;
					logger.info(info);
				} else {
					sql = "UPDATE PayOrderPack SET checkStatus='1', chkTime='" + DateTimeUtil.getTimeByFormat("yyyyMMddhhmmss") + "' WHERE " + whereClause;
					DBUtil.executeUpdate(sql);
					String info ="对帐成功. 包：" + MapToString(billParams);
					logger.info(info);
				}
			} 
		} else {
			logger.info("对帐失败：未定义的包代码:" + oriMsgNo);
		}
	}

	/**
	 * 将Map对象中的元素转换为字符串
	 * @param srcMap
	 * @return
	 */
	private String MapToString(Map srcMap) {
		StringBuffer sb = new StringBuffer();
		Set keys = srcMap.keySet();
		Iterator keyItor = keys.iterator();
		while (keyItor.hasNext()) {
			String keyStr = (String)(keyItor.next());
			String valueStr = srcMap.get(keyStr).toString();
			sb.append("[").append(keyStr).append("]: ");
			sb.append(valueStr).append("; ");
		}
		return sb.toString();
	}

}
