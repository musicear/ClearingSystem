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
            logger.debug("���ӳ�" + name + "���");
            closeFirst();
            getConnection();
        }

        return conn;

    }

    /**
     * �����ӳػ��һ����������
     */
    private Connection getUsableConnection() {
        Connection conn = (Connection) connPool.firstElement();
        connPool.removeElementAt(0);

        validateConnection(conn);

        try {
            if (conn.isClosed()) {
                logger.info("�����ӳ�" + name + "���һ���ѹرյ����ӣ�ɾ��֮�����»�ȡ����");
                // �ݹ�����Լ�,�����ٴλ�ȡ��������
                conn = getConnection();
            }
        } catch (SQLException e) {
            logger.error("�����ӳ�" + name + "���һ����������ӣ�ɾ��֮�����»�ȡ����", e);
            // �ݹ�����Լ�,�����ٴλ�ȡ��������
            conn = getConnection();
        } catch (NullPointerException e) {
            logger.error("�����ӳ�" + name + "���һ��NULL�����ӣ�ɾ��֮�����»�ȡ����", e);
            conn = getConnection();
        }
        if (isOverflow()) {
            closeFirst();
        }
        return conn;
    }

    //�����������ԡ�connection reset���쳣����ģ�����쳣���ܱ�Connection.isClosed()��⵽
    //ͨ��ִ��һ��sql��䣬�������ӵ���Ч�ԡ�
    private void validateConnection(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(validationQuery);
        } catch (SQLException ex) {
            logger.info("�����Ѷ�" + ex.getMessage());
            try {
                conn.close();
            } catch (SQLException ex1) {
                logger.info("�ر��Ѷ����ӵ�Connection�쳣" + ex1.getMessage());
            }
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.info("�ر��Ѷ����ӵ�Statement�쳣" + ex.getMessage());
            }
        }
    }

    private void closeFirst() {
        if (connPool.firstElement() != null) {
            Connection conn = (Connection) connPool.firstElement();
            try {
                conn.close();
                logger.debug("�ر�������ӳ�" + name + "�еĵ�һ������");
            } catch (SQLException e) {
                logger.debug("�ر�������ӳ�" + name + "�еĵ�һ�����ӳ���", e);
            } catch (NullPointerException e) {
                logger.error("�ر�������ӳ�" + name + "�еĵ�һ��������NULL", e);
            }
        } else {
            logger.debug("���ӳصĵ�һ��������null");
        }
    }

    private Connection createNewConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error(name + "�������ݿ�ʧ��", e);
            return null;
        }
        return conn;
    }

    /**
     * �����Ӷ���Ż����ӳ�ĩβ
     */
    public synchronized void logicalClose(Connection conn) {
        try {
            if (conn.isClosed()) {
                logger.info("�߼��ر�" + name + "֮ǰ�����Ѿ�������ر�");
            }
            connPool.addElement(conn);
            conn = (Connection) connPool.lastElement();
            if (conn.isClosed()) {
                logger.info("�߼��ر�" + name + "֮������Ѿ�������ر�");
            }
        } catch (SQLException e) {
            logger.error("���" + name + "�Ƿ�ر��쳣", e);
        } catch (NullPointerException e) {
            logger.error("���" + name + "�Ƿ�ر�,ȡ��NULL", e);
        }
    }

    public synchronized void physicalClose() {
        Enumeration allConns = connPool.elements();
        while (allConns.hasMoreElements()) {
            Connection conn = (Connection) allConns.nextElement();
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(name + "����ر����Ӵ���", e);
            } catch (NullPointerException e) {
                logger.error(name + "����ر�����ΪNULL", e);
            }
        }
        connPool.removeAllElements();
    }

    private boolean hasConn() {
        if (connPool.size() > 0) {
            logger.debug("hasConn ���ӳ�" + name + "��С��" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean isOverflow() {
        if (connPool.size() > maxConn) {
            logger.info("���ʱ�����ӳ�" + name + "��С��" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean notFull() {
        if (maxConn == 0 || connPool.size() < maxConn) {
            logger.debug("notFull ���ӳ�" + name + "��С��" + connPool.size());
            return true;
        }

        return false;
    }

    private boolean isFull() {
        if (connPool.size() == maxConn) {
            logger.debug("���ӳ�" + name + "������" + connPool.size());
            return true;
        }
        return false;
    }

}

