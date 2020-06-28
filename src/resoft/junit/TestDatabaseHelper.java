package resoft.junit;

import java.sql.Connection;

import org.zerone.jdbc.DatabaseHelper;

import junit.framework.TestCase;

/**
 * function: ≤‚ ‘DatabaseHelper
 * User: albert lee
 * Date: 2005-10-26
 * Time: 16:25:41
 */
public class TestDatabaseHelper extends TestCase{
    public void testConnectionPool() throws Exception {
        Connection first = DatabaseHelper.getConnection();
        first.close();
        Connection second = DatabaseHelper.getConnection();
        second.close();
        //assertTrue(first==second);
    }
}
