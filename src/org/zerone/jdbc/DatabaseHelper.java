package org.zerone.jdbc;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.common.BeanFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * User: Albert Lee
 * Date: 2004-5-24
 * Time: 0:03:27
 */
public class DatabaseHelper {
    private static Log logger = LogFactory.getLog(DatabaseHelper.class);

    //���ݿ�����
    private static String dbms = "oracle";

    //���ݿ�JDBC������
    private static String driver = "";

    //���Ӵ�
    private static String url = "";

    //�û���
    private static String user = "";

    //����
    private static String password = "";

    //�����ݿ����Ӳ������
    private static Map sqlTestMap = new HashMap();

    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    static {
        sqlTestMap.put("oracle","SELECT 1 FROM DUAL");
        sqlTestMap.put("db2","SELECT 1 FROM sysibm.sysdummy1");
        sqlTestMap.put("sqlserver","SELECT 1");
        sqlTestMap.put("informix","SELECT COUNT(*) FROM SYSTABLES");
        sqlTestMap.put("sybase","SELECT 1");
        sqlTestMap.put("mysql","SELECT 1");
        ConnectionConfig infoReader = (ConnectionConfig) BeanFactory.getBean("dbConfig");
        new DatabaseHelper(infoReader.getDBMS(),infoReader.getDriver(), infoReader.getUrl(),
                infoReader.getUser(), infoReader.getPassword());

    }

    /**
     * �������ݿ�url,user,password
     */
    private DatabaseHelper(String dbms,String driver, String url, String user,
                           String password) {
        registerDriver(driver);
        DatabaseHelper.dbms = dbms;
        DatabaseHelper.url = url;
        DatabaseHelper.user = user;
        DatabaseHelper.password = password;

        setupDS();
    }

