package resoft.tips.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.tips.util.CurrencyUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>返回批量包明细</p>
 * Author: liguoyin
 * Date: 2007-6-13
 * Time: 16:53:05
 */
public class ReturnPackDetails implements Action {
    private static final Log logger = LogFactory.getLog(ReturnPackDetails.class);
    public int execute(Message msg) throws Exception {
    	String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String payeeBankNo = msg.getString("payeeBankNo");
        String sql = "select * from BatchPackDetail " +
                " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='"+payeeBankNo+"' ";
        logger.info(sql);
        List rowSet = QueryUtil.queryRowSet(sql);
        for (int i = 0; i < rowSet.size(); i++) {
            Map row = (Map) rowSet.get(i);
            String prefix = "//CFX/MSG/BatchReturn2102[" + (i + 1) + "]/";
            msg.set(prefix + "OriTraNo",row.get("traNo"));
            String traAmt = (String) row.get("traAmt");
            msg.set(prefix + "TraAmt", CurrencyUtil.getCurrencyFormat(traAmt));
            msg.set(prefix + "TaxVouNo",row.get("taxVouNo"));
            msg.set(prefix + "TaxDate",row.get("taxDate"));
            msg.set(prefix + "Result",row.get("result"));
            msg.set(prefix + "AddWord",row.get("addWord"));

        }
        return SUCCESS;
    }
}
