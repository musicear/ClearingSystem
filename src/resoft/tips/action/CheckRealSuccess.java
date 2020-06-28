package resoft.tips.action;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���ʵʱ��˰�����Ƿ��ѿۿ�ɹ�</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 12:33:03
 */
public class CheckRealSuccess implements Action {
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
        String entrustDate = msg.getString("//CFX/MSG/RealHead3001/EntrustDate");
        String traNo = msg.getString("//CFX/MSG/RealHead3001/TraNo");
        String sql = "select count(*) from RealtimePayment where result='90000' and ";
        sql += " taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and traNo='" + traNo + "'";
        System.out.println("���ʵʱ��˰�����Ƿ��ѿۿ�ɹ� sql is.............:"+sql);
        int cnt = DBUtil.queryForInt(sql);
        if(cnt==0) {
            return SUCCESS;
        } else {
            //�ѿۿ�ɹ�
            msg.set(resultNodePath,"94051");
            msg.set(addWordNodePath,"�����ظ�");
            return FAIL;
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
