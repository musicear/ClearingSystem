package org.zerone.jdbc;
/**
* function:数据库执行错误？好像不许要这个了。为何不用SQLException?
**/
public class DbExecuteException extends Exception {
	public DbExecuteException() {
	}
	public DbExecuteException(String msg) {
		super(msg);
	}
	public DbExecuteException(Exception e) {
		super(e);
	}
}