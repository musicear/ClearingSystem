package resoft.tips.action;

//import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����"������˰Ʊ��ϸ���˻�ִ"��Ϣ</p>
 * Author: liguoyin
 * Date: 2007-8-12
 * Time: 17:26:18
 * Update by zhuchangwu
 */
public class SetPayCheckReturn implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("//CFX/MSG/MsgReturn2111/OriPayBankNo",msg.getString("PayBkCode"));
        msg.set("//CFX/MSG/MsgReturn2111/OriChkDate",msg.getString("ChkDate"));
        msg.set("//CFX/MSG/MsgReturn2111/OriChkAcctOrd",msg.getString("ChkAcctOrd"));
        msg.set("//CFX/MSG/MsgReturn2111/OriPayeeBankNo",msg.getString("PayeeBankNo"));
        msg.set("//CFX/MSG/MsgReturn2111/Result","90000");
        msg.set("//CFX/MSG/MsgReturn2111/AddWord","���׳ɹ�");
//        String sql="select count(*) from PayCheckDetail where succCheck='N' and chkDate='"+msg.getString("ChkDate")+" and chkAcctOrd='"+msg.getString("ChkAcctOrd")+"'";
//        int cnt = DBUtil.queryForInt(sql);
//        if(cnt==0){
//          msg.set("//CFX/MSG/MsgReturn2111/Result","90000");
//          msg.set("//CFX/MSG/MsgReturn2111/AddWord","���׳ɹ�");
//        }else{
//          msg.set("//CFX/MSG/MsgReturn2111/Result","24030");
//          msg.set("//CFX/MSG/MsgReturn2111/AddWord","���ʲ���");
//        }

        return SUCCESS;
    }
}
