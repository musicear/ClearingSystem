package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���������͡���Ϊ�ռ䣬�������ڶ���</p>
 * Author: liguoyin
 * Date: 2007-8-17
 * Time: 0:22:46
 */
public class CheckChkType implements Action {
    public int execute(Message msg) throws Exception {
        String chkAcctType = msg.getString("ChkAcctType"); 
        System.out.println("the chkAcctType is:"+chkAcctType);
        if(chkAcctType.equals("0")) {
            //�ռ�״̬
            return 0;//FAIL;
        } else {
            return 1;//SUCCESS;
        }                          
    }
}
