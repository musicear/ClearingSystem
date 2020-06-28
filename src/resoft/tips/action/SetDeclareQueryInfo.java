package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置申报查询要素</p>
 * Author: liguoyin
 * Date: 2007-8-14
 * Time: 1:29:57
 */
public class SetDeclareQueryInfo implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/DeclareQuery2091/PayOpBkCode","102100000030");//
        msg.set("//CFX/MSG/DeclareQuery2091/BankName","bankName");
        msg.set("//CFX/MSG/DeclareQuery2091/TaxOrgCode",msg.get("TaxOrgCode"));
        msg.set("//CFX/MSG/DeclareQuery2091/CorpCode","0");//公司代码
        msg.set("//CFX/MSG/DeclareQuery2091/TaxPayCode",msg.get("TaxPayCode"));
        msg.set("//CFX/MSG/DeclareQuery2091/OuterLevyNo",msg.get("OuterLevyNo"));
        return SUCCESS;
    }
}
