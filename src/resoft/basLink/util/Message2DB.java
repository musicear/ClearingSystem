package resoft.basLink.util;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message;

/**
 * <p>function:��Message����Ϣ���������ݿ���</p>
 * <p>Author: albert lee</p>
 * <p>Date: 2005-11-15</p>
 * <p>Time: 17:11:28</p>
 */
public class Message2DB {
    private static Log logger = LogFactory.getLog(Message2DB.class);

    /**
     * ��Message���������ݿ���
     */
    public static void SaveMsg2DB(Message msg, String tableName) {
        tableName = tableName.toLowerCase();
        //���ò���
        Configuration conf = Configuration.getInstance("db");

        StringBuffer columns = new StringBuffer();
        StringBuffer values = new StringBuffer();
        //����Message
        for (Iterator itr = msg.findAllKeysByCategory(Message.DefaultCategory).iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            String columnName = conf.getProperty("fields_" + tableName, key);
            if (columnName == null || columnName.equals("")) {
                continue;
            }
            String value = (String) msg.get(key);
            value = value.replaceAll("\r", "").replaceAll("\n", "");
            if (!columns.toString().equals("")) {
                columns.append(",");
                values.append(",");
            }
            columns.append(columnName);
            if (conf.getProperty("fields_" + tableName + "_type",
                    columnName).equals("D")) {
                values.append("to_date('").append(value).append("','yyyy/mm/dd')");
            } else {
                values.append('\'').append(value).append('\'');
            }

        }
        StringBuffer sql = new StringBuffer("insert into ");
        sql.append(tableName).append(" (").append(columns).append(") values (").
                append(values).append(")");
        logger.info("sql:" + sql);
        //ִ�����ݿ�      
        DBUtil.executeUpdate(sql.toString());

    }
}
