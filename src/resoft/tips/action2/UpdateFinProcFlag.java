package resoft.tips.action2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>更新支付令包表中处理状态</p>
 * Date: 2009-3-13
 * Time: 0:29:45
 */
public class UpdateFinProcFlag implements Action {
    public int execute(Message msg) throws Exception {
    	String packID=msg.getString("PackID");
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String sql="update PayOrderPack set procstatus='2',PROCENDTIME='" + df.format(new Date()) + "'"+ "where ID="+packID;      
       DBUtil.executeUpdate(sql); 
       System.out.println(sql);
        return SUCCESS;
    }
}
