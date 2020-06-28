package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>检测签名</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 12:16:48
 */
public class CheckSign implements Action {
    public int execute(Message msg) throws Exception {
        Boolean checkSignResult = (Boolean) msg.get("核押结果");
        System.out.println("核押结果 is.........:"+checkSignResult);
        if(!checkSignResult.booleanValue()) {
            //核押失败
            msg.set(resultNodePath,"91005");
            msg.set(addWordNodePath,"校验电子签名错");
            return FAIL;
        } else {
            return SUCCESS;
        }                          
    }

    public void setResultNodePath(String resultNodePath) {
        this.resultNodePath = resultNodePath;
    }

    public void setAddWordNodePath(String addWordNodePath) {
        this.addWordNodePath = addWordNodePath;
    }

    private String resultNodePath,addWordNodePath;
}
