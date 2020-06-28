package org.zerone.jdbc;

/**
 *Function: 数据库连接失败
 * User     : 李国印
 * Date      : 2004-7-14 10:20:24  
 */
public class ConnRefusedException  extends Exception{
    public ConnRefusedException() {
    }
    public ConnRefusedException(String msg) {
        super(msg);
    }
    public ConnRefusedException(Exception e) {
        super(e);
    }
}
