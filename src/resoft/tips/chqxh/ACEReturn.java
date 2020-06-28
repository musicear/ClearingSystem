package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>����ACE����</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 08:00:00
 */
public class ACEReturn implements Action {
    private static final Log logger = LogFactory.getLog(ACEReturn.class);
    
    public int execute(Message msg) throws Exception {
    	//����ACEPack��Ϣ
    	String packInfo=msg.getString("ACEPack");
    	ACEPackager acePack=new ACEPackager(packInfo);
    	msg.set("ACEObj", acePack);	
    	
    	//����ACE����ͷ
    	msg.set("ACETransHead", acePack.packTransHead);
    	//����ACE������
    	msg.set("ACETrandBody", msg.get("ACETrandBody"));
    	//����ACE��
    	msg.set("ACEPack", "");
    	
    	logger.info("---------------����ACE--------------");
    	return SUCCESS;
    }
    
}