/*
 * Created on 2009-3-10
 *
 */
package resoft.tips.action2;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * @author haoruibing
 *检查额度是否满足，科目是否存在
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckQuota implements Action{
	 private static final Log logger = LogFactory.getLog(CheckQuota.class);
	/* (non-Javadoc)
	 * @see resoft.xlink.core.Action#execute(resoft.xlink.core.Message)
	 */
	public int execute(Message msg) throws Exception {
		String bdgOrgCode = (String)(msg.get("BdgOrgCode"));
		String funcSbtCode = (String)(msg.get("FUNCSBTCODE"));
		String finOrgCode = (String)(msg.get("FinOrgCode"));
		String entrustDate = (String)(msg.get("entrustDate"));
		String packNo = (String)(msg.get("packNo"));
		String traNo = (String)(msg.get("TraNo"));
		String seqNo = (String)(msg.get("SeqNo"));
		
		String whereClause = " FinOrgCode='" + finOrgCode + "' AND entrustDate='" + entrustDate + "' AND packNo='" + packNo + "' AND TraNo='" +traNo + "' AND SeqNo=" + seqNo + " ";
		String sql = "SELECT * FROM PayQuotaBalance WHERE BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'";
		List result = QueryUtil.queryRowSet(sql);
		if (result.size() == 0) {     // 如果不存在该额度科目，直接标志“拨款失败”
			msg.set("PayStatus", "3");
			String addWord="不存在额度科目";
			sql = "UPDATE PayOrderDetail SET  procStatus='3' , AddWord='" +addWord +"' WHERE " + whereClause;
			DBUtil.executeUpdate(sql);
			return -1;
		} else {
			Map balanceMap = (Map)(result.get(0));
			double balance = Double.parseDouble((String)(balanceMap.get("BalanceAmt")));
			double dialAmt = Double.parseDouble((String)(msg.get("Amt")));
			double newBalance = balance - dialAmt;
			msg.set("BalanceAmt", new Double(newBalance));
			if (newBalance<0) {
				String addWord="当前科目额度余额不足";
				sql = "UPDATE PayOrderDetail SET procStatus='3' , AddWord='" + addWord + "' WHERE " + whereClause;
				DBUtil.executeUpdate(sql);
				return -1;
			} 
			/*else {
				sql = "UPDATE PayQuotaBalance SET BalanceAmt=" + newBalance + " WHERE BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'";
				DBUtil.executeUpdate(sql);
			}*/
		}
		return 0;
	}

}
