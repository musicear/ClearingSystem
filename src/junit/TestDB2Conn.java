package junit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-8-9
 * Time: 9:08:58
 */
public class TestDB2Conn {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        Connection conn = DriverManager.getConnection("jdbc:db2://127.0.0.1:50000/EMLUATDB","db2admin","db2admin");
        Statement stmt = conn.createStatement();
        String sql = "select modules.id module_id,modules.name module_name,modules.url module_url,moduleGroups.id group_id,moduleGroups.name groupName" +
                " from modules,moduleGroups,permissions" +
                " where modules.group_id = moduleGroups.id and" +
                " modules.id = permissions.module_id and" +
                " permissions.user_id ='admin'" +
                " order by modules.group_id";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            String name = rs.getString("module_name");
            System.out.println(name);
        }
        System.out.println(conn);
    }
}
