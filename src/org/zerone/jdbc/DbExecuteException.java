package org.zerone.jdbc;
/**
* function:���ݿ�ִ�д��󣿺�����Ҫ����ˡ�Ϊ�β���SQLException?
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