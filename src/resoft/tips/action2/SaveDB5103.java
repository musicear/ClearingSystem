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
import resoft.tips.action.AbstractSyncAction;
import resoft.xlink.core.Message;

/**
 * <p> * 保存批量授权支付额度数据库 * </p>
 * User: chenlujia 
 * Date: 2008-8-22 
 */
public class SaveDB5103 extends AbstractSyncAction {
    private static final Log logger = LogFactory.getLog(SaveDB5103.class);

    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("批量文件");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");
        String localName = "", parentLocalName="";
        String treCode = "", entrustDate = "", packNo= "", traNo= "";
        //包信息
        Map packParams = new HashMap();
        //额度信息
        Map quotaParams = new HashMap();
        //额度明细信息
        Map detailParams = new HashMap();
        Stack tagStack = new Stack();
        int billCount=0, detailCount=0;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();

                    tagStack.push(localName);
                    if (localName.equals("BatchHead5103")) {
                    	packParams.clear();
                    	billCount=0;
                    } else if (localName.equals("Bill5103")) {
                    	quotaParams.clear();
                    	billCount ++;
                    	detailCount=0;
                    } else if (localName.equals("Detail5103")) {
                    	detailParams.clear();
                    	detailCount ++;
                    }
                    if (tagStack.size() > 1) {
                    	parentLocalName=(String)(tagStack.get(tagStack.size()-2));
                    } else {
                    	parentLocalName="";
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                    	if (parentLocalName.equals("BatchHead5103")) {
                    		packParams.put(localName, text);
                    		if (localName.equals("TreCode")) {
                    			treCode = text;
                            } else if (localName.equals("EntrustDate")) {
                                entrustDate = text;
                            } else if (localName.equals("PackNo")) {
                                packNo = text;
                            } else if (localName.equals("AllNum")) {
                            	packParams.put(localName, Integer.valueOf(text));
                            } else if (localName.equals("AllAmt")) {
                            	packParams.put(localName, Double.valueOf(text));
                            }
                    	} else if (parentLocalName.equals("Bill5103")) {
                    		quotaParams.put(localName, text); 
                    		if(localName.equals("TraNo")) {
                    			traNo = text;
                    		} else if (localName.equals("SumAmt")) {
                    			quotaParams.put(localName, Double.valueOf(text));
                    		} else if (localName.equals("StatInfNum")) {
                    			quotaParams.put(localName, Integer.valueOf(text));
                    		} else if (localName.equals("RransactOrg")) {
//                    			quotaParams.remove("RransactOrg");
                    			//quotaParams.put("TransactOrg", text);
                    			quotaParams.put("RransactOrg", text);
                    		}
                    	} else if (parentLocalName.equals("Detail5103")) {
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
                    if (localName.equals("Detail5103")) {
                    	detailParams.put("TreCode", treCode);
                    	detailParams.put("entrustDate", entrustDate);
                    	detailParams.put("packNo", packNo);
                    	detailParams.put("TraNo", traNo);
                    	DBUtil.insert("PayQuotaDetail", detailParams);
                    	//updateBalance(detailParams, (String)(quotaParams.get("BdgOrgCode")));
                    } else if (localName.equals("Bill5103")) {
                    	quotaParams.put("TreCode", treCode);
                    	quotaParams.put("entrustDate", entrustDate);
                    	quotaParams.put("packNo", packNo);
                    	DBUtil.insert("PayQuota", quotaParams);

                    } else if (localName.equals("BatchHead5103")) {
                    	packParams.put("payType", "2");
                    	packParams.put("checkStatus", "0");
                    	packParams.put("procFlag", "0");
                    	quotaParams.put("payType", "2");
                    	DBUtil.insert("QuotaPack", packParams);
                    	logger.info("额度包保存成功.packNo=" + packNo + ";entrustDate=" + entrustDate + ";TreCode=" + treCode);
                    	
                    } else if (localName.equals("MSG")) {
//                    	全部明细处理完毕，设置批量包处理状态为“接收完毕”
                    	String sql = "update QuotaPack set procFlag='1' " +
                        " where TreCode='" + treCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payType='2'";
                    	DBUtil.executeUpdate(sql);
                    }
                    break;
            }
        }

        return SUCCESS;
    }

    /**
     * 更新余额信息
     * @param detailParams
     * @throws SQLException 
     */
//	private void updateBalance(Map detailParams, String BdgOrgCode) throws SQLException {
//		String sql = "select BalanceAmt from PayQuotaBalance where BdgOrgCode='"
//				+ BdgOrgCode
//				+ "' and FuncBdgSbtCode='"
//				+ (String) (detailParams.get("FuncBdgSbtCode")) + "' ";
//		List result = QueryUtil.queryRowSet(sql);
//		if (result.size() == 0) {
//			sql = "insert into PayQuotaBalance (BdgOrgCode, FuncBdgSbtCode," +
//					" EconSubjectCode, BalanceAmt, QuotaType) values( '" + 
//					BdgOrgCode + "', '" + detailParams.get("FuncBdgSbtCode") + 
//					"', '" + ((detailParams.containsKey("EconSubjectCode"))? detailParams.get("EconSubjectCode") : "") +
//					"', '" + detailParams.get("Amt") + "', '2')";
//		} else {
//			double oriAmt = Double.parseDouble((String)((Map)(result.get(0))).get("BalanceAmt"));
//			double deltaAmt = ((Double)(detailParams.get("Amt"))).doubleValue();
//			double sumAmt = oriAmt + deltaAmt;
//			sql = "update PayQuotaBalance set BalanceAmt="
//					+ Double.toString(sumAmt) + " where BdgOrgCode='"
//					+ BdgOrgCode
//					+ "' and FuncBdgSbtCode='"
//					+ (String) (detailParams.get("FuncBdgSbtCode")) + "' ";
//		}
//		DBUtil.executeUpdate(sql);
//		
//	}
}
