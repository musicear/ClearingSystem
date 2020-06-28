package resoft.junit;

import java.io.IOException;
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
public class TestDBUtil {
	 public static void main(String[] args) {
    	try {
		 Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        String sql = "delete from table_test";
        System.out.println("sql=" + sql);
        DBUtil.executeUpdate(sql);
        
        sql ="insert into table_test(a,b,c) values(#ab#,#b#,#c#)";
        System.out.println("sql=" + sql);
        DBUtil.executeUpdate(sql,new HashMap(){{
            put("b",new Date());
            put("ab","ÑîÔ¶Ðø");
            put("c",new Double(268.18));
        }});
        
        sql = "select a from table_test";
        System.out.println("sql=" + sql);
        String name = DBUtil.queryForString( sql );
        System.out.println("name=" + name);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
