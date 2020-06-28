package resoft.junit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.zerone.jdbc.DatabaseHelper;

/**
 * <p>function:</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-21</p>
 * <p>Time: 11:28:35</p>
 */
public class ResultSetTest {
    public static void main(String[] args) throws Exception{
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("select * from Test");
        while(rs.next()) {
            System.out.println(rs.getString(1));
            Thread.sleep(10 * 1000);
        }
        DatabaseHelper.clearup(rs,stmt,conn);

    }
}
