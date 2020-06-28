package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检查对账类型。若为日间，则不与行内对账</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 0:22:46
 */
public class CheckChkType implements Action {
    public int execute(Message msg) throws Exception {
        String chkAcctType = msg.getString("ChkAcctType"); 
        System.out.println("the chkAcctType is:"+chkAcctType);
        if(chkAcctType.equals("0")) {
            //日间状态
            return 0;//FAIL;
        } else {
            return 1;//SUCCESS;
        }                          
    }
}