    private static void setupDS() {
//        ds = new PooledDataSource();
//        ds.setDriverClassName(driver);
//        ds.setUrl(url);
//        ds.setUsername(user);
//        ds.setPassword(password);
//        ds.setMaxActive(50);
//        ds.setDefaultAutoCommit(false);
//        String sql = (String) sqlTestMap.get(dbms);
//        if(sql==null) {
//            sql = "SELECT 1";
//        }
//        ds.setValidationQuery(sql);
    	try {
			ds.setDriverClass(driver);
			ds.setJdbcUrl(url);
			ds.setUser(user);
			ds.setPassword(password);
			// �����ӳ��е���������ʱ��C3POһ���Դ����µ�������Ŀ
			ds.setAcquireIncrement(5);
			// �����ڴ����ݿ��ȡ�µ�����ʧ�ܺ��ظ����Ի�ȡ�Ĵ�����Ĭ��Ϊ30
			ds.setAcquireRetryAttempts(30);
			// ���������м��ʱ��Ĭ��Ϊ1000����
			ds.setAcquireRetryDelay(1000);
			// ���ӹر�ʱĬ�Ͻ�����δ�ύ�Ĳ����ع� Ĭ��Ϊfalse
			ds.setAutoCommitOnClose(false);
			// ��ȡ����ʧ�ܽ����������еȴ���ȡ���ӵ��߳��쳣,��������Դ����Ч�ı���,�����´ε���getConnection()��ʱ��������Ի�ȡ����.�����Ϊtrue,��ô���Ի�ȡ����ʧ�ܺ������Դ�������Ѿ��Ͽ������ùر�.Ĭ��Ϊfalse
			ds.setBreakAfterAcquireFailure(false);
			// �����ӳ�����ʱ�ͻ��˵���getConnection()��ȴ���ȡ�����ӵ�ʱ��,��ʱ���׳�SQLException,����Ϊ0�������ڵȴ�.��λ����,Ĭ��Ϊ0
			ds.setCheckoutTimeout(0);
			// �����������������ӳ��еĿ�������,Ĭ��Ϊ0��ʾ�����
			ds.setIdleConnectionTestPeriod(0);
			//  ��ʼ��ʱ������������,Ӧ��minPoolSize��maxPoolSize֮��ȡֵ.Ĭ��Ϊ3
			ds.setInitialPoolSize(25);
			// ������ʱ��,��������ʱ������ӽ�������.Ϊ0����������������.Ĭ��Ϊ0
			ds.setMaxIdleTime(0);
			// �����Ϊtrue��ô��ȡ�����ӵ�ͬʱ��У�����ӵ���Ч��.Ĭ��Ϊfalse
			ds.setTestConnectionOnCheckin(true);
			// ���ӳ��б�������С��������
			ds.setMinPoolSize(5);
			// ���ӳ��б����������������.Ĭ��Ϊ15
			ds.setMaxPoolSize(100);
			// JDBC�ı�׼����,���Կ�������Դ�ڼ��ص�PreparedStatement����.������Ԥ�����Statement���ڵ���Connection�������������ӳ�.�����������������Ҫ���˵��෽�������,���maxStatements    
            // ��maxStatementsPerConnection��Ϊ0,�򻺴汻�ر�.Ĭ��Ϊ0
			ds.setMaxStatements(0);
			// ���ӳ��ڵ���������ӵ�е���󻺴汻�ر�.Ĭ��Ϊ0
			ds.setMaxStatementsPerConnection(0);
			// C3P0���첽������,������JDBC����ͨ�������������.��չ��Щ����������Ч����������,ͨ��������ʵ�ֶ������ͬʱ��ִ��.ĬΪΪ3
			ds.setNumHelperThreads(3);
			// �û��޸�ϵͳ���ò���ִ��ǰ���ȴ�������.Ĭ��Ϊ300
			//ds.setPropertyCycle(300);			
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
        logger.info("jdbc dbms:" + dbms);
        logger.info("jdbc driver:" + driver );
        logger.info("jdbc url:" + url);
        logger.info("jdbc username:" + user);
        logger.info("jdbc password:" + password);
//        logger.info("jdbc validationQuery:" + sql);
    }

    /**
     * ��ȡ�����ļ�
     */
    public DatabaseHelper() {
        //��ȡ��������
        String fileName = getClass().getName().replace('.', '/') + ".properties";

        //���ļ�����
        InputStream is = null;
        try {
            //�õ����ò���
            is = getClass().getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                logger.info("File not found.the name is " + fileName);
                throw new RuntimeException("File Not Found");
            }
            Properties prop = new Properties();
            prop.load(is);
            driver = (String) prop.get("driver");
            registerDriver(driver);
            url = (String) prop.get("url");
            user = (String) prop.get("user");
            password = (String) prop.get("password");

            setupDS();
        } catch (IOException e) {
            logger.info("cfg file not found");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.warn("Fail to close the file");
                }
            }
        }
    }


    /**
     * ע������
     */
    private void registerDriver(String driver) {
        DatabaseHelper.driver = driver;
        try {
            if (driver != null && !driver.equals("")) {
                Class.forName(driver);
            }
        } catch (ClassNotFoundException e) {
            logger.error(driver + " not found", e);
        }
    }

    /**
     * �õ����ݿ����Ӷ���Connection
     *
     * @return Connection
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
//            logger.info("��ǰ����ʹ�õ����ݿ�������Ϊ: " + ds.getNumBusyConnectionsDefaultUser() + "     " + "��ǰ������Ϊ: " + ds.getNumConnectionsDefaultUser());
        } catch (SQLException e) {
            logger.info("url=" + url + ";user=" + user + ";passwod=" + password);
            logger.info(e.getMessage());
        }
        return conn;

    }

    /**
     * ��ӡ���ӳ���Ϣ
     * */
    public static void printDS() {
        //System.out.println("number Active:" + ds.getNumActive());
        //System.out.println("number Idle:" + ds.getNumIdle());
    }

    /**
     * ִ��һ��sql���
     * */
    public static void executeUpdate(Connection conn,String sql) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql.toUpperCase());
            conn.commit();
        } catch(SQLException e) {
            logger.error("����ʧ��:sql=" + sql,e);
            throw e;
        } finally {
            clearup(null,stmt,null);
        }
    }

    /**
     * �������ݿ�������Դ
     */
    public static void clearup(ResultSet rs, PreparedStatement pstmt,
                               Connection conn) {
        //�ر�ResultSet
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //�ر�PreparedStatement
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //�ر�Connection
        if (conn != null) {
            try {
//            	logger.info("��ʼִ���ͷ�����,�ͷ�ǰʹ��������Ϊ:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "��ǰ������Ϊ:" + ds.getNumConnectionsDefaultUser());
                conn.close();
//                logger.info("�ͷ�����ִ�����,�ͷź�ʹ��������Ϊ:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "��ǰ������Ϊ: " + ds.getNumConnectionsDefaultUser());
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * �������ݿ�������Դ
     */
    public static void clearup(ResultSet rs, Statement stmt,
                               Connection conn) {
        //�ر�ResultSet
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //�ر�Statement
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //�ر�Connection
        if (conn != null) {
            try {
//            	logger.info("��ʼִ���ͷ�����,�ͷ�ǰʹ��������Ϊ:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "��ǰ������Ϊ:" + ds.getNumConnectionsDefaultUser());
                conn.close();
//                logger.info("�ͷ�����ִ�����,�ͷź�ʹ��������Ϊ:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "��ǰ������Ϊ: " + ds.getNumConnectionsDefaultUser());
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * �������ݿ�������Դ
     */
    public static void clearup(ResultSet rs, PreparedStatement pstmt) {
        clearup(rs,pstmt,null);
    }

    /**
     * �������ݿ�������Դ
     */
    public static void clearup(ResultSet rs, Statement stmt) {
        clearup(rs,stmt,null);
    }

    /**
     * ִ��һ��SQL��䲢�õ����е�һ��ֵ���紫��select treName from TreMng where treCode='0001'����õ�TreNameֵ֮
     * @return String  ��δ�鵽�򷵻ؿ��ַ�������""
     * */
    public static String getValueFromSql(String sql) throws SQLException{
        Connection conn;
        Statement stmt = null;
        ResultSet rs = null;
        conn = DatabaseHelper.getConnection();
        if(conn==null) {
            throw new SQLException("�õ����ݿ����Ӵ���");
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toUpperCase());
            if(rs.next()) {
                return rs.getString(1);
            } else {
                return "";
            }
        } catch(SQLException e) {
            logger.error("��ѯ����" + sql,e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }        
    }
    
    public static ComboPooledDataSource getComboPooledDataSource() {
    	return ds;
    }
}
