package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>根据银行核心返回设置返回TIPS的2001回执信息</p>
 * Author: liguoyin
 * Date: 2007-8-23
 * Time: 12:35:16
 */
public class SetTips2001Response implements Action {
	private static final Log logger = LogFactory.getLog(SetTips2001Response.class);
    public int execute(Message msg) throws Exception {

    	DateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String traDate=df.format(new Date()); 
        msg.set("//CFX/MSG/SingleReturn2001/Result",msg.getString("Result"));
        msg.set("//CFX/MSG/SingleReturn2001/AddWord",msg.getString("AddWord"));
        msg.set("//CFX/MSG/SingleReturn2001/TaxDate",msg.getString("bankTraDate")==null||"".equals(msg.getString("bankTraDate"))?traDate:msg.getString("bankTraDate"));
       // msg.set("//CFX/MSG/SingleReturn2001/TaxDate",msg.get("bankTraDate"));
        
        return SUCCESS;
    }
}
