package resoft.tips.action;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置报文标识号</p>
 *
 * @Author:
 * Date: 2007-5-13
 * Time: 1:41:02
 */
public class SetMsgId implements Action {
	
	private static final Log logger = LogFactory.getLog(SetMsgId.class);
    public int execute(Message msg) throws Exception {
        //规则编制为"日期" + 6位随机数
    try{
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String id = df.format(new Date());
        NumberFormat nf = new DecimalFormat("000000");
        id += nf.format(Math.random() * 1000000);  
        
        logger.info("id is : " + id);
        msg.set("//CFX/HEAD/MsgID",id);
        msg.set("//CFX/HEAD/MsgRef",id);
    }catch(Exception ex){
    	return FAIL;
    }
        return SUCCESS;
    }
}

