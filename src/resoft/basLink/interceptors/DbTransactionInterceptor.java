package resoft.basLink.interceptors;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.DatabaseHelper;

import resoft.xlink.comm.helper.ThreadLocalContext;
import resoft.xlink.core.Message;
import resoft.xlink.transaction.TransactionInterceptor;

/**
 * <p>function:获取与释放数据库连接的拦截器</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-10-22</p>
 * <p>Time: 10:52:37</p>
 */
public class DbTransactionInterceptor implements TransactionInterceptor {
	
	private static final Log logger = LogFactory.getLog(TransactionInterceptor.class);
	
    public boolean before(Message msg) {
    	logger.info("-------------interceptor------before-----" + Thread.currentThread().getName());
        Connection conn = DatabaseHelper.getConnection();
        ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection",conn);
        return true;
    }

    public void after(Message msg) {
        Connection conn = (Connection) ThreadLocalContext.getInstance().getContext().getProperty("java.sql.Connection");
        DatabaseHelper.clearup(null,null,conn);
        logger.info("-------------interceptor------after-----" + Thread.currentThread().getName());
    }
}
