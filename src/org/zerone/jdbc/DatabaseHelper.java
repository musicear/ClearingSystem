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

    //数据库类型
    private static String dbms = "oracle";

    //数据库JDBC驱动类
    private static String driver = "";

    //连接串
    private static String url = "";

    //用户名
    private static String user = "";

    //密码
    private static String password = "";

    //各数据库连接测试语句
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
     * 设置数据库url,user,password
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
			// 当连接池中的连接用完时，C3PO一次性创建新的连接数目
			ds.setAcquireIncrement(5);
			// 定义在从数据库获取新的连接失败后重复尝试获取的次数，默认为30
			ds.setAcquireRetryAttempts(30);
			// 两次连接中间隔时间默认为1000毫秒
			ds.setAcquireRetryDelay(1000);
			// 连接关闭时默认将所有未提交的操作回滚 默认为false
			ds.setAutoCommitOnClose(false);
			// 获取连接失败将会引起所有等待获取连接的线程异常,但是数据源仍有效的保留,并在下次调用getConnection()的时候继续尝试获取连接.如果设为true,那么尝试获取连接失败后该数据源将申明已经断开并永久关闭.默认为false
			ds.setBreakAfterAcquireFailure(false);
			// 当连接池用完时客户端调用getConnection()后等待获取新连接的时间,超时后将抛出SQLException,如设为0则无限期等待.单位毫秒,默认为0
			ds.setCheckoutTimeout(0);
			// 隔多少秒检查所有连接池中的空闲连接,默认为0表示不检查
			ds.setIdleConnectionTestPeriod(0);
			//  初始化时创建的连接数,应在minPoolSize与maxPoolSize之间取值.默认为3
			ds.setInitialPoolSize(25);
			// 最大空闲时间,超过空闲时间的连接将被丢弃.为0或负数据则永不丢弃.默认为0
			ds.setMaxIdleTime(0);
			// 如果设为true那么在取得连接的同时将校验连接的有效性.默认为false
			ds.setTestConnectionOnCheckin(true);
			// 连接池中保留的最小连接数据
			ds.setMinPoolSize(5);
			// 连接池中保留的最大连接数据.默认为15
			ds.setMaxPoolSize(100);
			// JDBC的标准参数,用以控制数据源内加载的PreparedStatement数据.但由于预缓存的Statement属于单个Connection而不是整个连接池.所以设置这个参数需要考滤到多方面的因素,如果maxStatements    
            // 与maxStatementsPerConnection均为0,则缓存被关闭.默认为0
			ds.setMaxStatements(0);
			// 连接池内单个连接所拥有的最大缓存被关闭.默认为0
			ds.setMaxStatementsPerConnection(0);
			// C3P0是异步操作的,缓慢的JDBC操作通过帮助进程完成.扩展这些操作可以有效的提升性能,通过多数程实现多个操作同时被执行.默为为3
			ds.setNumHelperThreads(3);
			// 用户修改系统配置参数执行前最多等待的秒数.默认为300
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
     * 读取配置文件
     */
    public DatabaseHelper() {
        //读取配置数据
        String fileName = getClass().getName().replace('.', '/') + ".properties";

        //此文件存在
        InputStream is = null;
        try {
            //得到配置参数
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
     * 注册驱动
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
     * 得到数据库连接对象Connection
     *
     * @return Connection
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
//            logger.info("当前正在使用的数据库连接数为: " + ds.getNumBusyConnectionsDefaultUser() + "     " + "当前连接数为: " + ds.getNumConnectionsDefaultUser());
        } catch (SQLException e) {
            logger.info("url=" + url + ";user=" + user + ";passwod=" + password);
            logger.info(e.getMessage());
        }
        return conn;

    }

    /**
     * 打印连接池信息
     * */
    public static void printDS() {
        //System.out.println("number Active:" + ds.getNumActive());
        //System.out.println("number Idle:" + ds.getNumIdle());
    }

    /**
     * 执行一条sql语句
     * */
    public static void executeUpdate(Connection conn,String sql) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql.toUpperCase());
            conn.commit();
        } catch(SQLException e) {
            logger.error("更新失败:sql=" + sql,e);
            throw e;
        } finally {
            clearup(null,stmt,null);
        }
    }

    /**
     * 清理数据库连接资源
     */
    public static void clearup(ResultSet rs, PreparedStatement pstmt,
                               Connection conn) {
        //关闭ResultSet
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //关闭PreparedStatement
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //关闭Connection
        if (conn != null) {
            try {
//            	logger.info("开始执行释放连接,释放前使用连接数为:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "当前连接数为:" + ds.getNumConnectionsDefaultUser());
                conn.close();
//                logger.info("释放连接执行完毕,释放后使用连接数为:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "当前连接数为: " + ds.getNumConnectionsDefaultUser());
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * 清理数据库连接资源
     */
    public static void clearup(ResultSet rs, Statement stmt,
                               Connection conn) {
        //关闭ResultSet
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //关闭Statement
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
        //关闭Connection
        if (conn != null) {
            try {
//            	logger.info("开始执行释放连接,释放前使用连接数为:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "当前连接数为:" + ds.getNumConnectionsDefaultUser());
                conn.close();
//                logger.info("释放连接执行完毕,释放后使用连接数为:" + ds.getNumBusyConnectionsDefaultUser() + "     " + "当前连接数为: " + ds.getNumConnectionsDefaultUser());
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * 清理数据库连接资源
     */
    public static void clearup(ResultSet rs, PreparedStatement pstmt) {
        clearup(rs,pstmt,null);
    }

    /**
     * 清理数据库连接资源
     */
    public static void clearup(ResultSet rs, Statement stmt) {
        clearup(rs,stmt,null);
    }

    /**
     * 执行一条SQL语句并得到其中的一个值。如传入select treName from TreMng where treCode='0001'，则得到TreName之值
     * @return String  若未查到则返回空字符串。即""
     * */
    public static String getValueFromSql(String sql) throws SQLException{
        Connection conn;
        Statement stmt = null;
        ResultSet rs = null;
        conn = DatabaseHelper.getConnection();
        if(conn==null) {
            throw new SQLException("得到数据库连接错误");
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
            logger.error("查询错误：" + sql,e);
            throw e;
        } finally {
            DatabaseHelper.clearup(rs,stmt,conn);
        }        
    }
    
    public static ComboPooledDataSource getComboPooledDataSource() {
    	return ds;
    }
}
