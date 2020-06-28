package org.zerone.jdbc;

/**
 * Created by IntelliJ IDEA.
 * User: 李国印
 * Date: 2004-5-25
 * Time: 23:19:49
 * To change this template use Options | File Templates.
 */
public interface ConnectionConfig {
    /**
     * 得到jndi数据源
     */
    public String getDataSource();

    /**
     * 得到数据库类型
     * */
    public String getDBMS();

    /**
     * 得到jdbc驱动名称
     */
    public String getDriver();

    /**
     * 得到jdbc连接url
     */
    public String getUrl();

    /**
     * 得到用户名
     */
    public String getUser();

    /**
     * 得到密码
     */
    public String getPassword();


}
