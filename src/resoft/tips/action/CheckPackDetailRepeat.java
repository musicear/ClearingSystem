package resoft.tips.action;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>包明细请求返回结果</p>
 * Author: zhuchangwu
 * Date: 2007-8-10
 * Time: 17:55:07
 */
public class CheckPackDetailRepeat implements Action {
    public int execute(Message msg) throws Exception {
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
        String result = returnMsg.getString("//CFX/MSG/MsgReturn9121/Result");
        if("90000".equals(result)) {
        	
            msg.set("Result","Y");
        } else {
            msg.set("Result","N");
        }
        msg.set("AddWord", returnMsg.getString("//CFX/MSG/MsgReturn9121/AddWord"));
        return SUCCESS;
    }
    }
