package org.zerone.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * sql查询
 * User: Albert Lee
 * Date: 2005-12-18
 * Time: 0:03:27
 */
public class QueryUtil {
    private static final Log logger = LogFactory.getLog(QueryUtil.class);
    /**
     * 根据sql查询，查询结果存入一二维数组
     * @deprecated 此方法返回的是一个二维数组，若列较多时，用1、2、3等数字表示实在不可读
     * */
    public static List query(String sql) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        sql=sql.toUpperCase();
        if(conn==null) {
            throw new SQLException("未找到数据库连接");
        }
        List rowSet = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while(rs.next()) {
                List line = new ArrayList();
                for(int i=1;i<=columnsCount;i++) {
                    line.add(rs.getString(i));
                }
                rowSet.add(line);
            }
        } catch(SQLException e) {
            logger.error("查询失败:sql=" + sql + ".sqlcode=" + e.getErrorCode() + ".sqlstate=" + e.getSQLState(),e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }
        return rowSet;
    }
    /**
     * 根据sql查询，查询结果存入一二维数组。各行表示为一个map，key为列名，value为值。若为null，则用""表示
     * */
    public static List queryRowSet(String sql) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        sql=sql.toUpperCase();
        if(conn==null) {
            throw new SQLException("未找到数据库连接");
        }
        List rowSet = new ArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while(rs.next()) {
                Map row = new IgnoreCaseMap();
                for(int i=1;i<=columnsCount;i++) {
                    String key = rsmd.getColumnName(i);
                    String value = rs.getString(i);
                    if(value==null) {
                        value = "";
                    }
                    row.put(key,value);
                }
                rowSet.add(row);
            }
        } catch(SQLException e) {
            logger.error("查询失败:sql=" + sql,e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }
        return rowSet;
    }
    /**
     * 查询sql语句，并将结果返回为xml形式。如
     * <Result>
     *   <Row>
     *     <dfds>...</dfds>
     *     <dfds>...</dfds>
     *   </Row>
     *   <....>
     * </Result>
     * */
    public static String exportXml(String sql) throws SQLException {
    	sql=sql.toUpperCase();
        List rowSet = queryRowSet(sql);
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"GB18030\"?>");
        xml.append("<Result>");
        for(Iterator itr = rowSet.iterator();itr.hasNext();) {
            Map row = (Map) itr.next();
            xml.append("<Row>");
            for(Iterator itrRow = row.keySet().iterator();itrRow.hasNext();) {
                String key = (String) itrRow.next();
                String value = (String) row.get(key);
                xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
            }
            xml.append("</Row>");
        }
        xml.append("</Result>");
        return xml.toString();
    }

    /**
     * 根据sql查询，查询结果只是一条记录，存入Map中。key为列名，value为值。若为null，则用""表示
     * */
//    public static Map queryForMap(String sql) throws SQLException {
//        List list = queryRowSet(sql);
//        if(list.size()==0) {
//            return new HashMap();
//        }
//    }

}
/**
 * 新建Map。此Map将对Key的大小写不区分。用于QueryUtil。因其将每行的字段名与值建立一个Map。而数据库字段名不区分大小写
 * */
class IgnoreCaseMap implements Map {
    public IgnoreCaseMap() {
        internalMap = new HashMap();
    }
    public int size() {
        return internalMap.size();
    }

    public void clear() {
        internalMap.clear();
    }

    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    public Collection values() {
        return internalMap.values();
    }

    public void putAll(Map t) {
        for(Iterator itr = t.keySet().iterator();itr.hasNext();) {
            String key = (String) itr.next();
            Object value = t.get(key);
            put(key,value);
        }
    }

    public Set entrySet() {
        return internalMap.entrySet();
    }

    public Set keySet() {
        return internalMap.keySet();
    }

    public Object get(Object key) {
        String lowerKey = key.toString().toLowerCase();
        return internalMap.get(lowerKey);
    }

    public Object remove(Object key) {
        String lowerKey = key.toString().toLowerCase();
        return internalMap.remove(lowerKey);
    }

    public Object put(Object key, Object value) {
        String lowerKey = key.toString().toLowerCase();
        return internalMap.put(lowerKey,value);
    }

    private Map internalMap ;
}