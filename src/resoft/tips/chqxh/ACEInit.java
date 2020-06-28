package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>初始化ACE报文</p>
 * Author: liwei
 * Date: 2007-08-08
 * Time: 17:54:00
 */
public class ACEInit implements Action {
    private static final Log logger = LogFactory.getLog(ACEInit.class);
    
    public int execute(Message msg) throws Exception {
    	logger.info("---------------初始化ACE--------------");
    	//初始化ACEPack信息
    	String packInfo=msg.getString("ACEPack");
    	ACEPackager acePack=new ACEPackager(packInfo);
    	
    	if (acePack.transCode.equals("2008")) {//三方协议录入
    		acePack=new ACE2008(packInfo);
    	}else if(acePack.transCode.equals("2009")){//三方协议查询
    		acePack=new ACE2009(packInfo);
    	}else if(acePack.transCode.equals("2010")){//三方协议删除
    		acePack=new ACE2010(packInfo);
    	}else if(acePack.transCode.equals("2006")){//税票打印
    		acePack=new ACE2006(packInfo);
    	}
    	
    	//初始化ACE交易报文头
    	acePack.makeTransHead();
    	msg.set("ACEObj", acePack);					
    	return SUCCESS;
    }
    
}
