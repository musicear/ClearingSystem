package resoft.tips.action;


import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class SetRepeat2111Return  implements Action {
    public int execute(Message msg) throws Exception {
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
  
        String result = returnMsg.getString("//CFX/Head/MsgReturn9120/Result");
        if(result.equals("90000")) 
        	return FAIL;
        else
        	return SUCCESS;
}
}
