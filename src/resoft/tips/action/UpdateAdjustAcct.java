package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Message;
/**
 * <p>更新调帐表状态</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 19:11:18
 */
public class UpdateAdjustAcct  extends AbstractSyncAction {

    public int execute(Message msg) throws Exception {
    	String bankTraNo=msg.getString("bankTraNo");
    	String bankTraDate=msg.getString("bankTraDate");
    	String payAcct=msg.getString("payAcct");
    	String sql="update AdjustAcct set adjustStatus='3' where bankTraNo='"+bankTraNo+"' and bankTraDate='"+bankTraDate+"' and payAcct='"+payAcct+"' ";
        DBUtil.executeUpdate(sql);
        return SUCCESS;
    }
}
