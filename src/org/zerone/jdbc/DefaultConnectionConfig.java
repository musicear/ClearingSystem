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
        //读取配置数据
        //此文件存在
        InputStream is = null;
        try {
            //得到配置参数
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
     * 得到数据库类型
     * */
    public String getDBMS() {
        return dbms;
    }

    public String getDataSource() {
        return dataSource;
    }

    /**
     * 得到jdbc驱动名称
     */
    public String getDriver() {
        return driver;
    }

    /**
     * 得到jdbc连接url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 得到用户名
     */
    public String getUser() {
        return user;
    }

    /**
     * 得到密码
     */
    public String getPassword() {
        return password;
    }

}
