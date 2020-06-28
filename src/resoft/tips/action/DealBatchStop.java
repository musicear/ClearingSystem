package resoft.tips.action;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.basLink.util.MessageSender;
import resoft.tips.bankImpl.BatchSocketMessageSender;
//import resoft.tips.bankImpl.SyncSocketMessageSender;
//import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>检查批量包中可发送扣税的记录</p>
 * Author: zhuchangwu
 * Date: 2007-8-30
 * Time: 22:58:11
 */
public class DealBatchStop implements Action {
	private static Configuration conf = Configuration.getInstance();
    public int execute(Message msg) throws Exception {
    	String teller="666666";//福州柜
    	DateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String traDate=df.format(new Date());

    	List changeDateRow=QueryUtil.queryRowSet("select workDate from ChangeDate where id=1");
    	Map changeDateMap=(Map)changeDateRow.get(0);
    	String changeDate=(String)changeDateMap.get("workDate");
    	msg.set("changeDate", changeDate);
    	
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String traNo="";
        String updateSqlDetail = "update BatchPackDetail set result='24009',addWord='未签定协议',taxDate='" +traDate+"',teller='"+teller+"'";
        String sqlWhere= " where stopFlag='N' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +
        "' and packNo='" + packNo + "'";
        String sql = "select * from BatchPackDetail " +
                " where stopFlag='N' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +
                "' and packNo='" + packNo + "' and traNo not in(select oriTraNo from DelApply where taxOrgCode='" + taxOrgCode + "' and oriEntrustDate='" + entrustDate +"' and oriPackNo='" + packNo + "')";
        List rowSet = QueryUtil.queryRowSet(sql);
        List traNos=new ArrayList();
        if(rowSet.size()>0){
        	System.out.println("the sqlsize in DealBatchStop is:"+rowSet.size());
        	Message sendMsg = new DefaultMessage();
        	List list=new ArrayList();
        	Map row =null;
        	String payAcct="";
        	String protocolNo="";
        	String tipsWorkDate="";
        	
        	double total=0.00;
        	for(int i=0;i<rowSet.size();i++){
        		 row = (Map) rowSet.get(i);
        		 payAcct=(String)row.get("payAcct");
        		 protocolNo=(String)row.get("protocolNo");
        		 tipsWorkDate=(String)row.get("tipsWorkDate");
        		 traNo=(String)row.get("traNo");
        		 //检查是否签约
        		 String contSql="select * from ProveInfo where verifyResult='0' and  taxOrgCode='" + taxOrgCode + "' and payAcct='" + payAcct + "' and protocolNo='" + protocolNo + "'";
        		 //int cnt=DBUtil.queryForInt(contSql); 
        		 
        		 List contRow=QueryUtil.queryRowSet(contSql);
        		 //int num=2;
        		 if(contRow.size()>0){
//        			 Map conData=(Map)contRow.get(0);
        			 Message subMsg = new DefaultMessage();
        			 subMsg.set("交易码", "BatchSendDetails");
        			 subMsg.set("记录头", "RM");
        			 subMsg.set("最后一条标志", "0");
        			 subMsg.set("保留字段", "");
        			 subMsg.set("Khbh", taxOrgCode+entrustDate+packNo+(String)row.get("traNo"));
        			 subMsg.set("Sjrq", tipsWorkDate.substring(0,4)+"-"+tipsWorkDate.substring(4, 6)+"-"+tipsWorkDate.substring(6, 8));
        			 subMsg.set("Khzh", payAcct);
        			 subMsg.set("Zhlx","0");//"1"(String)conData.get("AcctSeq")
        			 String traAmt=(String)row.get("traAmt");
        			 subMsg.set("Fse", traAmt);
        			 subMsg.set("Znj", "0");
        			 total+=new Double(traAmt).doubleValue();
        			 list.add(subMsg);
        			 traNos.add(traNo);
        		 }else{
        			 String sqlUpdate=updateSqlDetail+sqlWhere+ " and traNo='"+traNo+"'";
                     DBUtil.executeUpdate(sqlUpdate);
                     
        		 }
        		 

        	}

        	if(list.size()>0){
   			    Message lastMsg = new DefaultMessage();
			    lastMsg.set("交易码", "BatchSendDetails");
			    lastMsg.set("记录头", "RM");
			    lastMsg.set("最后一条标志", "1");
			    list.add(lastMsg);
        	    sendMsg.set("BatchSendDetails", list);
        		String totalSql="select * from BatchPackage where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +"' and packNo='" + packNo + "'";
        		List batchRow = QueryUtil.queryRowSet(totalSql);
        		Map batchMap=(Map)batchRow.get(0);
        		sendMsg.set("交易码","030804");
    			sendMsg.set("柜员号","666666");
    			sendMsg.set("网点号","0004");
    			sendMsg.set("业务交易日",changeDate);
    			sendMsg.set("交易流水号",(String)batchMap.get("packNo"));
        		sendMsg.set("Dwbh", "0000000001");
        		sendMsg.set("Dljybh", "00001");
        		sendMsg.set("Wdlsh",(String)batchMap.get("packNo"));//(String)batchMap.get("taxOrgCode")+(String)batchMap.get("entrustDate")+(String)batchMap.get("packNo")+(String)batchMap.get("traNo")
        		sendMsg.set("Jybs", String.valueOf(list.size()-1));
        		sendMsg.set("JYJE", String.valueOf(total));
        		sendMsg.set("SXF", "0");
        		sendMsg.set("Bzh", "01");
        		sendMsg.set("Dwzh", "9010210010010000002761");//(String)batchMap.get("payeeAcct")
        		sendMsg.set("Zhlx", "1");
        		sendMsg.set("Dllx", "1");
        		sendMsg.set("Ssbz", "1");
        		sendMsg.set("Khbz", "0");
        		sendMsg.set("Zym", "A15");
        		MessageSender sender = new BatchSocketMessageSender();//MessageSenderUtil.getMessageSender();
                sender.setProperty("host",conf.getProperty("bankImpl", "host"));
                sender.setProperty("port",conf.getProperty("bankImpl", "port"));
                sender.setProperty("packager","resoft.tips.bankImpl.fjnxbank.FJNXPackager");
                sender.setProperty("lengthAccessor","resoft.tips.bankImpl.EbcLengthAccessor");
        		Message returnMsg = sender.send(sendMsg);
                String RespCode = returnMsg.getString("RespCode");//主机流水号
                System.out.println("the RespCode is:.................."+RespCode);

                if("000".equals(RespCode)){
                	msg.set("bankNo",returnMsg.getString("主机流水号"));
                	msg.set("Result","94999");
                	msg.set("AddWord", "银行接受批量数据成功,未返回成功数据");
                	for(int j=0;j<traNos.size();j++){
                		traNo=(String)traNos.get(j);
                    	updateSqlDetail="update BatchPackDetail set result='94999',addWord='银行接受批量数据成功,未返回成功数据',taxDate='" +traDate+"',teller='"+teller+"'"+sqlWhere+" and traNo='"+traNo+"'";
                    	DBUtil.executeUpdate(updateSqlDetail);
                	}

                }else{//发送银行失败
                	for(int j=0;j<traNos.size();j++){
                		traNo=(String)traNos.get(j);
                    	updateSqlDetail="update BatchPackDetail set result='94999',addWord='交易处理失败',taxDate='" +traDate+"',teller='"+teller+"'"+sqlWhere+" and traNo='"+traNo+"'";
                    	DBUtil.executeUpdate(updateSqlDetail);	
                	}
                	msg.set("Result", "94999");
                	msg.set("AddWord", "交易处理失败");
                }
        	}else{
                msg.set("Result", "24009");
                msg.set("AddWord", "都未签定协议");	
        	}
        	//标记已经止付的明细记录
            updateSqlDetail = "update BatchPackDetail set stopFlag='Y',result='24020',addWord='已经止付',taxDate='" +traDate+"',teller='"+teller+"'"+
            " where stopFlag='N' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +
            "' and packNo='" + packNo + "' and traNo in(select oriTraNo from DelApply where taxOrgCode='" + taxOrgCode + "' and oriEntrustDate='" + entrustDate +"' and oriPackNo='" + packNo + "')";        	
            DBUtil.executeUpdate(updateSqlDetail);        	
        }else{

            updateSqlDetail = "update BatchPackDetail set stopFlag='Y',result='24020',addWord='已经止付',taxDate='" +traDate+"',teller='"+teller+"'"+
            " where stopFlag='N' and taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate +
            "' and packNo='" + packNo + "' and traNo in(select oriTraNo from DelApply where taxOrgCode='" + taxOrgCode + "' and oriEntrustDate='" + entrustDate +"' and oriPackNo='" + packNo + "')";        	
            DBUtil.executeUpdate(updateSqlDetail);
            msg.set("Result", "24020");
            msg.set("AddWord", "已经止付");
            //已止付
            //return FAIL;
        }
        return SUCCESS;
    }
}
