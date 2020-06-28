/*
 * Created on 2009-3-10
 *
 */
package resoft.tips.action2;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * @author haoruibing
 *检查预算单位信息是否验证成功,拿支付令里的预算单位代码，预算单位账号，预算单位开户行行号查找该单位信息是否验证成功
 *
 */
public class CheckVCResult implements Action{
	 private static final Log logger = LogFactory.getLog(CheckVCResult.class);
	/* (non-Javadoc)
	 * @see resoft.xlink.core.Action#execute(resoft.xlink.core.Message)
	 */
	public int execute(Message msg) throws Exception {
		String funcSbtCode = (String)(msg.get("FUNCSBTCODE"));
		String finOrgCode = (String)(msg.get("FinOrgCode"));
		String entrustDate = (String)(msg.get("entrustDate"));
		String packNo = (String)(msg.get("packNo"));
		String traNo = (String)(msg.get("TraNo"));
		String seqNo = (String)(msg.get("SeqNo"));
		String whereClause = " FinOrgCode='" + finOrgCode + "' AND entrustDate='" + entrustDate + "' AND packNo='" + packNo + "' AND TraNo='" +traNo + "' AND SeqNo=" + seqNo + " ";
		
		String bdgOrgCode = (String)(msg.get("BdgOrgCode"));
		String bdgOrgOpnBnkNo=(String)msg.get("BdgOrgOpnBnkNo");
		String bdgOrgOpnBnkAcct = (String)(msg.get("BdgOrgOpnBnkAcct"));
		
		String sql ="select * from BdgOrgInfo where PayeeOpBkNo='"+bdgOrgOpnBnkNo+"' and BdgOrgCode='"+bdgOrgCode+"' and PayeeAcct='"+bdgOrgOpnBnkAcct+"'";
		List result = QueryUtil.queryRowSet(sql);
		if (result.size() == 0) {     // 如果不成功
			msg.set("PayStatus", "3");
			String addWord="预算单位未经过验证，不予支付";
			sql = "UPDATE PayOrderDetail SET  procStatus='3' , AddWord='" +addWord +"' WHERE " + whereClause;
			DBUtil.executeUpdate(sql);
			return -1;
		} 
		
		return 0;
	}

}
