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
 * sql��ѯ
 * User: Albert Lee
 * Date: 2005-12-18
 * Time: 0:03:27
 */
public class QueryUtil {
    private static final Log logger = LogFactory.getLog(QueryUtil.class);
    /**
     * ����sql��ѯ����ѯ�������һ��ά����
     * @deprecated �˷������ص���һ����ά���飬���н϶�ʱ����1��2��3�����ֱ�ʾʵ�ڲ��ɶ�
     * */
    public static List query(String sql) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        sql=sql.toUpperCase();
        if(conn==null) {
            throw new SQLException("δ�ҵ����ݿ�����");
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
            logger.error("��ѯʧ��:sql=" + sql + ".sqlcode=" + e.getErrorCode() + ".sqlstate=" + e.getSQLState(),e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }
        return rowSet;
    }
    /**
     * ����sql��ѯ����ѯ�������һ��ά���顣���б�ʾΪһ��map��keyΪ������valueΪֵ����Ϊnull������""��ʾ
     * */
    public static List queryRowSet(String sql) throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        sql=sql.toUpperCase();
        if(conn==null) {
            throw new SQLException("δ�ҵ����ݿ�����");
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
            logger.error("��ѯʧ��:sql=" + sql,e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }
        return rowSet;
    }
    /**
     * ��ѯsql��䣬�����������Ϊxml��ʽ����
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
     * ����sql��ѯ����ѯ���ֻ��һ����¼������Map�С�keyΪ������valueΪֵ����Ϊnull������""��ʾ
     * */
//    public static Map queryForMap(String sql) throws SQLException {
//        List list = queryRowSet(sql);
//        if(list.size()==0) {
//            return new HashMap();
//        }
//    }

}
/**
 * �½�Map����Map����Key�Ĵ�Сд�����֡�����QueryUtil�����佫ÿ�е��ֶ�����ֵ����һ��Map�������ݿ��ֶ��������ִ�Сд
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