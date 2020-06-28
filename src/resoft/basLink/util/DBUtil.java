package resoft.basLink.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.DatabaseHelper;

import resoft.xlink.comm.helper.ThreadLocalContext;

/**
 * function: 数据库操作辅助类
 *   操，所有操作小写变大写，写不了小写字母，MMP
 * User: albert lee
 * Date: 2005-10-27
 * Time: 18:18:17
 */
public class DBUtil {
    private static Log logger = LogFactory.getLog(DBUtil.class);


    /**
     * 得到数据库连接。从ThreadLocal中
     *
     * @return Connection
     */
    public static Connection getCurrentConnection() {
        Connection conn = (Connection) ThreadLocalContext.getInstance().getContext().getProperty("java.sql.Connection");
        if (conn == null) {
            throw new RuntimeException("没有数据库连接");
        }
        try {
			if (conn.isClosed()) {
				//logger.info("================>由于连接被关闭,因此重新获得新的连接!");
				ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", getNewConnection());
				return (Connection) ThreadLocalContext.getInstance().getContext().getProperty("java.sql.Connection");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return conn;
    }

    /**
     * 得到新的连接
     *
     * @return Connection
     */
    public static Connection getNewConnection() {        
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            throw new RuntimeException("没有数据库连接");
        }
        return conn;
    }

    /**
     * 查询sql语句，得到字符串的结果
     *
     * @param sql String
     * @return String 结果
     * @throws java.sql.SQLException 数据库连接失败或查询失败
     */
    public static String queryForString(String sql) throws SQLException {
        Connection conn = getCurrentConnection();
        Statement stmt = null;
        ResultSet rs = null;

        if (conn == null) {
            throw new SQLException("得到数据库连接错误");
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toUpperCase());
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "";
            }
        } catch (SQLException e) {
        	  logger.error("执行错误:" + sql.toUpperCase(), e);
              throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(rs, stmt);
        }
    }

    /**
     * <p>查询单条sql得到查询结果</p>
     * @param sql  要查询的语句
     * @throws java.sql.SQLException 当数据库连接不存在是提示错误
     * @return int 查询结果
     * */
    public static int queryForInt(String sql) throws SQLException {
        String value = queryForString(sql);
        return Integer.parseInt(value);
    }

    /**
     * 执行sql语句并提交
     *
     * @param sql 查询语句
     */
    public static void executeUpdate(String sql) {
        Connection conn = getCurrentConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql.toUpperCase());
            conn.commit();
        } catch (SQLException e) {
            logger.error("执行错误:" + sql.toUpperCase(), e);
            throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(null, stmt);
        }

    }

    /**
     * 执行sql语句
     *
     * @param sql    查询语句
     * @param params 参数列表
     */
    public static void executeUpdate(String sql, Map params) {
        Pattern pattern = Pattern.compile("\\#\\w+\\#");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer newSql = new StringBuffer();
        int startIndex, endIndex = 0;
        while (matcher.find()) {
            startIndex = matcher.start();
            //加上参数前数据
            newSql.append(sql.substring(endIndex, startIndex));
            //
            endIndex = matcher.end();
            String param = matcher.group();
            param = param.substring(1, param.length() - 1);

            Object obj = params.get(param);
            //判断类型。若是字符串则添加单引号，若是数值型则不添加，若是日期型则设置转型函数
            String value;
            if (obj instanceof Number) {
                value = obj.toString();
            } else if (obj instanceof java.util.Date) {
                DateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
                value = "to_date('" + df.format(obj) + "','yyyymmdd hh24:mi:ss')"; // for oracle,tzy
            } else {
                value = "'" + obj + "'";
            }
            newSql.append(value);
            //System.out.println(param);
        }
        newSql.append(sql.substring(endIndex, sql.length()));

//System.out.println(newSql.toString());

        logger.info("执行语句:" + newSql.toString());
        executeUpdate(newSql.toString());
    }

    /**
     * 执行数据插入操作
     * */
    public static void insert(String tableName,Map params) {
        StringBuffer sql = new StringBuffer("insert into ");
        sql.append(tableName);
        StringBuffer sqlColumns = new StringBuffer();
        StringBuffer sqlValues = new StringBuffer();
        for(Iterator itr = params.keySet().iterator();itr.hasNext();) {
            String column =  itr.next().toString();
            Object value = params.get(column);
            if(value==null) {
                value = "null";
            }
            if(!sqlColumns.toString().equals("")) {
                sqlColumns.append(",");
                sqlValues.append(",");
            }
            sqlColumns.append(column);
            if(value instanceof Number) {
                sqlValues.append(value.toString());
            } else {
                sqlValues.append("\'").append(value.toString()).append("\'");
            }
        }
        sql.append(" (").append(sqlColumns).append(" ) values(").append(sqlValues).append(")");
        executeUpdate(sql.toString());
    }

    /**
     * 执行多条sql语句并提交
     *
     * @param sqlArray 查询语句的数组
     * @throws java.sql.SQLException 数据库连接失败抛出异常
     */
    public static void executeUpdate(String[] sqlArray) throws SQLException {
        Connection conn = getCurrentConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (int i = 0; i < sqlArray.length; i++) {
                stmt.executeUpdate(sqlArray[i].toUpperCase());
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            logger.error("执行错误:" + sqlArray, e);
            throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(null, stmt);
        }

    }

}
