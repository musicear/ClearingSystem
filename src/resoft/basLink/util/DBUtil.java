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
 * function: ���ݿ����������
 *   �٣����в���Сд���д��д����Сд��ĸ��MMP
 * User: albert lee
 * Date: 2005-10-27
 * Time: 18:18:17
 */
public class DBUtil {
    private static Log logger = LogFactory.getLog(DBUtil.class);


    /**
     * �õ����ݿ����ӡ���ThreadLocal��
     *
     * @return Connection
     */
    public static Connection getCurrentConnection() {
        Connection conn = (Connection) ThreadLocalContext.getInstance().getContext().getProperty("java.sql.Connection");
        if (conn == null) {
            throw new RuntimeException("û�����ݿ�����");
        }
        try {
			if (conn.isClosed()) {
				//logger.info("================>�������ӱ��ر�,������»���µ�����!");
				ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", getNewConnection());
				return (Connection) ThreadLocalContext.getInstance().getContext().getProperty("java.sql.Connection");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return conn;
    }

    /**
     * �õ��µ�����
     *
     * @return Connection
     */
    public static Connection getNewConnection() {        
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            throw new RuntimeException("û�����ݿ�����");
        }
        return conn;
    }

    /**
     * ��ѯsql��䣬�õ��ַ����Ľ��
     *
     * @param sql String
     * @return String ���
     * @throws java.sql.SQLException ���ݿ�����ʧ�ܻ��ѯʧ��
     */
    public static String queryForString(String sql) throws SQLException {
        Connection conn = getCurrentConnection();
        Statement stmt = null;
        ResultSet rs = null;

        if (conn == null) {
            throw new SQLException("�õ����ݿ����Ӵ���");
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
        	  logger.error("ִ�д���:" + sql.toUpperCase(), e);
              throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(rs, stmt);
        }
    }

    /**
     * <p>��ѯ����sql�õ���ѯ���</p>
     * @param sql  Ҫ��ѯ�����
     * @throws java.sql.SQLException �����ݿ����Ӳ���������ʾ����
     * @return int ��ѯ���
     * */
    public static int queryForInt(String sql) throws SQLException {
        String value = queryForString(sql);
        return Integer.parseInt(value);
    }

    /**
     * ִ��sql��䲢�ύ
     *
     * @param sql ��ѯ���
     */
    public static void executeUpdate(String sql) {
        Connection conn = getCurrentConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql.toUpperCase());
            conn.commit();
        } catch (SQLException e) {
            logger.error("ִ�д���:" + sql.toUpperCase(), e);
            throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(null, stmt);
        }

    }

    /**
     * ִ��sql���
     *
     * @param sql    ��ѯ���
     * @param params �����б�
     */
    public static void executeUpdate(String sql, Map params) {
        Pattern pattern = Pattern.compile("\\#\\w+\\#");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer newSql = new StringBuffer();
        int startIndex, endIndex = 0;
        while (matcher.find()) {
            startIndex = matcher.start();
            //���ϲ���ǰ����
            newSql.append(sql.substring(endIndex, startIndex));
            //
            endIndex = matcher.end();
            String param = matcher.group();
            param = param.substring(1, param.length() - 1);

            Object obj = params.get(param);
            //�ж����͡������ַ�������ӵ����ţ�������ֵ������ӣ�����������������ת�ͺ���
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

        logger.info("ִ�����:" + newSql.toString());
        executeUpdate(newSql.toString());
    }

    /**
     * ִ�����ݲ������
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
     * ִ�ж���sql��䲢�ύ
     *
     * @param sqlArray ��ѯ��������
     * @throws java.sql.SQLException ���ݿ�����ʧ���׳��쳣
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
            logger.error("ִ�д���:" + sqlArray, e);
            throw new RuntimeException(e);
        } finally {
            DatabaseHelper.clearup(null, stmt);
        }

    }

}
