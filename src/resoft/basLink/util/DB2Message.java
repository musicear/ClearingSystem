package resoft.basLink.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.DatabaseHelper;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * function:将数据库中的数据转化为Message形式。若有多条记录，则会调用多次onMessage
 * User: albert lee
 * Date: 2005-10-24
 * Time: 18:14:46
 */
public class DB2Message {
    private static Log logger = LogFactory.getLog(DB2Message.class);

    public DB2Message(String sql, String tableName) {
        this.sql = sql;
        this.tableName = tableName;
    }

    /**
     * 设置数据集迭代器
     * @param dataSetItr DataSetIterator
     */
    public void setDataSetIterator(DataSetIterator dataSetItr) {
        this.dataSetItr = dataSetItr;
    }

    /**
     * 开始查询
     * @throws java.sql.SQLException 数据库链接失败或配置文件错误会抛出异常
     */
    public void query() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            throw new SQLException("数据库连接失败");
        }
        String category = "key_" + tableName.toLowerCase();
        Configuration conf = Configuration.getInstance("db");
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Message msg = new DefaultMessage();
                Collection list = conf.listAllProperties(category);
                if (list == null) {
                    throw new SQLException("配置集未找到:" + category);
                }
                for (java.util.Iterator itr = list.iterator(); itr.hasNext();) {
                    String fieldCode = (String) itr.next();
                    String msgKey = conf.getProperty(category, fieldCode);
                    //得到数据类型
                    String dataType = conf.getProperty(category + "_datatype", fieldCode);
                    String value;
                    if (dataType.equals("D")) {
                        //日期型
                        Date date = rs.getDate(fieldCode);
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = rs.getString(fieldCode);
                    }
                    if (value != null && !value.equals("")) {
                        msg.set(msgKey, value);
                    }
                }
                try {
                    dataSetItr.onMessage(msg);
                } catch (Exception e) {
                    logger.error("处理失败", e);
                }
            }
        } finally {
            DatabaseHelper.clearup(rs, stmt, conn);
        }
    }

    /**
     * 数据集迭代器
     */
    public interface DataSetIterator {
        public void onMessage(Message msg) throws Exception;
    }

    private String sql, tableName;
    private DataSetIterator dataSetItr;//数据集的迭代器
}

