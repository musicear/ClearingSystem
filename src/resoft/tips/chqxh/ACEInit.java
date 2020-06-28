package resoft.tips.chqxh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>��ʼ��ACE����</p>
 * Author: liwei
 * Date: 2007-08-08
 * Time: 17:54:00
 */
public class ACEInit implements Action {
    private static final Log logger = LogFactory.getLog(ACEInit.class);
    
    public int execute(Message msg) throws Exception {
    	logger.info("---------------��ʼ��ACE--------------");
    	//��ʼ��ACEPack��Ϣ
    	String packInfo=msg.getString("ACEPack");
    	ACEPackager acePack=new ACEPackager(packInfo);
    	
    	if (acePack.transCode.equals("2008")) {//����Э��¼��
    		acePack=new ACE2008(packInfo);
    	}else if(acePack.transCode.equals("2009")){//����Э���ѯ
    		acePack=new ACE2009(packInfo);
    	}else if(acePack.transCode.equals("2010")){//����Э��ɾ��
    		acePack=new ACE2010(packInfo);
    	}else if(acePack.transCode.equals("2006")){//˰Ʊ��ӡ
    		acePack=new ACE2006(packInfo);
    	}
    	
    	//��ʼ��ACE���ױ���ͷ
    	acePack.makeTransHead();
    	msg.set("ACEObj", acePack);					
    	return SUCCESS;
    }
    
}
