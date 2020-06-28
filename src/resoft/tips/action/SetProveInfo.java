package resoft.tips.action;


import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���÷���TIPS���ġ�����Э��ǩ�����󡱱���</p>
 * Author: liwei
 * Date: 2007-07-09
 * Time: 15:06:06
 */
public class SetProveInfo implements Action {
    public int execute(Message msg) throws Exception {

        msg.set("//CFX/MSG/ProveInfo9114/VCSign", msg.get("VCSign"));
        //String orgCode = conf.getProperty("general","BankSrcNodeCode");
        msg.set("//CFX/MSG/ProveInfo9114/TaxOrgCode", msg.get("TaxOrgCode"));
        msg.set("//CFX/MSG/ProveInfo9114/TaxPayName", msg.get("TaxPayName"));
        msg.set("//CFX/MSG/ProveInfo9114/TaxPayCode", msg.get("TaxPayCode"));
        msg.set("//CFX/MSG/ProveInfo9114/PayOpBkCode", msg.get("PayOpBkCode")==null?"1111":msg.get("PayOpBkCode"));//TIPS��������,���ǲ���Ϊ��
        msg.set("//CFX/MSG/ProveInfo9114/PayBkCode", msg.get("PayBkCode"));    
        msg.set("//CFX/MSG/ProveInfo9114/PayAcct", msg.get("PayAcct"));
        msg.set("//CFX/MSG/ProveInfo9114/HandOrgName", msg.get("HandOrgName")==null?msg.get("PayAcctName"):msg.get("HandOrgName"));
        msg.set("//CFX/MSG/ProveInfo9114/ProtocolNo", msg.get("ProtocolNo"));

        //Ԥ���趨����ʧ�ܡ��ȳɹ�֮�����޸�״̬
        msg.set("ReturnResult", "N");
        msg.set("AddWord","˰�������Ӧ������ϵ����Ա���Ժ��ط�");

        return SUCCESS;
    }
}


