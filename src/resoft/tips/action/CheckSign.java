package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���ǩ��</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 12:16:48
 */
public class CheckSign implements Action {
    public int execute(Message msg) throws Exception {
        Boolean checkSignResult = (Boolean) msg.get("��Ѻ���");
        System.out.println("��Ѻ��� is.........:"+checkSignResult);
        if(!checkSignResult.booleanValue()) {
            //��Ѻʧ��
            msg.set(resultNodePath,"91005");
            msg.set(addWordNodePath,"У�����ǩ����");
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
