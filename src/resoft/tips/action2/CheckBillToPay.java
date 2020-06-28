/*
 * Created on 2009-3-10
 *
 */
package resoft.tips.action2;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * @author haoruibing
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckBillToPay implements Action{
	private static final Log logger = LogFactory.getLog(CheckBillToPay.class);
	/* (non-Javadoc)
	 * @see resoft.xlink.core.Action#execute(resoft.xlink.core.Message)
	 * 轮训要处理的支付凭证
	 */
	public int execute(Message msg) throws Exception {
		String packID=msg.getString("PackID");
		String selectID = "select ID from payorder where procstatus='0' and packID="+packID;
		List rowSet = QueryUtil.queryRowSet(selectID);
		 for(int i=0;i<rowSet.size();i++){
	            Map row = (Map) rowSet.get(i);
	            String billID = (String) row.get("ID");
	            //设置为正在处理中
	            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            String sql = "update payorderpack set ProcStatus='1',procBeginTime='" + df.format(new Date()) + "'" +
	                    " where  ID='" + billID + "'";
	            DBUtil.executeUpdate(sql);
	            Message newMsg = new DefaultMessage();
	            newMsg.set("billID",billID);
	            DBUtil.executeUpdate("update payorder set ProcStatus='1',procBeginTime='" + df.format(new Date()) + "' where  ID='" + billID + "'");
	            //寻找需要处理的明细信息，转到finPayDetail流程去处理
	            checkFinPayDetail(billID);
	            
	            List list=QueryUtil.queryRowSet("select id from payorderdetail where procStatus='3'");
	            String payStatus="";
	            String Description="";
	            if(list.size()==0){
	            	payStatus="2";
	            	Description="成功";
	            }else{
	            	payStatus="0";
	            	Description="序号为";
	            	for(int j=0;j<list.size()&&i<10;j++){
	            		Description=Description+list.get(i)+",";
	            	}
	            	Description=Description+"等审核有问题";
	            }
	            String update = "update payorder set ProcStatus='2',Description='"+Description+"', payState='"+payStatus+"',procendtime='" + df.format(new Date()) + "'"+
 	            " where  ID='" + billID + "'";
	            //logger.info(update);
		        DBUtil.executeUpdate(update);
	        }
	        return SUCCESS;
	}
	private int checkFinPayDetail(String billID) throws SQLException{
		String query = "select * from payorderdetail where procstatus='0' and payID="+billID;
		List rowSet = QueryUtil.queryRowSet(query);
	        for(int i=0;i<rowSet.size();i++) {
	        	Message newMsg=new DefaultMessage();
	            Map row = (Map) rowSet.get(i);
	            String id = (String) row.get("ID");
	            String FinOrgCode = (String) row.get("FinOrgCode");
	            String entrustDate = (String) row.get("entrustDate");
	            String packNo = (String) row.get("packNo");
	            String TraNo = (String) row.get("TraNo");
	            String SeqNo = (String) row.get("SeqNo");
	            String BdgOrgCode = (String) row.get("BdgOrgCode");
	            String FUNCSBTCODE = (String) row.get("FUNCSBTCODE");
	            String EconSubjectCode = (String) row.get("EconSubjectCode");
	            String Amt = (String) row.get("Amt");
	            String DialFundDate = (String) row.get("DialFundDate");
	            String BdgOrgOpnBnkAcct = (String) row.get("BdgOrgOpnBnkAcct");
	            String BdgOrgName = (String) row.get("BdgOrgName");
	            String BdgOrgAdd = (String) row.get("BdgOrgAdd");
	            String BdgOrgOpnBnkNo = (String) row.get("BdgOrgOpnBnkNo");
	            String AddWord = (String) row.get("AddWord");
	            newMsg.set("FinOrgCode", FinOrgCode);
	            newMsg.set("entrustDate", entrustDate);
	            newMsg.set("packNo", packNo);
	            newMsg.set("TraNo", TraNo);
	            newMsg.set("SeqNo", SeqNo);
	            newMsg.set("BdgOrgCode", BdgOrgCode);
	            newMsg.set("FUNCSBTCODE", FUNCSBTCODE);
	            newMsg.set("EconSubjectCode", EconSubjectCode);
	            newMsg.set("Amt", Amt);
	            newMsg.set("DialFundDate", DialFundDate);
	            newMsg.set("BdgOrgOpnBnkAcct", BdgOrgOpnBnkAcct);
	            newMsg.set("BdgOrgName", BdgOrgName);
	            newMsg.set("BdgOrgAdd", BdgOrgAdd);
	            newMsg.set("BdgOrgOpnBnkNo", BdgOrgOpnBnkNo);
	            newMsg.set("AddWord", AddWord);
	            newMsg.set("detailID", id);
	            
	            newMsg.set("交易码","finPayDetail");
	            Controller controller = new Controller();
	            controller.setNameOfTransCode("交易码");
	            controller.execute(newMsg);
	}
			return 0;
	}
}
