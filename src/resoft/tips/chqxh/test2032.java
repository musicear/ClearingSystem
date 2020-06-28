package resoft.tips.chqxh;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
public class test2032 extends AbstractSyncAction {
	Map params=new HashMap();
    public int execute(Message msg) throws Exception {
    	try{
	    	for(int i=0;i<1000;i++){		    	
	    		String testFlag=DBUtil.queryForString("select flag from test2033 where status='2032' ");
	    		if (testFlag.equals("Y")) {
	    			List rowSet = QueryUtil.queryRowSet("select payacct,taxPayCode,taxOrgCode from proveinfo where EnabledFlag='Y' and verifyResult='0' ");
		    		for (int j=0;j<rowSet.size();j++) {
		    			String traNo=test2032.radomTra();
		    			Map row=(Map)rowSet.get(j);
				    	ACE2032 ace2032=new ACE2032();
				    	ace2032.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("PayAcct")));
				    	ace2032.packTags.put("TradeAmount", test2032.randomAmt());
				    	ace2032.packTags.put("TipsDate",DateTimeUtil.getDateString() );
				    	ace2032.packTags.put("TradeNo",traNo);
				    	ace2032.packTags.put("TaxOrgCode", ACEPackUtil.leftStr("12",(String)row.get("TaxOrgCode")));
				    	ace2032.packTags.put("UserNo", ACEPackUtil.leftStr("32",(String)row.get("TaxPayCode")));
				    	//System.out.println(ace2032.packTags);    	
				    	ace2032.initPack();    	    				    	
				    	//记录日志			    	
				    	params.put("TipsDate", ((String)ace2032.packTags.get("TipsDate")).trim());
				    	params.put("TraAmt", ((String)ace2032.packTags.get("TradeAmount")).trim());
				    	params.put("TraNo", ((String)ace2032.packTags.get("TradeNo")).trim());
				    	params.put("PayAcct", ((String)ace2032.packTags.get("PayAcct")).trim());
				    	params.put("TaxPayCode", ((String)ace2032.packTags.get("UserNo")).trim());
				    	params.put("TaxOrgCode", ((String)ace2032.packTags.get("TaxOrgCode")).trim());
				    	params.put("Status", "1");
				    	DBUtil.insert("TEST2033",params);
				    	params.clear();
				    				    	
				    	//封装记账请求信息
				    	msg.set("ACESendObj", ace2032);
				    	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
				    	sender.sendMsg();
				    	
				    	//处理记账结果    	
				    	ace2032=(ACE2032)msg.get("ACERecObj");
				    	ace2032.makeTransBody();
				    					    	
				    	params.put("MarkId", ((String)ace2032.packTags.get("MarkId")).trim());
				    	if (((String)ace2032.packTags.get("MarkId")).trim().equals("AAAAAAA")) {
				    		params.put("AddWord", "交易成功");
				    	}else {
				    		params.put("AddWord", ((String)ace2032.packTags.get("FailInfo")).trim());
				    	}
				    	DBUtil.executeUpdate("update test2033 set MarkId=#MarkId#,AddWord=#AddWord# where TraNo='"+traNo+"'",params);
				    	params.clear();			    	
		    		}
	    		}	    			    	
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}        	    	    
        return SUCCESS;
    }
        
    public static String randomAmt(){
		String amt="";
		NumberFormat nf = new DecimalFormat("00");
        amt+= nf.format(Math.random() * 100);
        amt+= "."+nf.format(Math.random() * 100).substring(0,2);
        return amt;
	}
    
    
    public static String radomTra(){    	    	
    	String tra="";    	
    	NumberFormat nf = new DecimalFormat("0000000000");
    	tra+= nf.format(Math.random() * 1000000000);
        return tra;
    }
    
    public static void main(String[] args){
    	for(int i=0;i<1000;i++){
    		System.out.println(test2032.randomAmt());
    	}
    }
    
}
