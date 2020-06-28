package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Э��ɾ��</p>
 * Author: zhuchangwu
 * Date: 2007-08-23
 * Time: 10:10:10
 */

public class RemoveProveInfo implements Action {
    private static final Log logger = LogFactory.getLog(RemoveProveInfo.class);

    public int execute(Message msg) throws Exception {
    	logger.info("------------------this is  RemoveProveInfo.action-------------------------------");
        String TaxPayCode = msg.getString("TaxPayCode");
        String ProtocolNo = msg.getString("ProtocolNo");
        String addWord = msg.getString("AddWord");
        String payAcct = msg.getString("PayAcct");//�����ʺ�
        String[] Word = addWord.split("��");
        String AddWord = Word[0];
        String verifyResult=msg.getString("verifyResult");
        String EnabledFlag=msg.getString("EnabledFlag");
        try{
            Map params = new HashMap();
//            params.put("RemoveTeller", msg.get("RemoveTeller"));//Ȫ�ݹ̶�Ϊ999999
//            DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
//            params.put("RemoveTime", dtFormat.format(new Date()));
            params.put("EnabledFlag", EnabledFlag);
            params.put("verifyResult", verifyResult);
            params.put("AddWord", AddWord); 
            DBUtil.executeUpdate("update ProveInfo set EnabledFlag=#EnabledFlag#,verifyResult=#verifyResult#,AddWord=#AddWord# where  TaxPayCode='" + TaxPayCode + "' and protocolNo='" + ProtocolNo + "' and payAcct='"+payAcct+"'", params);
//            msg.set("ReturnResult", "Y");
//            msg.set("AddWord", "Э��ɾ���ɹ�");
            return SUCCESS;
        }catch(Exception e){
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "����ʧ��");
            return FAIL;
        }

    }


}
