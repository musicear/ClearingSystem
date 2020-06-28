package resoft.tips.action;

//import java.util.HashMap;
//import java.util.Map;

import resoft.basLink.util.DBUtil;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class UpdateReturnDeclareInfo implements Action {
    public int execute(Message msg) throws Exception {
        String oriTaxOrgCode = msg.getString("OriTaxOrgCode");
        String oriEntrustDate = msg.getString("OriEntrustDate");
        String oriTraNo = msg.getString("OriTraNo");
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
  
        String result = returnMsg.getString("//CFX/Head/MsgReturn9120/Result");
        String cheDate = returnMsg.getString("//CFX/Head/WorkDate");
        if(result.equals("90000")) {
        String sql = "update DeclareInfo set chkDate='" + cheDate + "' where traNo='"+oriTraNo+"' and taxOrgCode='"+oriTaxOrgCode+"' and entrustDate='"+oriEntrustDate+"'";
        DBUtil.executeUpdate(sql);
        
    }
        return SUCCESS;
}
}