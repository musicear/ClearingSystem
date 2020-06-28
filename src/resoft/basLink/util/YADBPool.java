package resoft.basLink.util;

/**
 * Yet Another DB Pool
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class YADBPool {
    private static Log logger = LogFactory.getLog(YADBPool.class);

    private Vector connPool = new Vector();
    private int maxConn = 20;
    private String name;
    private String password;
    private String url;
    private String user;
    private String validationQuery;

    public YADBPool(String name, String url, String user,
                            String password, int maxConnection, String testsql) {
        this.name = name;
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxConn = maxConnection;
        this.validationQuery = testsql;
    }

    public synchronized Connection getConnection() {
        Connection conn = null;
        if (hasConn()) {
            conn = getUsableConnection();
        } else if (notFull()) {
            conn = createNewConnection();
        } else if (isFull()) {
            conn = getUsableConnection();
        } else if (isOverflow()) {
            logger.debug("连接池" + name + "溢出");
            closeFirst();
            getConnection();
        }

        return conn;

    }

    /**
     * 从连接池获得一个可用连接
     */
    private Connection getUsableConnection() {
        Connection conn = (Connection) connPool.firstElement();
        connPool.removeElementAt(0);

        validateConnection(conn);

        try {
            if (conn.isClosed()) {
                logger.info("从连接池" + name + "获得一个已关闭的连接，删除之并重新获取连接");
                // 递归调用自己,尝试再次获取可用连接
                conn = getConnection();
            }
        } catch (SQLException e) {
            logger.error("从连接池" + name + "获得一个出错的连接，删除之并重新获取连接", e);
            // 递归调用自己,尝试再次获取可用连接
            conn = getConnection();
        } catch (NullPointerException e) {
            logger.error("从连接池" + name + "获得一个NULL的连接，删除之并重新获取连接", e);
            conn = getConnection();
        }
        if (isOverflow()) {
            closeFirst();
        }
        return conn;
    }

    //这个函数是针对“connection reset”异常补充的，这个异常不能被Connection.isClosed()检测到
    //通过执行一条sql语句，测试连接的有效性。
    private void validateConnection(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(validationQuery);
        } catch (SQLException ex) {
            logger.info("连接已断" + ex.getMessage());
            try {
                conn.close();
            } catch (SQLException ex1) {
                logger.info("关闭已断连接的Connection异常" + ex1.getMessage());
            }
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.info("关闭已断连接的Statement异常" + ex.getMessage());
            }
        }
    }

    private void closeFirst() {
        if (connPool.firstElement() != null) {
            Connection conn = (Connection) connPool.firstElement();
            try {
                conn.close();
                logger.debug("关闭溢出连接池" + name + "中的第一个连接");
            } catch (SQLException e) {
                logger.debug("关闭溢出连接池" + name + "中的第一个连接出错", e);
            } catch (NullPointerException e) {
                logger.error("关闭溢出连接池" + name + "中的第一个连接是NULL", e);
            }
        } else {
            logger.debug("连接池的第一个连接是null");
        }
    }

    private Connection createNewConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error(name + "连接数据库失败", e);
            return null;
        }
        return conn;
    }

    /**
     * 将连接对象放回连接池末尾
     */
    public synchronized void logicalClose(Connection conn) {
        try {
            if (conn.isClosed()) {
                logger.info("逻辑关闭" + name + "之前错误，已经被物理关闭");
            }
            connPool.addElement(conn);
            conn = (Connection) connPool.lastElement();
            if (conn.isClosed()) {
                logger.info("逻辑关闭" + name + "之后错误，已经被物理关闭");
            }
        } catch (SQLException e) {
            logger.error("检测" + name + "是否关闭异常", e);
        } catch (NullPointerException e) {
            logger.error("检测" + name + "是否关闭,取得NULL", e);
        }
    }

    public synchronized void physicalClose() {
        Enumeration allConns = connPool.elements();
        while (allConns.hasMoreElements()) {
            Connection conn = (Connection) allConns.nextElement();
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(name + "物理关闭连接错误", e);
            } catch (NullPointerException e) {
                logger.error(name + "物理关闭连接为NULL", e);
            }
        }
        connPool.removeAllElements();
    }

    private boolean hasConn() {
        if (connPool.size() > 0) {
            logger.debug("hasConn 连接池" + name + "大小：" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean isOverflow() {
        if (connPool.size() > maxConn) {
            logger.info("溢出时，连接池" + name + "大小：" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean notFull() {
        if (maxConn == 0 || connPool.size() < maxConn) {
            logger.debug("notFull 连接池" + name + "大小：" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean isFull() {
        if (connPool.size() == maxConn) {
            logger.debug("连接池" + name + "已满：" + connPool.size());
            return true;
        }
        return false;
    }

}

