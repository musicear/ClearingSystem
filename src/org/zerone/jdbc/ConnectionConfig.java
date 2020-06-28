package org.zerone.jdbc;

/**
 * Created by IntelliJ IDEA.
 * User: ���ӡ
 * Date: 2004-5-25
 * Time: 23:19:49
 * To change this template use Options | File Templates.
 */
public interface ConnectionConfig {
    /**
     * �õ�jndi����Դ
     */
    public String getDataSource();

    /**
     * �õ����ݿ�����
     * */
    public String getDBMS();

    /**
     * �õ�jdbc��������
     */
    public String getDriver();

    /**
     * �õ�jdbc����url
     */
    public String getUrl();

    /**
     * �õ��û���
     */
    public String getUser();

    /**
     * �õ�����
     */
    public String getPassword();


}
