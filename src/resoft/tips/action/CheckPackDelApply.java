package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����������Ƿ���ֹ��</p>
 * Author: liguoyin
 * Date: 2007-6-12
 * Time: 18:58:56
 */
public class CheckPackDelApply implements Action {
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode");
        String entrustDate = msg.getString("EntrustDate");
        String packNo = msg.getString("PackNo");
        String sql = "select count(*) from DelApply " +
                " where stopType='1' and taxOrgCode='" + taxOrgCode + "' and oriEntrustDate='" + entrustDate + "' and oriPackNo='" + packNo + "'";
        int cnt = DBUtil.queryForInt(sql);
        if(cnt>0) {
            msg.set("ֹ����־",Boolean.TRUE);
            msg.set("Result", "24020");
            msg.set("AddWord", "���Ѿ�ֹ��");
            //��ֹ��
            return FAIL;
        }
        return SUCCESS;
    }
}
