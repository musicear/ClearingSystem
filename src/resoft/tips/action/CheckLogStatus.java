package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����¼״̬</p>
 * Author:liwei
 * Date: 2007-07-10
 * Time: 16:59:00
 */
public class CheckLogStatus implements Action {
    public int execute(Message msg) throws Exception {
        String logStatus = DBUtil.queryForString("select loginStatus from SysStatus");
        if(logStatus.equals("1")) {//δ��¼�ɹ�
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}
