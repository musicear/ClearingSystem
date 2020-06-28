package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>
 * 银行端缴款税票信息核对通知
 * </p>
 * Author: zhuchangwu Date: 2007-8-31 Time: 14:09:32
 */
public class Process3113 implements Action {
//	private static final Log logger = LogFactory.getLog(Process3113.class);

	public int execute(Message msg) throws Exception {
        List compareList=(List)msg.get("compareList");
        System.out.println("the compareList size is:"+compareList.size());
        String sqlWhere="select * from DeclareInfo where result!='90000' or result==null or result='' ";
      /*  for(int i=0;i<compareList.size();i++){
        	Message sumMsg=(Message)compareList.get(i);
        	String OriTaxOrgCode=sumMsg.getString("OriTaxOrgCode");
        	String OriEntrustDate=sumMsg.getString("OriEntrustDate");
        	String OriTraNo=sumMsg.getString("OriTraNo");
        	String TaxVouNo=sumMsg.getString("TaxVouNo");
        	String TraAmt=sumMsg.getString("TraAmt");
        	String comTime=this.addOneHour();
        	String sql=sqlWhere+" taxOrgCode='"+OriTaxOrgCode+"' and entrustDate='"+OriEntrustDate+"' and traNo='"+OriTraNo+"' "+
        	                    " and taxVouNo='"+TaxVouNo+"' and TraAmt="+TraAmt+" and saveTime>'"+comTime+"'";
        	List rowSet = QueryUtil.queryRowSet(sql);
        	if(rowSet.size()>0){
        		Message sendMsg=new DefaultMessage();
        		sendMsg.set("交易码","3113Detail");
        		sendMsg.set("TaxVouNo", TaxVouNo);
        		sendMsg.set("OriTaxOrgCode", OriTaxOrgCode);
        		sendMsg.set("OriEntrustDate", OriEntrustDate);
        		
        		Map row=(Map)rowSet.get(0);
        		sendMsg.set("TaxDate", (String)row.get("taxDate"));
        		sendMsg.set("OriTraNo", (String)row.get("traNo"));
        		sendMsg.set("Result", "24003");
        		sendMsg.set("AddWord", "用户已撤销");
                String updateSql="update DeclareInfo set result='24003' and addWord='用户已撤销' where "+
                                 " and traNo='"+OriTraNo+"' and taxOrgCode='"+OriTaxOrgCode+"' and entrustDate='"+OriEntrustDate+"' and traNo='"+OriTraNo+"' ";
                DBUtil.executeUpdate(updateSql);
                Controller controller = new Controller();
                controller.setNameOfTransCode("交易码");
                //controller.execute(sendMsg);
        	}*/
        
        for(int i=0;i<compareList.size();i++){
        	Message sumMsg=(Message)compareList.get(i);
        	String OriTaxOrgCode=sumMsg.getString("OriTaxOrgCode");
        	String OriEntrustDate=sumMsg.getString("OriEntrustDate");
        	String OriTraNo=sumMsg.getString("OriTraNo");
        	String TaxVouNo=sumMsg.getString("TaxVouNo");
        	String TraAmt=sumMsg.getString("TraAmt");
        	String comTime=this.addOneHour();
        	String sql=sqlWhere+" taxOrgCode='"+OriTaxOrgCode+"' and entrustDate='"+OriEntrustDate+"' and traNo='"+OriTraNo+"' "+
        	                    " and taxVouNo='"+TaxVouNo+"' and TraAmt="+TraAmt+" and saveTime>'"+comTime+"'";
        	List rowSet = QueryUtil.queryRowSet(sql);
        	if(rowSet.size()>0){
        		Message sendMsg=new DefaultMessage();
        		//msg.set("交易码","3113Detail");
        		msg.set("TaxVouNo", TaxVouNo);
        		msg.set("OriTaxOrgCode", OriTaxOrgCode);
        		msg.set("OriEntrustDate", OriEntrustDate);
        		
        		Map row=(Map)rowSet.get(0);
        		msg.set("TaxDate", (String)row.get("taxDate"));
        		msg.set("OriTraNo", (String)row.get("traNo"));
        		msg.set("Result", "24003");
        		msg.set("AddWord", "用户已撤销");
                String updateSql="update DeclareInfo set result='24003' and addWord='用户已撤销' where "+
                                 " and traNo='"+OriTraNo+"' and taxOrgCode='"+OriTaxOrgCode+"' and entrustDate='"+OriEntrustDate+"' and traNo='"+OriTraNo+"' ";
                DBUtil.executeUpdate(updateSql);
                Controller controller = new Controller();
                //controller.setNameOfTransCode("交易码");
                //controller.execute(sendMsg);
        	}
        }
		return SUCCESS;
	}
    private String addOneHour(){
    	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Date curDate=new Date();
        long lontime=(curDate.getTime()/1000)-60*60;
        curDate.setTime(lontime*1000);
        String compDate=formatter.format(curDate);
    	return compDate;
    }
}
