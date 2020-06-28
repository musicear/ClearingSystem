package resoft.tips.action;

import java.util.Iterator;
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
 * <p>取出明细扣款信息，进行扣款</p>
 * Author: liguoyin
 * Date: 2007-6-12
 * Time: 19:50:59
 */
public class QueryPackDetails implements Action {
	
	private static final Log logger = LogFactory.getLog(QueryPackDetails.class);
	
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String protocolNo = msg.getString("ProtocolNo");
        String payeeBankNo = msg.getString("PayeeBankNo");
        //yangyuanxu update
        String sql = "select bd.tipsWorkDate,bd.payOpBkCode,bp.payBkCode,bd.payAcct,bd.traAmt,bp.payeeAcct,bd.protocolNo,bd.traNo,bd.ChkDate,bd.payeeBankNo,bp.payeeAcct,bd.TaxPayName from BatchPackDetail bd,BatchPackage bp" +
                " where (bd.result!='90000' or bd.result is null)  and bd.packNo=bp.packNo and bd.payeeBankNo=bp.payeeBankNo and bp.stopFlag='N' and bp.taxOrgCode='" + taxOrgCode + "' and bp.entrustDate='" + entrustDate + "' and bp.packNo='" + packNo + "' and bp.payeeBankNo='" + payeeBankNo + "'";
        logger.info("测试 SQL is："+sql);
        try{
	        List rowSet = QueryUtil.queryRowSet(sql);
	        for (Iterator itr = rowSet.iterator(); itr.hasNext();) {
	            Map row = (Map) itr.next();
	            Message newMsg = new DefaultMessage();
	            newMsg.set("交易码","packDetail");
	            newMsg.set("//CFX/HEAD/WorkDate", row.get("tipsWorkDate"));
	            newMsg.set("//CFX/MSG/RealHead3001/TaxOrgCode", taxOrgCode);
	            newMsg.set("//CFX/MSG/RealHead3001/EntrustDate", entrustDate);
	            newMsg.set("TaxOrgCode",taxOrgCode);
	            newMsg.set("EntrustDate",entrustDate);
	            newMsg.set("PackNo",packNo);
	            newMsg.set("TraNo",row.get("traNo"));
	            newMsg.set("ChkDate",row.get("ChkDate"));
	            newMsg.set("//CFX/MSG/TurnAccount3001/PayOpBkCode", row.get("payOpBkCode"));
	            newMsg.set("//CFX/MSG/TurnAccount3001/PayBkCode", row.get("payBkCode"));
	            //yangyuanxu add
	            newMsg.set("//CFX/MSG/TurnAccount3001/PayeeBankNo", row.get("payeeBankNo"));
	            newMsg.set("payeeBankNo", row.get("payeeBankNo"));
	            newMsg.set("//CFX/MSG/Payment3001/PayAcct", row.get("payAcct"));
	            newMsg.set("//CFX/MSG/TurnAccount3001/PayeeAcct", row.get("payeeAcct"));
	            newMsg.set("//CFX/MSG/Payment3001/TraAmt", row.get("traAmt"));
	            newMsg.set("//CFX/MSG/Payment3001/ProtocolNo",row.get("protocolNo"));
	            newMsg.set("//CFX/HEAD/WorkDate", row.get("tipsWorkDate"));
	            newMsg.set("//CFX/MSG/RealHead3001/TraNo", row.get("traNo"));
	            newMsg.set("//CFX/MSG/Payment3001/TraAmt", row.get("traAmt")); 
	            newMsg.set("//CFX/MSG/Payment3001/TaxPayName", row.get("TaxPayName"));
	            newMsg.set("TaxDate", row.get("TipsWorkDate"));
	            //yangyuanxu update
	            String voucherSql="select TAXTYPENAME from VoucherTaxType where bizCode='2' and taxOrgCode='"+taxOrgCode+"' and entrustDate='"+entrustDate+"' and packNo='"+packNo+"' and traNo='"+row.get("traNo")+"' and payeeBankNo='"+row.get("payeeBankNo")+"' and rownum=1";
	            logger.info("查询税种 SQL is："+voucherSql);
	            String TAXTYPENAME=DBUtil.queryForString(voucherSql);	            
	            newMsg.set("//CFX/MSG/Payment3001/TaxTypeNum", "2");
	            newMsg.set("BATCHTAXTYPENAME", TAXTYPENAME);
	            	            
	            Controller controller = new Controller();
	            controller.setNameOfTransCode("交易码");
	            controller.execute(newMsg);
	        }
        }catch(Exception ex){
        	ex.printStackTrace();
           /*
        	//yangyuanxu update
        	String updateSql = "update BatchPackage set procFlag='1',procEndTime='" + DateTimeUtil.getDateTimeString() +"'"+
            " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='"+payeeBankNo+"'";
            DBUtil.executeUpdate(updateSql);
            */
        	return FAIL;
        }
        return SUCCESS;
    }
}
