package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import resoft.basLink.util.DBUtil;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import resoft.xlink.comm.Packager;
//import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>保存自由格式报文</p>
 * Author: liwei
 * Date: 2007-07-10
 * Time: 15:06:06
 */
public class SaveDB9105 implements Action {
    
//	private static final Log logger = LogFactory.getLog(SaveDB9105.class);
    
    public int execute(Message msg) throws Exception {
                
        String workDate=msg.getString("//CFX/HEAD/WorkDate");
        String msgId=msg.getString("//CFX/HEAD/MsgID");
        String content=msg.getString("//CFX/MSG/FreeFormat9105/Content");
        String srcNodeCode=msg.getString("//CFX/MSG/FreeFormat9105/SrcNodeCode");
        Map params=new HashMap();
        params.put("workDate",workDate);
        params.put("msgId",msgId);
        params.put("content",content);        
        params.put("SrcNodeCode",srcNodeCode);
        DateFormat dtFormat = new SimpleDateFormat("yyyyMMddkkmmss");
        params.put("recvTime",dtFormat.format(new Date()));
        
        DBUtil.insert("FreeMessage",params);
        return SUCCESS;
    }
}