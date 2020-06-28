/*
 * Created on 2009-3-10
 *
 */
package resoft.tips.action2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * @author haoruibing
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckPackToPay implements Action{
	 private static final Log logger = LogFactory.getLog(CheckPackToPay.class);
	/** 
	 * 轮训要处理的支付包
	 */
	public int execute(Message msg) throws Exception {
		 List rowSet = QueryUtil.queryRowSet("select ID from payorderpack where procstatus='0'");
	        if(rowSet.size()>0) {
	            Map row = (Map) rowSet.get(0);
	            String packID = (String) row.get("ID");
	            //设置为正在处理中
	            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            String sql = "update payorderpack set ProcStatus='1',procBeginTime='" + df.format(new Date()) + "'" +
	                    " where ID='" + packID + "'";
	            DBUtil.executeUpdate(sql);
	            msg.set("PackID",packID);
	            return SUCCESS;
	        }
	        return FAIL;
	}

}
