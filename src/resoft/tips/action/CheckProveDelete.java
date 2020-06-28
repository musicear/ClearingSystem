package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��ѯ�Ƿ��Ѿ�ǩԼ</p>
 * Author: liwei
 * Date: 2007-07-09
 * Time: 13:06:06
 */
public class CheckProveDelete implements Action {
    private static final Log logger = LogFactory.getLog(CheckProveDelete.class);
    public int execute(Message msg) throws Exception {
        //String payAcct = msg.getString("PayAcct");//�����ʺ�
        String taxPayCode = msg.getString("TaxPayCode");//��˰�˱���
        String ProtocolNo = msg.getString("ProtocolNo");//Э�����
        ///���˰����Ҫ��֤����Ҫ���� ����and verifyResult='0'
        String sql = "select TaxOrgCode,TaxPayName,HandOrgName,PayOpBkCode,PayBkCode,PayAcct from ProveInfo where EnabledFlag='Y' and verifyResult='0' "
                    +"and taxPayCode='" + taxPayCode  + "' and protocolNo='" + ProtocolNo+"' ";
        logger.info("sql:" + sql);
        List rowSet = QueryUtil.queryRowSet(sql);
        if (rowSet.size() > 0) {//�Ѿ��ɹ�ǩԼЭ�����
        	Map row=(Map)rowSet.get(0);
        	msg.set("VCSign", "1");
        	msg.set("TaxOrgCode", (String)row.get("TaxOrgCode"));
        	msg.set("TaxPayName", (String)row.get("TaxPayName"));
        	msg.set("HandOrgName", (String)row.get("HandOrgName"));
        	msg.set("PayOpBkCode", (String)row.get("PayOpBkCode"));
        	msg.set("PayBkCode", (String)row.get("PayBkCode"));
        	msg.set("PayAcct", (String)row.get("PayAcct"));
            Map params = new HashMap();
            params.put("RemoveTeller", msg.getString("UserId"));//Ȫ�ݹ̶�Ϊ999999
            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            params.put("RemoveTime", dtFormat.format(new Date()));
            DBUtil.executeUpdate("update ProveInfo set EnabledFlag='N',RemoveTeller=#RemoveTeller#,RemoveTime=#RemoveTime# where EnabledFlag='Y' and TaxPayCode='" + taxPayCode +  "' and protocolNo='" + ProtocolNo + "' ", params);
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "Э��ɾ���ɹ�");

            return SUCCESS;
        } else {
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "��Э�鲻���ڻ����Ѿ���Ч");
           return FAIL;

      }
    }
    }