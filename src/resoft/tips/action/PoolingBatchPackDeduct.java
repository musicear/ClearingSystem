package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>轮询批量包以发起扣款，一次只读取一条批量信息，以避免启动子流程</p>
 * Author: liguoyin
 * Date: 2007-6-12
 * Time: 18:20:55
 */
public class PoolingBatchPackDeduct implements Action {
    public int execute(Message msg) throws Exception {
        List rowSet = QueryUtil.queryRowSet("select * from BatchPackage where procFlag='1' and stopFlag='N'"); 
        if(rowSet.size()>0) {
            Map row = (Map) rowSet.get(0);
            String taxOrgCode = (String) row.get("taxOrgCode");
            String entrustDate = (String) row.get("entrustDate");
            String packNo = (String) row.get("packNo");
            //yangyuanxu add
            String payeeBankNo = (String) row.get("payeeBankNo");
            String protocolNo = (String)row.get("protocolNo");
            //设置为正在处理中
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //yangyuanxu update
            String sql = "update BatchPackage set procFlag='2',procBeginTime='" + df.format(new Date()) + "'" +
                    " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='" + payeeBankNo + "'";
            DBUtil.executeUpdate(sql);
            msg.set("TaxOrgCode",taxOrgCode);
            msg.set("EntrustDate",entrustDate);
            msg.set("PackNo",packNo);
            //yangyuanxu add
            msg.set("PayeeBankNo", payeeBankNo);
            msg.set("ProtocolNo", protocolNo);
            System.out.println("payeeBankNo is:"+msg.getString("PayeeBankNo"));
            return SUCCESS;
        }
        return FAIL;
    }
}
