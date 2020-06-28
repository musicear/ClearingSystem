package resoft.tips.action;

import resoft.tips.chqsh.TIPSDesOrgCode;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;;

/**
 * <p>…Ë÷√SrcNodeCode</p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 15:17:20
 */
public class SetSrcNodeCode implements Action {
    public int execute(Message msg) throws Exception {
    	TIPSDesOrgCode tipsDesOrgCode = new TIPSDesOrgCode();
        msg.set("//CFX/MSG/FreeFormat9105/SrcNodeCode",msg.getString("//CFX/MSG/FreeFormat9105/SendOrgCode"));
        msg.set("//CFX/MSG/FreeFormat9105/RcvOrgCode",tipsDesOrgCode.getgetTIPSDesOrgCode());
        return SUCCESS;
    }
}
