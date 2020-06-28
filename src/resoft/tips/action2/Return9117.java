package resoft.tips.action2;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class Return9117 implements Action{

	public int execute(Message msg) throws Exception {
		// TODO Auto-generated method stub
		/*
		String xml = (String)msg.get("packet");
		Packager packager = new GenericXmlPackager();
		Message returnMsg = packager.unpack(xml.getBytes());
		String Result = returnMsg.getString("//CFX/MSG/MsgReturn9121/Result");
	
		msg.set("Result", Result);
		msg.set("AddWord", returnMsg.getString("//CFX/MSG/MsgReturn9121/AddWord"));
	    */
		msg.set("Result", "90000");
		msg.set("AddWord", "Success");
		return 0;
	}

}
