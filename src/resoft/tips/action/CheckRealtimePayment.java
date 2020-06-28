package resoft.tips.action;


import java.util.List;
import java.util.Map;
//import java.util.Map;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
//import org.zerone.jdbc.QueryUtil;
//import resoft.basLink.util.MessageSendException;
//import resoft.basLink.util.MessageSender;
//import resoft.tips.util.MessageSenderUtil;
import resoft.xlink.core.Message;
//import resoft.xlink.impl.DefaultMessage;

/**
 * <p>����ʵʱ����������</p>
 * User:zhangshaolei
 * Date:2007-5-14
 * Time:20:20:30
 */
public class CheckRealtimePayment implements Action {
//	private static final Log logger = LogFactory.getLog(CheckRealtimePayment.class);
    public int execute(Message msg) throws Exception {
        String taxOrgCode = (String) msg.get("//CFX/MSG/RushApply1021/TaxOrgCode");
        String oriEntrustDate = (String) msg.get("//CFX/MSG/RushApply1021/OriEntrustDate");
        String oriTraNo = (String) msg.get("//CFX/MSG/RushApply1021/OriTraNo");
        //�жϳ������м�¼�Ƿ��Ѳ�����ϣ����Ѳ��������
//        String sql="select result,cancelFlag from RealtimePayment where  TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
//        List values = QueryUtil.queryRowSet(sql);
        String sql = "select count(*) from RealtimePayment where result='90000' and " +
        " TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
        int cnt = DBUtil.queryForInt(sql);
        System.out.println("sql:.................."+sql);
        //System.out.println("the cnt is....................................:"+cnt);
        
        if (cnt == 0) {
            //�����ڴ�c
//            msg.set("cancelAnswer","2");
//            msg.set("addWord","����ʧ�ܣ��Ѿ���˰");
        	String strsql = "select bankTraNo,traAmt,payeeAcct,payAcct,bankTraDate from RealtimePayment where  " +
            " TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
        	List row =QueryUtil.queryRowSet(strsql);
            msg.set("REF_NO", ((Map)row.get(0)).get("bankTraNo"));
            msg.set("TRAN_AMT", ((Map)row.get(0)).get("traAmt"));
            msg.set("CR_ACCT_NO", ((Map)row.get(0)).get("payeeAcct"));//dai
            msg.set("DR_ACCT_NO", ((Map)row.get(0)).get("payAcct"));//jie
            msg.set("BU_SETTLEMENT_DATE",((Map)row.get(0)).get("bankTraDate"));
        	return SUCCESS;
        } else {
            String strsql = "select bankTraNo,traAmt,payeeAcct,payAcct,bankTraDate from RealtimePayment where result='90000' and " +
            " TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
            List row =QueryUtil.queryRowSet(strsql);
            msg.set("REF_NO", ((Map)row.get(0)).get("bankTraNo"));
            msg.set("TRAN_AMT", ((Map)row.get(0)).get("traAmt"));
            msg.set("CR_ACCT_NO", ((Map)row.get(0)).get("payeeAcct"));//dai
            msg.set("DR_ACCT_NO", ((Map)row.get(0)).get("payAcct"));//jie
            msg.set("BU_SETTLEMENT_DATE",((Map)row.get(0)).get("bankTraDate"));
          return SUCCESS;
        }
    }
}

