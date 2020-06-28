package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>返回ACE报文</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 08:00:00
 */
public class ACEReturn implements Action {
    private static final Log logger = LogFactory.getLog(ACEReturn.class);
    
    public int execute(Message msg) throws Exception {
    	//返回ACEPack信息
    	String packInfo=msg.getString("ACEPack");
    	ACEPackager acePack=new ACEPackager(packInfo);
    	msg.set("ACEObj", acePack);	
    	
    	//设置ACE报文头
    	msg.set("ACETransHead", acePack.packTransHead);
    	//设置ACE报文体
    	msg.set("ACETrandBody", msg.get("ACETrandBody"));
    	//设置ACE包
    	msg.set("ACEPack", "");
    	
    	logger.info("---------------结束ACE--------------");
    	return SUCCESS;
    }
    
}