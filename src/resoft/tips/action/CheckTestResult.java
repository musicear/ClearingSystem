package resoft.tips.action;
import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>检查连接请求返回结果</p>
 * Author: zhuchangwu
 * Date: 2007-8-10
 * Time: 17:55:07
 */
public class CheckTestResult implements Action {
    public int execute(Message msg) throws Exception {
    	try{
	        String xml = (String) msg.get("packet");
	        Packager packager = new GenericXmlPackager();
	        Message returnMsg = packager.unpack(xml.getBytes());
	        String result = returnMsg.getString("//CFX/MSG/MsgReturn9120/Result");
	        if("90000".equals(result)) {
	        	DBUtil.executeUpdate("update ConnTest set ConnFlag='Y',ReciveTime='"+DateTimeUtil.getDateTimeString()+"' ");
	            msg.set("Result","Y");
	        } else {
	            msg.set("Result","N");
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    		DBUtil.executeUpdate("update ConnTest set ConnFlag='N' ");
    	}
    	
        return SUCCESS;
    }
}
