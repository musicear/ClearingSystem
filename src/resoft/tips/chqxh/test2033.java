package resoft.tips.chqxh;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.AbstractSyncAction;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;
/**
 * <p>具体的账务处理</p>
 * User: liguoyin
 * Date: 2007-4-25
 * Time: 17:44:26
 */
public class test2033 extends AbstractSyncAction {
	Map params=new HashMap();
    public int execute(Message msg) throws Exception {    	  
    	test(msg);
        return SUCCESS;
    }        
    public void test(Message msg) throws Exception{
    	while (true){
    		//冲成功的扣款信息
    		//List rowSet = QueryUtil.queryRowSet("select * from TEST2033 where status='1' and markid='AAAAAAA' order by TraNo ");
    		//冲无状态的扣款信息
    		List rowSet = QueryUtil.queryRowSet("select * from TEST2033 where status='1' and markid is null order by TraNo ");
			if (rowSet.size()>0) {	
    			Map row = (Map) rowSet.get(0);
		    	//测试冲帐  
		    	ACE2033 ace2033=new ACE2033();
		    	ace2033.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("PayAcct")));
		    	ace2033.packTags.put("OldTradeNo", (String)row.get("TraNo"));
		    	ace2033.packTags.put("NewTradeNo","++++++++++");
		    	ace2033.packTags.put("TipsDate",DateTimeUtil.getDateString());
		    	ace2033.packTags.put("TradeAmount", (String)row.get("TRAAMT"));
		    	ace2033.packTags.put("TaxOrgCode", (String)row.get("taxOrgCode"));
		    	ace2033.initPack();    	    	
		    			    	
		    	//封装记账请求信息
		    	msg.set("ACESendObj", ace2033);
		    	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
		    	sender.sendMsg();
		    	
		    	//处理记账结果
		    	ace2033=(ACE2033)msg.get("ACERecObj");
		    	ace2033.makeTransBody();
		    	
		    	if (((String)ace2033.packTags.get("MarkId")).trim().equals("AAAAAAA")) {		    		
		    		DBUtil.executeUpdate("update TEST2033 set status='3',ADDWORD='2032冲帐成功' where TraNo='"+(String)row.get("TraNO")+"' ");
		    	}else {
		    		DBUtil.executeUpdate("update TEST2033 set status='4',ADDWORD='"+((String)ace2033.packTags.get("FailInfo")).trim()+"' where TraNo='"+(String)row.get("TraNO")+"' ");
		    	}	
			}
		}	        	
    }    
}
