package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class CheckReShenbao implements Action {

	public int execute(Message msg) throws Exception {
		String strsql="select * from DeclareInfo where OuterLevyNo='"+msg.getString("OuterLevyNo")+"' and Result='"+90000+"'";
	
		int count=DBUtil.queryForInt(strsql);
		if(count>0)
		{
			msg.set("AddWord", "此信息已申报！");
			msg.set("ReturnResult", "N");
			return FAIL; 
			
		}
		else 
			return SUCCESS;
	}
}