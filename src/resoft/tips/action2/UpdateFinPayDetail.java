package resoft.tips.action2;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新支付令包表中处理状态</p>
 * Date: 2009-3-13
 * Time: 0:29:45
 */
public class UpdateFinPayDetail implements Action {
    public int execute(Message msg) throws Exception {
    	String id=msg.getString("detailID");
    	String addword=msg.getString("AddWord");
    	String result=msg.getString("Result");
    	String bdgOrgCode = (String)(msg.get("BdgOrgCode"));
		String funcSbtCode = (String)(msg.get("FUNCSBTCODE"));
        //设置为处理完毕
        String sql = "update payorderdetail set ProcStatus='"+result+"',addword='" +addword+"' where  ID=" + id + "";
        
        DBUtil.executeUpdate(sql); 
        if(msg.get("BalanceAmt")!=null){
        DBUtil.executeUpdate("UPDATE PayQuotaBalance SET BalanceAmt=" + msg.get("BalanceAmt") + " WHERE BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'");
        }
        return SUCCESS;
    }
}
