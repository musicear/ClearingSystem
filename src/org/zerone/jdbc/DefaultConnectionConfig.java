package org.zerone.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultConnectionConfig implements ConnectionConfig {
    private static Log logger = LogFactory.getLog(DefaultConnectionConfig.class);

    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    private static String dataSource;
    private static String dbms;

    static {
        //��ȡ��������
        //���ļ�����
        InputStream is = null;
        try {
            //�õ����ò���
            is = DefaultConnectionConfig.class.getResourceAsStream("/jdbc-config.properties");
            if (is == null) {
                logger.fatal("File not found");
                throw new RuntimeException("File Not Found");
            }
            Properties prop = new Properties();
            prop.load(is);
            dbms = (String) prop.get("dbms");
            driver = (String) prop.get("driver");
            //dataSource = (String) prop.get("datasource");
            url = (String) prop.get("url");
            user = (String) prop.get("user");
            password = (String) prop.get("password");

            dataSource = (String)prop.get("datasource");            
        } catch (IOException e) {
            logger.fatal(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.debug("Fail to close the file");
                }
            }
        }

    }

    /**
     * �õ����ݿ�����
     * */
    public String getDBMS() {
        return dbms;
    }

    public String getDataSource() {
        return dataSource;
    }

    /**
     * �õ�jdbc��������
     */
    public String getDriver() {
        return driver;
    }

    /**
     * �õ�jdbc����url
     */
    public String getUrl() {
        return url;
    }

    /**
     * �õ��û���
     */
    public String getUser() {
        return user;
    }

    /**
     * �õ�����
     */
    public String getPassword() {
        return password;
    }

}
