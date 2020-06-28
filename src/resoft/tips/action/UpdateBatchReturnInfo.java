package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存回执包信息</p>
 * Author: liguoyin
 * Date: 2007-8-12
 * Time: 12:49:23
 */
public class UpdateBatchReturnInfo implements Action {
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String returnDate = msg.getString("//CFX/MSG/BatchHead2102/EntrustDate");
        String returnPackNo = msg.getString("//CFX/MSG/BatchHead2102/PackNo");
        String succNum = msg.getString("//CFX/MSG/BatchHead2102/SuccNum");
        String succAmt = msg.getString("//CFX/MSG/BatchHead2102/SuccAmt");
        String sql = "update BatchPackage set returnDate='" + returnDate + "',returnPackNo='" + returnPackNo + "',succNum=" + succNum + ",succAmt=" + succAmt +",procFlag='4'"+
                " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "'";
        String sqlDetail = "update BatchPackDetail set returnPackNo='" + returnPackNo + "'"+
        " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "'";        
        DBUtil.executeUpdate(sql);
        DBUtil.executeUpdate(sqlDetail);
        return SUCCESS;
    }
}
