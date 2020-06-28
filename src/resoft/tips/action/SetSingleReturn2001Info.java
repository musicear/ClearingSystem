package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置实时扣税的回执信息</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 17:38:08
 */
public class SetSingleReturn2001Info implements Action {
	private static final Log logger = LogFactory.getLog(SetSingleReturn2001Info.class);
    public int execute(Message msg) throws Exception {
    	logger.info("get3001pack");
        msg.set("//CFX/MSG/SingleReturn2001/OriTaxOrgCode",msg.get("//CFX/MSG/RealHead3001/TaxOrgCode"));
        msg.set("//CFX/MSG/SingleReturn2001/OriTraNo",msg.get("//CFX/MSG/RealHead3001/TraNo"));
        msg.set("//CFX/MSG/SingleReturn2001/OriEntrustDate",msg.get("//CFX/MSG/RealHead3001/EntrustDate"));
        msg.set("//CFX/MSG/SingleReturn2001/TaxVouNo",msg.get("//CFX/MSG/Payment3001/TaxVouNo"));
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        msg.set("//CFX/MSG/SingleReturn2001/TaxDate",df.format(new Date()));
        return SUCCESS;
    }
}
