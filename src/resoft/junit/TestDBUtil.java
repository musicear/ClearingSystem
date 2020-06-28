package resoft.junit;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import resoft.basLink.util.DBUtil;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-4-27
 * Time: 17:04:02
 */
public class TestDBUtil extends TestCase {
    public void testExecuteUpdate() {
    	Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        String sql = "insert into table_test(a,b,c) values(#ab#,#b#,#c#)";
        DBUtil.executeUpdate(sql,new HashMap(){{
            put("b",new Date());
            put("ab","Albert");
            put("c",new Double(268));
        }});
    }
}
