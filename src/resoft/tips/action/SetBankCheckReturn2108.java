package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
//import resoft.tips.util.DateTimeUtil;

/**
 * <p>�������ж˽ɿ�˰Ʊ��Ϣ�˶�֪ͨ��ִ��Ϣ</p>
 * Author: zhuchangwu
 * Date: 2007-9-1
 * Time: 18:37:44
 */
public class SetBankCheckReturn2108 implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/SingleReturn2108/TaxVouNo",msg.get("TaxVouNo"));
        msg.set("//CFX/MSG/SingleReturn2108/OriTaxOrgCode",msg.get("OriTaxOrgCode"));
        msg.set("//CFX/MSG/SingleReturn2108/OriEntrustDate",msg.get("OriEntrustDate"));
        msg.set("//CFX/MSG/SingleReturn2108/OriTraNo",msg.get("OriTraNo"));
        msg.set("//CFX/MSG/SingleReturn2108/TaxDate", msg.get("TaxDate"));
        msg.set("//CFX/MSG/SingleReturn2108/Result", msg.get("Result"));
        msg.set("//CFX/MSG/SingleReturn2108/AddWord", msg.get("AddWord"));
        return SUCCESS;
    }
}
