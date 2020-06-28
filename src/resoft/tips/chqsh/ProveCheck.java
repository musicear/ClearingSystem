package resoft.tips.chqsh;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


public class ProveCheck implements Action {
    public int execute(Message msg) throws Exception {
        String Flag = msg.getString("Flag"); 
        System.out.println("the Type is:"+Flag);
        if(Flag.equals("0")) {
        	return 0;
        } else {
            return 1;
        }                          
    }
}
