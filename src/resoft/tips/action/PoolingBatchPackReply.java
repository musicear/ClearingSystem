package resoft.tips.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.CurrencyUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>轮询批量包以返回回执，一次只读取一条批量信息，以避免启动子流程</p>
 * Author: liguoyin
 * Date: 2007-6-12
 * Time: 18:20:55
 */
public class PoolingBatchPackReply implements Action {
	private static final Log logger = LogFactory.getLog(PoolingBatchPackReply.class);
    public int execute(Message msg) throws Exception {
        List rowSet = QueryUtil.queryRowSet("select entrustdate,payeeBankNo,payBkCode,taxOrgCode,packNo,allNum,allAmt from BatchPackage where procFlag='3'");
        logger.info("select entrustdate,payeeBankNo,payBkCode,taxOrgCode,packNo,allNum,allAmt from BatchPackage where procFlag='3'");
        if (rowSet.size() > 0) {
            Map row = (Map) rowSet.get(0);
            String payeeBankNo = (String)row.get("payeeBankNo");
            msg.set("payeeBankNo", payeeBankNo);
            System.out.println("payeeBankNo is:"+msg.get("payeeBankNo"));
            
            msg.set("//CFX/MSG/BatchHead2102/PayBkCode", row.get("payBkCode"));
            msg.set("//CFX/MSG/BatchHead2102/EntrustDate", DateTimeUtil.getDateString());
            msg.set("//CFX/MSG/BatchHead2102/PackNo", getReturnPackNo());
            String taxOrgCode = (String) row.get("taxOrgCode");
            msg.set("TaxOrgCode", taxOrgCode);
            msg.set("//CFX/MSG/BatchHead2102/OriTaxOrgCode", taxOrgCode);
            String entrustDate = (String) row.get("entrustdate"); 
            msg.set("EntrustDate", entrustDate); 
            msg.set("//CFX/MSG/BatchHead2102/OriEntrustDate", entrustDate);
            String packNo = (String) row.get("packNo");
            msg.set("PackNo", packNo);
            msg.set("//CFX/MSG/BatchHead2102/OriPackNo", packNo);
            msg.set("//CFX/MSG/BatchHead2102/AllNum", row.get("allNum"));
            String allAmt = (String) row.get("allAmt");
            msg.set("//CFX/MSG/BatchHead2102/AllAmt", CurrencyUtil.getCurrencyFormat(allAmt));
            //得到成功笔数、成功金额
            msg.set("//CFX/MSG/BatchHead2102/SuccNum", getSuccNum(taxOrgCode, entrustDate, packNo,payeeBankNo));
            String succAmt = getSuccAmt(taxOrgCode, entrustDate, packNo,payeeBankNo);
            msg.set("//CFX/MSG/BatchHead2102/SuccAmt", CurrencyUtil.getCurrencyFormat(succAmt));
            
            msg.set("//CFX/MSG/BatchHead2102/Result", "90000");
            msg.set("//CFX/MSG/BatchHead2102/AddWord", "处理成功");
            //msg.set("msgRef", (String)row.get("msgRef"));
            return SUCCESS;
        }
        return FAIL;
    }

    /**
     * 得到成功笔数
     */
    private String getSuccNum(String taxOrgCode, String entrustDate, String packNo,String payeeBankNo) throws SQLException {
        String sql = "select count(*) from BatchPackDetail " +
                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='"+payeeBankNo+"'";
        return DBUtil.queryForString(sql);
    }

    /**
     * 得到成功金额
     */
    private String getSuccAmt(String taxOrgCode, String entrustDate, String packNo,String payeeBankNo) throws SQLException {
        String sql = "select sum(traAmt) from BatchPackDetail " +
                " where result='90000' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='"+payeeBankNo+"' ";
        String str = DBUtil.queryForString(sql);
        if (str == null || str.equals("") || str.equals(" ") ) {
            return "0.00";
        } else {
            double amt = Double.parseDouble(str);
            NumberFormat nf = new DecimalFormat("0.00");
            return nf.format(amt);
        }
    }

    /**
     * 生成回执包流水号。生成8位流水号，规则为hhmmss加2位随机数
     */
    private String getReturnPackNo() {
        String packNo = DateTimeUtil.getTimeByFormat("hhmmss");
        NumberFormat nf = new DecimalFormat("00");
        packNo += nf.format(Math.random() * 100);
        return packNo;
    }
}
