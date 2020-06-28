package resoft.tips.chqxh;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>重庆信合与后台冲帐,TIPS日切的时候做一次冲帐</p>
 * Author: liwei
 * Date: 2007-10-10
 * Time: 09:36:06
 */
public class ProcessCheckAcctChongZhang implements Action{
	
	public int execute(Message msg) throws Exception {
        //对账信息
		String tipsDate = msg.getString("ChkDate");        		//对账日期
        String chkAcctType=msg.getString("ChkAcctType");		//对账类型
        if(chkAcctType.equals("1")){							//0:日间;1:日切
        	//进行冲帐    	
    		String sql = "select * from adjustAcct where adjustStatus='0' and chkDate='"+tipsDate+"' order by traNo";
            List adjustLists = QueryUtil.queryRowSet(sql);
            for (int i=0;i<adjustLists.size();i++){
            	//初始化冲帐信息	    		
	    		Map row=(Map)adjustLists.get(i);  
		    	ACE2033 ace2033=new ACE2033();
		    	ace2033.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("PayAcct")));
		    	ace2033.packTags.put("OldTradeNo",ACEPackUtil.leftStrFormat("10", (String)row.get("TraNo"), " "));
		    	ace2033.packTags.put("NewTradeNo","++++++++++");
		    	ace2033.packTags.put("TipsDate",tipsDate);
		    	ace2033.packTags.put("TradeAmount", (String)row.get("TraAmt"));
		    	ace2033.packTags.put("TaxOrgCode", (String)row.get("taxOrgCode"));
		    	//批量冲帐的时候需要原包号
		    	if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
		    		ace2033.packTags.put("OldPackNo", (String)row.get("packNo"));
		    	}
		    	ace2033.initPack();    	    	
		    			    	
		    	//封装记账请求信息
		    	msg.set("ACESendObj", ace2033);
		    	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
		    	sender.sendMsg();
		    	
		    	//处理记账结果
		    	ace2033=(ACE2033)msg.get("ACERecObj");
		    	if ( ace2033 !=null ) {//有冲帐应答
			    	ace2033.makeTransBody();			    	
			    	if (((String)ace2033.packTags.get("MarkId")).trim().equals("AAAAAAA")) { //冲帐成功		    		
			    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='冲帐成功' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");			    		
			    		//修改扣款明细中的状态
			    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
			    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='2' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}else {
			    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='2' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}			    		
			    	}else {//冲帐失败
			    		//修改扣款明细中的状态
			    		String failInfo=((String)ace2033.packTags.get("FailInfo")).trim();
			    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='"+failInfo+"' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
			    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}else {
			    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
			    		}
			    	}
		    	}else {//超时或是其他错误
		    		//修改扣款明细中的状态
		    		DBUtil.executeUpdate("update adjustAcct set adjustStatus='1',reason='冲帐其他错误' where chkDate='"+tipsDate+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		if ((String)row.get("packNo")!=null && !((String)row.get("packNo")).trim().equals("")) {
		    			DBUtil.executeUpdate("update batchPackDetail set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and packNo='"+(String)row.get("packNo")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		}else {
		    			DBUtil.executeUpdate("update realTimePayMent set revokeStatus='3' where chkDate='"+tipsDate+"' and entrustDate='"+(String)row.get("entrustDate")+"' and TraNo='"+(String)row.get("TraNO")+"' ");
		    		}
		    	}
            }        	
        }
        return SUCCESS;
    }		
}
